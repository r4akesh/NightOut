package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ChatpersonalActvityBinding

class ChatPersonalActvity : BaseActivity() {
    lateinit var binding : ChatpersonalActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.chatpersonal_actvity)
        binding = DataBindingUtil.setContentView(this@ChatPersonalActvity,R.layout.chatpersonal_actvity)
        initView()

    }

    private fun initView() {
         setTouchNClick(binding.chatpersonalClose)
         setTouchNClick(binding.chatpersonalImage)
         setTouchNClick(binding.chatpersonalTitle)
         setTouchNClick(binding.chatpersonalSubTitle)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(binding.chatpersonalClose==v){
            finish()
        }
       else  if(v==binding.chatpersonalImage || v==binding.chatpersonalSubTitle || v==binding.chatpersonalTitle){
            startActivity(Intent(this@ChatPersonalActvity,GroupInfoActvity::class.java))
        }
    }

    companion object {
        const val INTENT_EXTRAS_KEY_IS_GROUP = "isGroup"
        const val INTENT_EXTRAS_KEY_GROUP_DETAILS = "groupDetails"
        const val INTENT_EXTRAS_KEY_ROOM_ID = "roomID"
        const val INTENT_EXTRAS_KEY_SENDER_DETAILS = "senderDetails"
        private const val TAG = "ChatActivity"
        private const val REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE = 3
        private const val REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO = 4
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 5
        private const val REQUEST_ADD_CONTACT = 8
        private const val REQUEST_SELECT_CONTACTS = 9
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var fileName: String? = null
    }
}