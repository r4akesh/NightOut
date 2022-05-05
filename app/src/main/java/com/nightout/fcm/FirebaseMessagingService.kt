package com.nightout.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nightout.R
import com.nightout.chat.activity.ChatPersonalActvity
import com.nightout.ui.activity.NotificationActivity
import com.nightout.ui.activity.SplashActivity
import com.nightout.utils.AppConstant
import com.nightout.utils.PreferenceKeeper
import org.json.JSONObject


class FirebaseMessagingServices : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("token created", p0)
        PreferenceKeeper.instance.fcmTokenSave = p0

    }

    override fun onMessageReceived(payload: RemoteMessage) {
        super.onMessageReceived(payload)
        if (payload.notification != null) {
            var idRoom=""
            try {
                val data_obj: Map<String, String> = payload.data
                //var message = Html.fromHtml(data_obj).toString()
                var mPayloadData = data_obj["payload"]
                var mJson = JSONObject(mPayloadData)
                  idRoom = mJson.getString("id")//roomID

                Log.d("push", "onMessageReceived: $data_obj")

                sendNotification(payload.notification!!.title, payload.notification!!.body,idRoom)
            } catch (e: Exception) {
                Log.d("TAG", "onMessageReceived: " + e)
                sendNotification(payload.notification!!.title, payload.notification!!.body,idRoom)
            }
        } else {
            sendNotificationData(payload.data)

        }
    }

    private fun sendNotification(title: String?, messageBody: String?, roomId:String) {
        val intent: Intent
        if (title!!.contains("New message from"))
            intent = Intent(this, ChatPersonalActvity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_PUSH, true)
            .putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_ROOM_ID, roomId)
        else
            intent = Intent(this, NotificationActivity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_PUSH, true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendNotificationData(data: MutableMap<String, String>) {
        val appointId = data["Id"]!!.toInt()
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("appointId", appointId)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}