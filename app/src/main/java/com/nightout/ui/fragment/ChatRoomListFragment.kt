package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightout.R
import com.nightout.chat.activity.ChatPersonalActvity
import com.nightout.chat.activity.CreateGroupActvity
import com.nightout.chat.adapter.ChatGroupListAdapter
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.model.FSRoomModel
import com.nightout.chat.model.ResponseModel
import com.nightout.chat.model.RoomNewResponseModel
import com.nightout.chat.model.RoomResponseModel
import com.nightout.databinding.FragmentChatBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.FSUsersModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.services.APIClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ChatRoomListFragment() : Fragment() , View.OnClickListener , WebSocketObserver {

    lateinit var binding : FragmentChatBinding
    private var onMenuOpenListener: OnMenuOpenListener? = null
    lateinit var chatAdapter: ChatGroupListAdapter

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        WebSocketSingleton.getInstant()!!.register(this)
        setRoomList()
        initView()
        joinCommand()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(MyApp.getStatus(AppConstant.INTENT_EXTRAS.IsUserExitClickBtn)){
            joinCommand()
            MyApp.setStatus(AppConstant.INTENT_EXTRAS.IsUserExitClickBtn,false)
        }
    }
    override fun onStart() {
        super.onStart()
        WebSocketSingleton.getInstant()?.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ok", "onDestroy: ")
        WebSocketSingleton.getInstant()?.unregister(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i("ok", "onStop: ")
       /// WebSocketSingleton.getInstant()?.unregister(this)
    }


    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            val userList = JSONArray()
            userList.put(PreferenceKeeper.instance.myUserDetail.id)
         //  userList.put( PreferenceKeeper.instance.loginUser?.id)
            //userList.put( 1)
            jsonObject.put("type", "allRooms")
            jsonObject.put("userList", userList)
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_ROOM)
            //			jsonObject.put("room", roomId);
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
    }

    override fun onClick(v: View?) {
        if(v==binding.headerChat.headerSideMenu){
            onMenuOpenListener?.onOpenMenu()
        }

        else   if(v==binding.headerChat.headerCreateGroup){
            startActivity(Intent(requireContext(), CreateGroupActvity::class.java))

        }
    }
    private fun initView() {
        binding.headerChat.headerSideMenu.setOnClickListener(this)
        binding.headerChat.headerCreateGroup.setOnClickListener(this)
    }


    private fun setRoomList() {
        chatAdapter = ChatGroupListAdapter(requireContext(),object: ChatGroupListAdapter.ClickListener{
            override fun onClick(item: FSRoomModel) {
                val intent = Intent(requireActivity(), ChatPersonalActvity::class.java)
                intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_IS_GROUP, item.isGroup)
                intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_GROUP_DETAILS, item.groupDetails)
                intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_ROOM_ID, item.roomId)
                intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_SENDER_DETAILS, item.senderUserDetail)
                intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_PARTICIPENT_SIZE, item.userList.size.toString())
                startActivity(intent)
            }

        })
        binding.fragmentChatRecycler.also {
            it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            it.adapter = chatAdapter
        }
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            requireActivity().runOnUiThread {
                println("received message chat: $response")
                val gson = Gson()
                if (ResponseType.RESPONSE_TYPE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> = gson.fromJson(response, type1)
                        MyApp.saveUserDetailChatUsers(roomResponseModelResponseModel.getData().userListMap)
                        var vv = MyApp.fetchUserDetailChatUsers()
                      //  UserDetails.instance.chatUsers = roomResponseModelResponseModel.getData().userListMap
                        for (element in roomResponseModelResponseModel.getData().roomList) {
                            for (userId in element.userList) {
                                 if (userId != PreferenceKeeper.instance.myUserDetail.id) {
                               // if (userId != PreferenceKeeper.instance.loginUser?.id) {
                                    //element.senderUserDetail = UserDetails.instance.chatUsers[userId]
                                    element.senderUserDetail = MyApp.fetchUserDetailChatUsers().get(userId)
                                    break
                                }
                            }
                        }
                        // data filter by  group
                        var roomListGroup = ArrayList<FSRoomModel>()
                        for (i in 0 until roomResponseModelResponseModel.getData().roomList.size){
                            if(roomResponseModelResponseModel.getData().roomList[i].isGroup){
                                roomListGroup.add(roomResponseModelResponseModel.getData().roomList[i])
                            }
                        }
                        if(roomListGroup.size>0) {
                            chatAdapter.addAll(roomListGroup)
                            binding.chtGrpItemNoDataConstrent.visibility=GONE
                        }
                        else{
                            chatAdapter.addAll(ArrayList<FSRoomModel>())
                            binding.chtGrpItemNoDataConstrent.visibility=VISIBLE
                        }
                    } else {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_MODIFIED.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<FSRoomModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<FSRoomModel> = gson.fromJson<ResponseModel<FSRoomModel>>(response, type1)
                        for (userId in roomResponseModelResponseModel.getData().userList) {
                            if (userId != PreferenceKeeper.instance.myUserDetail.id) {
                              //  roomResponseModelResponseModel.getData().senderUserDetail = UserDetails.instance.chatUsers[userId]
                                roomResponseModelResponseModel.getData().senderUserDetail = MyApp.fetchUserDetailChatUsers().get(userId)
                                break
                            }
                        }
                        chatAdapter.updateElement(roomResponseModelResponseModel.getData())
                    } else {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_CREATE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 =
                            object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomNewResponseModel> =
                            gson.fromJson(response, type1)
                        val tmpUserList: HashMap<String, FSUsersModel> = roomResponseModelResponseModel.getData().userListMap
                        for (key in tmpUserList.keys) {
                           // UserDetails.instance.chatUsers[key] = tmpUserList[key]!!
                            MyApp.fetchUserDetailChatUsers()[key] = tmpUserList[key]!!
                        }
                        val element: FSRoomModel =
                            roomResponseModelResponseModel.getData().newRoom!!
                        for (userId in element.userList) {
                            if (userId != PreferenceKeeper.instance.myUserDetail.id) {
                              //  element.senderUserDetail = UserDetails.instance.chatUsers[userId]
                                element.senderUserDetail = MyApp.fetchUserDetailChatUsers()[userId]
                                break
                            }
                        }
                        binding.chtGrpItemNoDataConstrent.visibility=GONE
                        chatAdapter.addOrUpdate(element)
                    } else if (ResponseType.RESPONSE_TYPE_USER_MODIFIED.equalsTo(type)) {
                        Log.d("ok", "received message: $response")
                        val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                        val fsUsersModelResponseModel: ResponseModel<FSUsersModel> =
                            Gson().fromJson<ResponseModel<FSUsersModel>>(response, type1)
                        chatAdapter.updateUserElement(fsUsersModelResponseModel.getData())
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("ok", "onWebSocketResponse: $type")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override val activityName: String = ChatRoomListFragment::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_ROOM,
            ResponseType.RESPONSE_TYPE_ROOM_MODIFIED,
            ResponseType.RESPONSE_TYPE_CREATE_ROOM,
            ResponseType.RESPONSE_TYPE_USER_MODIFIED
        )
    }

}