package com.nightout.chat.chatinterface;


import android.os.Handler
import android.os.Looper
import android.util.Log
import com.nightout.chat.utility.UserDetails
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.services.APIClient
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

// Uses the Subject interface to update all Observers
class WebSocketSingleton : WebSocketListener(), WebSocketSubject {
    private val webSocketObservers: ArrayList<WebSocketObserver> = ArrayList()
    private var webSocket: WebSocket? = null
    override fun register(o: WebSocketObserver) {
        val observerIndex = webSocketObservers.indexOf(o)
        if (observerIndex == -1) {
            // Adds a new observer to the ArrayList
            webSocketObservers.add(o)
        } else {
            Log.d(TAG, "Subscriber is already registered")
        }
    }

    override fun unregister(o: WebSocketObserver) {

        // Get the index of the observer to delete
        val observerIndex = webSocketObservers.indexOf(o)

        // Print out message (Have to increment index to match)
        println("Observer " + (observerIndex + 1) + " deleted")

        // Removes observer from the ArrayList
        if (observerIndex != -1) webSocketObservers.removeAt(observerIndex)
    }

    override fun notifyObserver(response: String) {
        try {
            val jsonObject = JSONObject(response)
            val responseType = jsonObject.getString("type")
            val message = jsonObject.getString("message")
            val statusCode = jsonObject.getInt("statusCode")

            // Cycle through all observers and notifies them of
            // price changes
            for (webSocketObserver in webSocketObservers) {
                Log.d(TAG, "notifyObserver: " + webSocketObserver.activityName)
                val registeredFor = webSocketObserver.registerFor()
                for (element in registeredFor) {
                    if (element.equalsTo(responseType)) {
                        webSocketObserver.onWebSocketResponse(
                            response,
                            responseType,
                            statusCode,
                            message
                        )
                        break
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun startWebSocket() {

        // WebSocket
        val request = Request.Builder().url(APIClient.BASE_URL_WEB_SOCKET).build()
        val okHttpClient = OkHttpClient()
        webSocket = okHttpClient.newWebSocket(request, this)
       // okHttpClient.dispatcher().executorService().shutdown()
    }

    private fun reconnectWebSocket() {

        // WebSocket
        val request = Request.Builder().url(APIClient.BASE_URL_WEB_SOCKET).build()
        val okHttpClient = OkHttpClient()
        webSocket = okHttpClient.newWebSocket(request, this)
        //        okHttpClient.dispatcher().executorService().shutdown();
    }

    fun sendMessage(command: JSONObject) {
        webSocket!!.send(command.toString())
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "WebSocket connect stable")
        joinCommand()
    }

    private fun joinCommand() {
        PreferenceKeeper.instance.getRegisterUser()?.let { userDetails ->
            UserDetails.instance.myDetail = userDetails
            val jsonObject = JSONObject()
            try {
                jsonObject.put(
                    "user_id",
                    userDetails.id
                )
                jsonObject.put("type", "create")
                jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_CREATE_CONNECTION)
                getInstant()!!.sendMessage(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("received message WebSocket: $text")
        notifyObserver(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("onClosing: $code / $reason")
        closeConnection(webSocket)
    }

    private fun closeConnection(webSocket: WebSocket?) {
        webSocket?.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("onFailure: " + t.message)
        //        closeConnection(webSocket);
        Handler(Looper.getMainLooper()).postDelayed({ reconnectWebSocket() }, 3000)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 10000
        private const val TAG = "DownloadUtility"

        //    public static HashMap<String, DownloadRequest> downloadList = new HashMap<>();
        private var webSocketSingleTon: WebSocketSingleton? = null
        fun getInstant(): WebSocketSingleton? {
            if (webSocketSingleTon == null) {
                webSocketSingleTon = WebSocketSingleton()
            }
            return webSocketSingleTon
        }
    }

    init {
        // Creates an ArrayList to hold all observers
        startWebSocket()
    }
}