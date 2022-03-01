package com.nightout.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.model.ChatModel
import com.nightout.chat.utility.PermissionClass
import com.nightout.chat.utility.UserDetails
import com.nightout.databinding.ChatpersonalActivitynewBinding
import com.nightout.databinding.ChatpersonalActvityBinding
import com.nightout.model.FSUsersModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator

class ChatPersonalActvity : BaseActivity(),PermissionClass.PermissionRequire, WebSocketObserver {
    lateinit var binding : ChatpersonalActivitynewBinding
    private lateinit var permissionClass: PermissionClass
    private var _roomId: String? = null
    private val chatListTmp = ArrayList<ChatModel>()
    private val chatList = HashMap<Date, ArrayList<ChatModel>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.chatpersonal_actvity)
        binding = DataBindingUtil.setContentView(this@ChatPersonalActvity,R.layout.chatpersonal_activitynew)
        permissionClass = PermissionClass(this, this)
        binding.attachmentWrapper.visibility = View.INVISIBLE
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.sendButton.setOnClickListener(this)
        binding.backBtnImage.setOnClickListener(this)
        binding.chatAttachment.setOnClickListener(this)
        binding.attachmentCamera.setOnClickListener(this)
        binding.attachmentGallery.setOnClickListener(this)
        binding.attachmentLocation.setOnClickListener(this)
        binding.chatGoToBottom.setOnClickListener(this)
        binding.chatUnblockBtn.setOnClickListener(this)
        binding.chatUserNameConstrant.setOnClickListener(this)
        adapter = setUpRecyclerView()
        binding.chatGoToBottom.visibility = View.GONE

        parseExtras()
        WebSocketSingleton.getInstant()?.register(this)
    }

    private fun parseExtras() {

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionClass.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun permissionDeny() {}

    override fun permissionGranted(flag: Int) {
        when (flag) {
          //  REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE -> selectCameraImage()  //doLater
         //   REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO -> showPicVideoDialog()
//            REQUEST_RECORD_AUDIO_PERMISSION -> {
//                onRecord(mStartRecording)
//                if (mStartRecording) {
//                    binding.chatStartRecording.text = "Stop recording"
//                } else {
//                    binding.chatStartRecording.text = "Start recording"
//                }
//                mStartRecording = !mStartRecording
//            }
        }
    }

    override fun listOfPermission(flag: Int): Array<String> {
       /* if (flag == REQUEST_RECORD_AUDIO_PERMISSION) {
            return arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
        }*/   if (flag == REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE || flag == REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO) {
            return arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onWebSocketResponse(
        response: String,
        type: String,
        statusCode: Int,
        message: String?
    ) {
        Log.d(TAG, "received message: $response")
        runOnUiThread {
            try {
                if (ResponseType.RESPONSE_TYPE_MESSAGES.equalsTo(type)) {
                    val responseData = JSONObject(response)
                    val responseCode = responseData.getInt("statusCode")
                    if (responseCode == 200) {
                        val jsonObject = responseData.getJSONArray("data")
                        for (i in 0 until jsonObject.length()) {
                            val nthObject = jsonObject.getJSONObject(i)
                            appendMessage(nthObject, false)
                        }
                        setRead()

                        ///Move to bottom if user open chat first time
                        binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    } else if (responseCode == 201) {
                        appendMessage(responseData.getJSONObject("data"), true)
                        //                        binding.chatNewMessageCount
                        setRead()
                    } else {
                        Toast.makeText(
                            this@ChatActivity,
                            responseData.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }


//                        if (jsonObject.has("message")) {
//                            ChattingModel chattingModel = ChattingModel.getModel(jsonObject, PreferenceUtils.getLoginUser(ChatActivity.this).getId());
//                            chattingAdapter.updateList(chattingModel);
////                            chatRecyclerView.add
//                            binding.chatRecyclerView.scrollToPosition(chattingAdapter.items.size() - 1);
//                        }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_DETAILS.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> =
                            Gson().fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                        for (element in roomResponseModelResponseModel.getData().userList) {
                            UserDetails.chatUsers.put(element.id, element)
                        }
                        val roomDetails: FSRoomModel =
                            roomResponseModelResponseModel.getData().roomList.get(0)
                        _senderDetails = roomDetails.senderUserDetail!!
                        /*for (FSRoomModel elemen  t : roomResponseModelResponseModel.getData().getRoomList()) {
                            for (String userId : element.getUserList()) {
                                if (!userId.equals(UserDetails.myDetail.getId())) {
                                    element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                    break;
                                }
                            }
                        }*/joinCommand()
                        blockList
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_USER_MODIFIED.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                    val fsUsersModelResponseModel: ResponseModel<FSUsersModel> =
                        Gson().fromJson<ResponseModel<FSUsersModel>>(response, type1)
                    _senderDetails = fsUsersModelResponseModel.getData()
                    if (_isGroup) {
//                    setGroupDetails();
                    } else {
                        setIndividualDetails()
                    }

//                otherUser = UserDetails.chatUsers.get(id);
//
//                setOtherDetails();
                } else if (ResponseType.RESPONSE_TYPE_USER_BLOCK_MODIFIED.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    val type1 = object : TypeToken<ResponseModel<UserBlockModel?>?>() {}.type
                    val userBlockModelResponseModel: ResponseModel<UserBlockModel> =
                        Gson().fromJson<ResponseModel<UserBlockModel>>(response, type1)
                    val element: UserBlockModel = userBlockModelResponseModel.getData()
                    if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                        blockedByOtherUser()
                    }
                    if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                        unBlockedByOtherUser()
                    }
                    if (element.blockedTo == _senderDetails.id && element.blockedBy == UserDetails.myDetail.id && element.isBlock) {
                        isBlocked = true
                    }
                } else if (ResponseType.RESPONSE_TYPE_USER_ALL_BLOCK.equalsTo(type)) {
                    Log.d(TAG, "received message: $response")
                    if (!_isGroup) {
                        val type1 = object :
                            TypeToken<ResponseModel<ArrayList<UserBlockModel?>?>?>() {}.type
                        val userBlockModelResponseModel: ResponseModel<ArrayList<UserBlockModel>> =
                            Gson().fromJson<ResponseModel<ArrayList<UserBlockModel>>>(
                                response,
                                type1
                            )
                        for (element in userBlockModelResponseModel.getData()) {
                            if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                                blockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == UserDetails.myDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                                unBlockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == _senderDetails.id && element.blockedBy == UserDetails.myDetail.id && element.isBlock) {
                                isBlocked = true
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "onWebSocketResponse: $type")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(JSONException::class)
    private fun appendMessage(chatData: JSONObject, showMessageCount: Boolean) {
        val senderDetails: FSUsersModel =
            UserDetails.instance.chatUsers.get(chatData.getString("sender_id"))!!
        val chatModel = ChatModel(chatData, senderDetails)
        if (chatModel.roomId == _roomId) {
            chatListTmp.add(chatModel)
            val tempDate = chatModel.createdDate
            var correspondingChatList = chatList[tempDate]
            if (correspondingChatList == null) {
                correspondingChatList = ArrayList()
                chatList[tempDate] = correspondingChatList
            }
            correspondingChatList.add(chatModel)
            correspondingChatList.sortWith(Comparator { o1: ChatModel, o2: ChatModel ->
                o1.messageDate.compareTo(
                    o2.messageDate
                )
            })
            val keys = ArrayList(chatList.keys)
            keys.sort()
            adapter.clearAll()
            for (key in keys) {
                val chatForThatDay = chatList[key]!!
                //										HeaderDataImpl headerData1 = new HeaderDataImpl(R.layout.header1_item_recycler, key);
                val headerData1 = HeaderDataImpl(R.layout.header1_item_recycler, key)
                adapter.setHeaderAndData(chatForThatDay as List<ChatModel?>, headerData1)
            }
            if (showMessageCount && chatModel.sender_detail.id != UserDetails.myDetail.id) {
                noOfNewMessages += 1
                binding.chatGoToBottom.visibility = View.VISIBLE
                binding.chatNewMessageCount.text = noOfNewMessages.toString()
                if (noOfNewMessages <= 1) {
                    val layout = binding.chatRecyclerView.layoutManager as LinearLayoutManager?
                    layout!!.scrollToPosition(layout.findLastVisibleItemPosition() + 1)
                }
            }
            if (chatModel.sender_detail.id == UserDetails.myDetail.id) {
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)


                /* int offset = binding.chatRecyclerView.computeVerticalScrollOffset();
                int extent = binding.chatRecyclerView.computeVerticalScrollExtent();
                int range = binding.chatRecyclerView.computeVerticalScrollRange();

                float percentage = (100.0f * offset / (float)(range - extent));
                Log.d(TAG, "appendMessage: " + percentage);*/
//                ((LinearLayoutManager) binding.chatRecyclerView.getLayoutManager()).scrollToPositionWithOffset(2, 20);
            }
        }
    }

    override val activityName: String
        get() = TODO("Not yet implemented")

    override fun registerFor(): Array<ResponseType> {
        TODO("Not yet implemented")
    }
}