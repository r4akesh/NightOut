package com.nightout.chat.activity


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.request.DownloadRequest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi

import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.callbacks.OnSelectOptionListener
import com.nightout.chat.adapter.ChatAdapter
import com.nightout.chat.adapter.HeaderDataImpl
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.fragment.PlayAudioFragment
import com.nightout.chat.fragment.UploadFileProgressFragment
import com.nightout.chat.model.*
import com.nightout.chat.stickyheader.stickyView.StickHeaderItemDecoration
import com.nightout.chat.utility.*
import com.nightout.databinding.ChatpersonalActivitynewBinding
import com.nightout.model.ChatImgUploadResponse
import com.nightout.model.FSUsersModel
import com.nightout.ui.activity.HomeActivityNew
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment
import com.nightout.utils.*
import com.nightout.vendor.services.APIClient
//import com.theartofdev.edmodo.cropper.CropImage
import org.apache.commons.io.FilenameUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.Comparator

class ChatPersonalActvity : BaseActivity(), PermissionClass.PermissionRequire, WebSocketObserver,
    PlayAudioFragment.PlayAudioCallback,
    UploadFileProgressFragment.UploadFileProgressCallback, OnSelectOptionListener {
    lateinit var binding: ChatpersonalActivitynewBinding
    private lateinit var permissionClass: PermissionClass
    private var _roomId: String? = null
    private val chatListTmp = ArrayList<ChatModel>()
    private val chatList = HashMap<Date, ArrayList<ChatModel>>()
    private lateinit var adapter: ChatAdapter
    private lateinit var _senderDetails: FSUsersModel
    private var _isGroup = false
    private var isBlocked = false
    private var noOfNewMessages = 0
    private var _groupDetails: FSGroupModel? = null
    private val uploadFragment: PlayAudioFragment = PlayAudioFragment()
    private var isSetWallpaper = false
    private val VIDEO_DIRECTORY = "/demonuts"
    var isFromPush = false
    var mChatListAllMsg = ArrayList<ChatModel>()
    var listMedia = ArrayList<String>()
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment
    var LAUNCH_GOOGLE_ADDRESS = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.chatpersonal_actvity)
        binding = DataBindingUtil.setContentView(
            this@ChatPersonalActvity,
            R.layout.chatpersonal_activitynew
        )
        permissionClass = PermissionClass(this, this)
        binding.attachmentWrapper.visibility = View.INVISIBLE
        // this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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
        setAudioPlayer()
        listMedia = ArrayList<String>()
        parseExtras()

        binding.chatRecyclerView.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                binding.chatRecyclerView.postDelayed(Runnable {
                    if (adapter.itemCount > 0) {
                        binding.chatRecyclerView.scrollToPosition(
                            binding.chatRecyclerView.getAdapter()?.getItemCount()!! - 1
                        )
                    }
                }, 10)
            }
        })

        //  WebSocketSingleton.getInstant()?.register(this)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        isFromPush = intent?.getBooleanExtra(AppConstant.INTENT_EXTRAS.ISFROM_PUSH, false)!!
    }

    override fun onBackPressed() {
        if (isFromPush) {
            val i = Intent(this, HomeActivityNew::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        } else {
            super.onBackPressed()
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.back_btnImage -> {
                finish()
            }
            R.id.chatUnblockBtn -> {
                //   blockOrUnblock(false)
            }
            R.id.chatGoToBottom -> {
                binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                resetNewMessageCount()
            }
            R.id.sendButton -> {
                if (!binding.messageArea.text.toString().isEmpty()) {
                    val message = binding.messageArea.text.toString()
                    val emailFilteredString: String =
                        StringUtilities.replaceEmailAddressWithStarsInString(message)
                    val mobileFilteredString: String =
                        StringUtilities.replaceMobileWithStarsInString(emailFilteredString)!!
                    sendMessage(mobileFilteredString, ChatModel.MessageType.text, HashMap())
                    binding.messageArea.setText("")
                }
            }
            R.id.openRecorder -> {
                binding.chatMessageWrapper.visibility = View.GONE
                binding.chatRecordingWrapper.visibility = View.VISIBLE
            }
            R.id.chatAttachment -> {
                toggleAttachmentWrapper()
            }
            R.id.chatCloseRecording -> {
//                stopRecording()
//                mStartRecording = true
//                binding.chatMessageWrapper.visibility = View.VISIBLE
//                binding.chatRecordingWrapper.visibility = View.GONE
            }
            R.id.attachmentGallery -> {
                toggleAttachmentWrapper()

                val intent = Lassi(this)
                    .with(LassiOption.CAMERA_AND_GALLERY)
                    .setMaxCount(1)
                    .setGridSize(3)
                    .setMinTime(5) // for MediaType.VIDEO only
                    .setMaxTime(30) // for MediaType.VIDEO only
                    .setMediaType(MediaType.VIDEO)
                    .setCompressionRation(10)
                    .disableCrop()
                    .build()
                receiveDataVideo.launch(intent)

                // onSelectImage()
                binding.attachmentWrapper.visibility = View.GONE
                // permissionClass.askPermission(REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO)
            }
            R.id.attachmentCamera -> {
                toggleAttachmentWrapper()
                onSelectImage()
                binding.attachmentWrapper.visibility = View.GONE
                // permissionClass.askPermission(REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE)
            }
            R.id.attachmentLocation -> {
                toggleAttachmentWrapper()
                Places.initialize(
                    this@ChatPersonalActvity,
                    resources.getString(R.string.google_place_picker_key)
                )
                val fieldList =
                    Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
                val intent =
                    Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
                        .build(this@ChatPersonalActvity)
                startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS)
                overridePendingTransition(0, 0)
            }
            R.id.chatStartRecording -> {
                permissionClass.askPermission(REQUEST_RECORD_AUDIO_PERMISSION)
            }
            R.id.sendAudioButton -> {
                // sendRecording()
            }
            R.id.attachmentContact -> {
//                toggleAttachmentWrapper()
//                contacts
            }
            R.id.chatUserNameConstrant -> {
                if (_isGroup) {
                    startForResultGroupInfo.launch(
                        Intent(this@ChatPersonalActvity, GroupInfoActvity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.ROOM_ID, _roomId)
                            .putExtra(AppConstant.INTENT_EXTRAS.ALLCHAT_MSG, listMedia)
                    )
                }
            }
        }
    }

    val startForResultGroupInfo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.IS_GROUP_EXIT,false)) {
                    finish()
                }
                else{   data not set
                    binding.chatChatWithUserName.text= intent.getStringExtra(AppConstant.INTENT_EXTRAS.GROUP_NAME)
                    var imgUrl= intent.getStringExtra(AppConstant.INTENT_EXTRAS.GROUP_IMG_URL)
                    Glide.with(this)
                        .setDefaultRequestOptions(MyApp.USER_PROFILE_DEFAULT_GLIDE_CONFIG)
                        .load(imgUrl)
                        .into(binding.chatUserProfile)
                }
            }
        }

    private fun onSelectImage() {
        if (!Utills.checkingPermissionIsEnabledOrNot(this)) {
            Utills.requestMultiplePermission(this, CreateGroupActvity.requestPermissionCode)
        } else {
            selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this, "")
            selectSourceBottomSheetFragment.show(
                supportFragmentManager,
                "selectSourceBottomSheetFragment"
            )
        }
    }

    override fun onOptionSelect(option: String) {
        if (option == "camera") {
            selectSourceBottomSheetFragment.dismiss()
            val intent = Lassi(this)
                .with(LassiOption.CAMERA)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE)
                .setCompressionRation(10)
                .build()
            receiveData.launch(intent)
        } else {
            selectSourceBottomSheetFragment.dismiss()
            val intent = Lassi(this)
                .with(LassiOption.GALLERY)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE)
                .setCompressionRation(10)
                .build()
            receiveData.launch(intent)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             val result = CropImage.getActivityResult(data)
             if (resultCode == RESULT_OK) {
                 val resultUri: Uri?
                 if (result != null) {
                     resultUri = result.uri
                     if (resultUri != null) {
                         if (isSetWallpaper) {
                             //  setWallpaper(resultUri) //doLater
                         } else {
                             uploadImageFormUri(resultUri)
                         }
                     }
                 }
             } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 val error: Exception
                 if (result != null) {
                     error = result.error
                     error.printStackTrace()
                 }
             }
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                contentURI?.let { uploadVideoFormUri(it) }
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                val contentURI = data.data
                contentURI?.let { uploadVideoFormUri(it) }
            }
        } else if (requestCode == ImagePicker.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                //val bitmap: Bitmap = BitmapFactory.decodeFile(ImagePicker.photoFile!!.absolutePath)

                var mUri = Uri.fromFile(File(ImagePicker.photoFile!!.absolutePath))
                uploadImageFormUri(mUri)


            }
        } else if (requestCode == LAUNCH_GOOGLE_ADDRESS && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            var lat = place.latLng?.latitude
            var lang = place.latLng?.longitude
            val messageContent = HashMap<String, Any?>()

            messageContent["name"] = place.address
            messageContent["address"] =
                "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lang + "&zoom=12&size=400x400&key=AIzaSyBrwXwOTaLkh10-1l6ZLWuPQK9jr5h3tOM"
            messageContent["latitude"] = lat
            messageContent["longitude"] = lang

            sendMessageLocation(messageContent)
        }
    }

    private val receiveData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia =
                    it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    val resultUri = Uri.parse(selectedMedia[0].path)
                    try {
                        //  var   bitmap = BitmapFactory.decodeFile(selectedMedia[0].path)
                        uploadImageFormUri(resultUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    private val receiveDataVideo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia =
                    it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    val resultUri = Uri.parse(selectedMedia[0].path)
                    Log.d("TAG", ": " + resultUri)
                    try {
                        //  var   bitmap = BitmapFactory.decodeFile(selectedMedia[0].path)
                        uploadVideoFormUri(resultUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()
        WebSocketSingleton.getInstant()?.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        WebSocketSingleton.getInstant()?.unregister(this)
        //        webSocket.cancel();
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
        WebSocketSingleton.getInstant()?.unregister(this)

//        mUserRef.child(currentUser.getUid()).child("isOnline").setValue(false);
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun toggleAttachmentWrapper() {
        revealLayoutFun()
        //  binding.attachmentWrapper.visibility = if (binding.attachmentWrapper.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun revealLayoutFun() {
        if (binding.attachmentWrapper.visibility == View.VISIBLE) {
            // get the center for the clipping circle
            val cx = binding.attachmentWrapper.width / 2
            val cy = binding.attachmentWrapper.height / 2

            // get the initial radius for the clipping circle
            val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animation (the final radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(
                binding.attachmentWrapper,
                cx,
                cy,
                initialRadius,
                0f
            )

            // make the view invisible when the animation is done
            anim.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.attachmentWrapper.visibility = View.INVISIBLE
                }
            })

            // start the animation
            anim.start()
        } else {

            val cx = binding.attachmentWrapper.width / 2
            val cy = binding.attachmentWrapper.height / 2

            // get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)
            val anim =
                ViewAnimationUtils.createCircularReveal(
                    binding.attachmentWrapper,
                    cx,
                    cy,
                    0f,
                    finalRadius
                )
            // make the view visible and start the animation
            binding.attachmentWrapper.visibility = View.VISIBLE
            anim.start()

        }

    }

    private fun setAudioPlayer() {
        uploadFragment.setPlayAudioCallback(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.audioPlayerFragment, uploadFragment)
        ft.commit()
    }


    private fun setUpRecyclerView(): ChatAdapter {
        val adapter = ChatAdapter(this, object : ChatAdapter.ChatCallbacks {
            override fun onClickDownload(
                chatModel: ChatModel?,
                messageContent: MediaModel?,
                stopDownloading: Boolean,
                onDownloadListener: OnDownloadListener?
            ) {
                if (chatModel!!.message_type == ChatModel.MessageType.image) {
                    val intent = Intent(this@ChatPersonalActvity, ZoomImageActivity::class.java)
                    intent.putExtra(ZoomImageActivity.INTENT_EXTRA_URL, messageContent?.file_url)
                    startActivity(intent)
                } else {
                    downloadFile(chatModel, messageContent, stopDownloading, onDownloadListener)
                }
            }

            override fun onClickLocation(chatModel: ChatModel?, locationModel: LocationModel?) {
                val geoUri =
                    "http://maps.google.com/maps?q=loc:" + locationModel!!.latitude + "," + locationModel.longitude + " (" + locationModel.name + ")"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                startActivity(intent)
            }

            override fun onClickContact(chatModel: ChatModel?, contactModel: ContactModel?) {
                /*    val dialogBuilder: ContactDialog.DialogBuilder =
                        ContactDialog.DialogBuilder(this@ChatPersonalActvity)
                            .setOnItemClick(object : ContactDialog.OnItemClick {
                                override fun addContactNew(dialog: ContactDialog?) {
                                    *//*	Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                                contactIntent
                                        .putExtra(ContactsContract.Intents.Insert.NAME, contactModel.getFirst_name() + " " + contactModel.getLast_name())
                                        .putExtra(ContactsContract.Intents.Insert.PHONE, contactModel.getMobile());

                                startActivityForResult(contactIntent, REQUEST_ADD_CONTACT);*//*
                                val contactIntent = Intent(Intent.ACTION_INSERT_OR_EDIT)
                                contactIntent.type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                                contactIntent
                                    .putExtra(
                                        ContactsContract.Intents.Insert.NAME,
                                        contactModel?.first_name + " " + contactModel?.last_name
                                    )
                                    .putExtra(
                                        ContactsContract.Intents.Insert.PHONE,
                                        contactModel?.mobile
                                    )
                                startActivityForResult(contactIntent, REQUEST_ADD_CONTACT)
                            }

                            override fun close(dialog: ContactDialog) {
                                dialog.cancel()
                            }
                        }).setContactNumber(contactModel?.mobile!!)
                        .setTitle(contactModel.first_name + " " + contactModel.last_name)
                val dialog: ContactDialog = dialogBuilder.build()
                val display = windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = (size.x * 0.90).toInt()
                //        int height = size.y;
                dialog.show()
                dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)*/
            }

            override fun onLongClick(chatModel: ChatModel?) {
                Toast.makeText(this@ChatPersonalActvity, "On long click ", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        val layoutManager = LinearLayoutManager(this)

//		setData(adapter);
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = layoutManager
        binding.chatRecyclerView.addItemDecoration(StickHeaderItemDecoration(adapter))
        binding.chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.d(TAG, "onScrolled: " + dx + " " + dy);
//                if (dy > 0) { //check for scroll down
                val layout = binding.chatRecyclerView.layoutManager as LinearLayoutManager?
                if (layout!!.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    resetNewMessageCount()
                }


//                layout.scrollToPosition();

//                Log.d(TAG, "onScrolled: " + layout.findLastVisibleItemPosition() + " " + adapter.getItemCount());

//                }
            }
        })
        return adapter
    }

    private fun resetNewMessageCount() {
        noOfNewMessages = 0
        binding.chatGoToBottom.visibility = View.GONE
        binding.chatNewMessageCount.text = noOfNewMessages.toString()
    }

    private fun downloadFile(
        chatModel: ChatModel?,
        appListModel: MediaModel?,
        stopDownloading: Boolean,
        onDownloadListener: OnDownloadListener?
    ) {
        val downloadUrl: String = appListModel!!.file_url
        try {
            val url = URL(downloadUrl)
            val downloadFileName = FilenameUtils.getName(url.path)
            val downloadFile: File = File(
                DownloadUtility.getPath(
                    applicationContext,
                    DownloadUtility.FILE_PATH_CHAT_FILES
                ) + "/" + downloadFileName
            )
            val isAppDownloaded = downloadFile.exists()
            if (isAppDownloaded) {
                chatModel!!.downloadStatus = ChatModel.DownloadStatus.DOWNLOADED
                val filePath = downloadFile.absolutePath
                when {
                    FileOpenUtility.isVideo(filePath) -> {
                        val intent = Intent(this, PlayerActivity::class.java)
                        intent.putExtra(PlayerActivity.INTENT_EXTRA_FILE_PATH, filePath)
                        startActivity(intent)
                    }
                    FileOpenUtility.isAudio(filePath) -> {
                        binding.audioPlayerFragment.visibility = View.VISIBLE
                        uploadFragment.play(filePath)
                    }
                    else -> {
                        FileOpenUtility.openFile(this@ChatPersonalActvity, filePath)
                    }
                }
            } else {
                val task: DownloadRequest? =
                    DownloadUtility.downloadList.get(MD5.stringToMD5(downloadUrl))
                if (task == null) {
                    DownloadUtility.downloadFile(
                        applicationContext,
                        DownloadUtility.getPath(
                            applicationContext,
                            DownloadUtility.FILE_PATH_CHAT_FILES
                        ),
                        downloadUrl, downloadFileName,
                        MD5.stringToMD5(downloadUrl),
                        onDownloadListener
                    )
                    chatModel!!.downloadStatus = ChatModel.DownloadStatus.DOWNLOADING
                } else {
                    if (stopDownloading) {
                        task.cancel()
                        DownloadUtility.downloadList.remove(MD5.stringToMD5(downloadUrl))
                        chatModel!!.downloadStatus = ChatModel.DownloadStatus.PENDING
                    }
                }
            }
            adapter.notifyDataSetChanged()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    private fun parseExtras() {
        _roomId = intent.getStringExtra(INTENT_EXTRAS_KEY_ROOM_ID)
        _isGroup = intent.getBooleanExtra(INTENT_EXTRAS_KEY_IS_GROUP, false)
        _groupDetails =
            intent.getSerializableExtra(INTENT_EXTRAS_KEY_GROUP_DETAILS) as FSGroupModel?
        Utills.setImageFullPath(
            this@ChatPersonalActvity,
            binding.chatUserProfile,
            _groupDetails?.about_pic
        )

        var listsize = getIntent().getStringExtra(INTENT_EXTRAS_KEY_PARTICIPENT_SIZE)

        val tmpSenderDetails = intent.getSerializableExtra(INTENT_EXTRAS_KEY_SENDER_DETAILS)
        if (_isGroup) {
            setGroupDetails(listsize)
        }
        if (tmpSenderDetails != null) {
            _senderDetails = tmpSenderDetails as FSUsersModel
            // addChatMenu()
            if (_isGroup) {
                setGroupDetails(listsize)
            } else {
                setIndividualDetails()
            }
            joinCommand()
            blockList
        } else {
            roomInfo
        }
    }

    private val roomInfo: Unit
        get() {
            val messageMap = HashMap<String?, Any?>()
            messageMap["type"] = "roomsDetails"
            messageMap["roomId"] = _roomId
            messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] =
                APIClient.KeyConstant.REQUEST_TYPE_ROOM
            WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        }

    private fun setGroupDetails(listsizeParicipent: String?) {
        binding.chatChatWithUserName.text = _groupDetails?.group_name
        //    binding.chatChatWithUserStatus.text = _groupDetails?.about_group
        binding.chatChatWithUserStatus.text = "$listsizeParicipent Participants"
        //        Glide.with(this).setDefaultRequestOptions(userProfileDefaultOptions).load(groupInfo.getRoomImage()).into(binding.chatUserProfile);
    }


    companion object {
        const val INTENT_EXTRAS_KEY_IS_GROUP = "isGroup"
        const val INTENT_EXTRAS_KEY_GROUP_DETAILS = "groupDetails"
        const val INTENT_EXTRAS_KEY_ROOM_ID = "roomID"
        const val INTENT_EXTRAS_KEY_SENDER_DETAILS = "senderDetails"
        const val INTENT_EXTRAS_KEY_PARTICIPENT_SIZE = "INTENT_EXTRAS_KEY_PARTICIPENT_SIZE"
        private const val TAG = "ChatPersonalActvity"
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
            REQUEST_READ_STORAGE_FOR_UPLOAD_IMAGE -> selectCameraImage()
            REQUEST_READ_STORAGE_FOR_UPLOAD_VIDEO -> showPicVideoDialog()
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

    private fun showPicVideoDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select video from gallery",
            "Record video from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> chooseVideoFromGallery()
                1 -> takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun chooseVideoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takeVideoFromCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun selectCameraImage() {
        //CropImage.activity().start(this, AddPostBottomSheetFragment.this);
        isSetWallpaper = false
        //     CropImage.activity().start(this)

//		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//		galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//		startActivityForResult(galleryIntent, GALLERY);
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
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
                            val dataResponse = fromJson<ChatMediaModel>(nthObject.toString())
                            if (dataResponse.message_type.lowercase() == "image") {
                                listMedia.add(dataResponse.message_content.file_url)
                            }
                        }
                        setRead()

                        ///Move to bottom if user open chat first time
                        binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    } else if (responseCode == 201) {
                        appendMessage(responseData.getJSONObject("data"), true)
                        //                        binding.chatNewMessageCount
                        setRead()
                    } else {
                        // Toast.makeText(this@ChatPersonalActvity, responseData.getString("message"), Toast.LENGTH_SHORT).show()
                    }


//                        if (jsonObject.has("message")) {
//                            ChattingModel chattingModel = ChattingModel.getModel(jsonObject, PreferenceUtils.getLoginUser(ChatActivity.this).getId());
//                            chattingAdapter.updateList(chattingModel);
////                            chatRecyclerView.add
//                            binding.chatRecyclerView.scrollToPosition(chattingAdapter.items.size() - 1);
//                        }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_DETAILS.equalsTo(type)) {
                    if (statusCode == 200) {
                        Log.d(TAG, "received message roomdetail: $response")
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> =
                            Gson().fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                        for (element in roomResponseModelResponseModel.getData().userList) {
                            //UserDetails.instance.chatUsers.put(element.id, element)
                            MyApp.fetchUserDetailChatUsers().put(element.id, element)
                        }
                        //  val roomDetails: FSRoomModel = roomResponseModelResponseModel.getData().roomList.get(0)_senderDetails = roomDetails.senderUserDetail!!
                        /*for (FSRoomModel elemen  t : roomResponseModelResponseModel.getData().getRoomList()) {
                            for (String userId : element.getUserList()) {
                                if (!userId.equals(UserDetails.myDetail.getId())) {
                                    element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                    break;
                                }
                            }
                        }*/
                        joinCommand()
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
                    // if (element.blockedTo == UserDetails.instance.myDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                    if (element.blockedTo == PreferenceKeeper.instance.myUserDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                        blockedByOtherUser()
                    }
                    if (element.blockedTo == PreferenceKeeper.instance.myUserDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                        unBlockedByOtherUser()
                    }
                    if (element.blockedTo == _senderDetails.id && element.blockedBy == PreferenceKeeper.instance.myUserDetail.id && element.isBlock) {
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
                            if (element.blockedTo == PreferenceKeeper.instance.myUserDetail.id && element.blockedBy == _senderDetails.id && element.isBlock) {
                                blockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == PreferenceKeeper.instance.myUserDetail.id && element.blockedBy == _senderDetails.id && !element.isBlock) {
                                unBlockedByOtherUser()
                                break
                            }
                            if (element.blockedTo == _senderDetails.id && element.blockedBy == PreferenceKeeper.instance.myUserDetail.id && element.isBlock) {
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

    private fun blockedByOtherUser() {
        binding.blockedWrapper.visibility = View.VISIBLE
        binding.chatMessageWrapper.visibility = View.GONE
    }

    private fun unBlockedByOtherUser() {
        binding.blockedWrapper.visibility = View.GONE
        binding.chatMessageWrapper.visibility = View.VISIBLE
    }

    private fun setIndividualDetails() {
        binding.chatChatWithUserName.text = _senderDetails.name
        binding.chatChatWithUserStatus.text =
            if (_senderDetails.isOnline) "Online" else _senderDetails.lastSeen


        val userImage = _senderDetails.profile_image.getImageString()
        if (userImage != null) {
            Glide.with(this)
                .setDefaultRequestOptions(MyApp.USER_PROFILE_DEFAULT_GLIDE_CONFIG)
                .load(userImage)
                .into(binding.chatUserProfile)
        }
    }

    private fun setRead() {
        val messageMap = HashMap<String?, Any?>()
        messageMap["type"] = "roomsModify"
        messageMap["roomId"] = _roomId
        messageMap["unread"] = PreferenceKeeper.instance.myUserDetail.id
        messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] = APIClient.KeyConstant.REQUEST_TYPE_ROOM
        WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
    }

    @Throws(JSONException::class)
    private fun appendMessage(chatData: JSONObject, showMessageCount: Boolean) {
        // val senderDetails: FSUsersModel = UserDetails.instance.chatUsers.get(chatData.getString("sender_id"))!!
        val senderDetails: FSUsersModel =
            MyApp.fetchUserDetailChatUsers().get(chatData.getString("sender_id"))!!

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
            mChatListAllMsg = ArrayList<ChatModel>()
            for (key in keys) {
                val chatForThatDay: ArrayList<ChatModel> = chatList[key]!!
                //										HeaderDataImpl headerData1 = new HeaderDataImpl(R.layout.header1_item_recycler, key);
                val headerData1 = HeaderDataImpl(R.layout.header1_item_recycler, key)
                mChatListAllMsg.addAll(chatForThatDay)
                adapter.setHeaderAndData(chatForThatDay as List<ChatModel?>, headerData1)
            }
            Log.d(TAG, "appendMessage: " + mChatListAllMsg)


            if (showMessageCount && chatModel.sender_detail.id != PreferenceKeeper.instance.myUserDetail.id) {
                noOfNewMessages += 1
                binding.chatGoToBottom.visibility = View.VISIBLE
                binding.chatNewMessageCount.text = noOfNewMessages.toString()
                if (noOfNewMessages <= 1) {
                    val layout = binding.chatRecyclerView.layoutManager as LinearLayoutManager?
                    layout!!.scrollToPosition(layout.findLastVisibleItemPosition() + 1)
                }
            }
            if (chatModel.sender_detail.id == PreferenceKeeper.instance.myUserDetail.id) {
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

    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", "allMessage")
            jsonObject.put("room", _roomId)
            jsonObject.put(
                APIClient.KeyConstant.REQUEST_TYPE_KEY,
                APIClient.KeyConstant.REQUEST_TYPE_MESSAGE
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.getInstant()?.sendMessage(jsonObject)
    }

    private val blockList: Unit
        get() {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("type", "allBlockUser")
                jsonObject.put("user", PreferenceKeeper.instance.myUserDetail.id)
                jsonObject.put(
                    APIClient.KeyConstant.REQUEST_TYPE_KEY,
                    APIClient.KeyConstant.REQUEST_TYPE_BLOCK_USER
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            WebSocketSingleton.getInstant()?.sendMessage(jsonObject)
        }


    override fun closeAudioPlayer() {
        binding.audioPlayerFragment.visibility = View.GONE
    }

    override fun uploadFinished(
        fragmentTag: String?,
        data: UploadFileModeNew?,
        messageType: ChatModel.MessageType?,
        date: Date?,
        messageData: HashMap<String, Any>?
    ) {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            if (data != null) {
                if (data.data.file_path != null && data.data.file_path.isNotEmpty()) {
                    //  messageData!![MediaMetaModel.KEY_FILE_THUMB] = APIClient.IMAGE_URL + data.data.file_path
                    messageData!![MediaMetaModel.KEY_FILE_THUMB] = data.data.file_path
                }
                val messageContent = HashMap<String, Any?>()
                val fileUrl: String =  /*APIClient.IMAGE_URL + */data.data.file_path!!
                messageContent["file_url"] = fileUrl
                messageContent["file_meta"] = messageData
                sendMessage("", messageType, messageContent)
            }
        }
    }

    private fun sendMessage(
        message: String,
        messageType: ChatModel.MessageType?,
        messageContent: HashMap<String, Any?>
    ) {
        val messageMap = HashMap<String?, Any?>()
        messageMap["type"] = "addMessage"
        messageMap["roomId"] = _roomId
        messageMap["room"] = _roomId
        messageMap["message"] = message.trim()
        messageMap["message_type"] = messageType.toString()
        //        messageMap.put("sender_id", UserDetails.myDetail.getId());
        messageMap["sender_id"] = PreferenceKeeper.instance.myUserDetail.id
        messageMap["receiver_id"] = "12312faa"
        messageMap["message_content"] = messageContent
        messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] =
            APIClient.KeyConstant.REQUEST_TYPE_MESSAGE
        //        messageMap.put("time", time);

        // TODO: 27/01/21 SendMessage
//        chatReference.add(messageMap);
        WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        try {
            if (adapter.itemCount > 0) {
                Timer().schedule(
                    object : TimerTask() {
                        override fun run() {
                            binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                            // your code here
                        }
                    },
                    500
                )
            }
        } catch (e: Exception) {
        }
    }

    private fun sendMessageLocation(messageContent: HashMap<String, Any?>) {
        val messageMap = HashMap<String?, Any?>()
        messageMap["type"] = "addMessage"
        messageMap["roomId"] = _roomId
        messageMap["room"] = _roomId
        messageMap["message"] = ""
        messageMap["message_type"] = ChatModel.MessageType.location.toString()
        messageMap["sender_id"] = PreferenceKeeper.instance.myUserDetail.id
        messageMap["receiver_id"] = "12312faa"
        messageMap["message_content"] = messageContent
        messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] =
            APIClient.KeyConstant.REQUEST_TYPE_MESSAGE
        //        messageMap.put("time", time);

        // TODO: 27/01/21 SendMessage
//        chatReference.add(messageMap);
        WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        try {
            if (adapter.itemCount > 0) {
                Timer().schedule(
                    object : TimerTask() {
                        override fun run() {
                            binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                            // your code here
                        }
                    },
                    500
                )
            }
        } catch (e: Exception) {
        }
    }


    private fun uploadImageFormUri(resultUri: Uri) {
        val imagePath = resultUri.path
        try {
            val thumbnailFile = File(cacheDir, "image.jpg")
            thumbnailFile.createNewFile()
            thumbnailFile.exists()
            val bitmap: Bitmap?
            bitmap = if (Build.VERSION.SDK_INT <= 29) {
                ThumbnailUtils.createImageThumbnail(
                    imagePath!!,
                    MediaStore.Images.Thumbnails.MINI_KIND
                )
            } else {
                // TODO: 4/17/2020 here we will do code for crete thumnail for latest api version 29 bcoz createVideoThumbnail is depricate for this version
                val signal = CancellationSignal()
                val size = Size(100, 100)
                val file = File(imagePath)
                ThumbnailUtils.createImageThumbnail(
                    file,
                    size, signal
                )
            }
            val bos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(thumbnailFile)
                val file = File(imagePath)
                if (file.exists()) {
                    val fileMeta = HashMap<String, Any>()
                    fileMeta[MediaMetaModel.KEY_FILE_TYPE] =
                        MediaMetaModel.MediaType.imageJPG.toString()
                    addFragment(file, thumbnailFile, ChatModel.MessageType.image, fileMeta)
                    Log.d(TAG, "onActivityResult: $file")
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (fos != null) {
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addFragment(
        file: File,
        thumb: File?,
        messageType: ChatModel.MessageType,
        messageMeta: HashMap<String, Any>
    ) {
        val ft = supportFragmentManager.beginTransaction()
        val uploadFragment = UploadFileProgressFragment()
        ft.add(R.id.uploadFileWrapper, uploadFragment, uploadFragment.fragmentTag)
        ft.commit()
        uploadFragment.uploadFiles(file, thumb, messageType, messageMeta, this)
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    fun saveVideoToInternalStorage(filePath: String?) {
        val newFile: File
        try {
            val currentFile = File(filePath)
            val wallpaperDirectory =
                File(Environment.getExternalStorageDirectory().toString() + VIDEO_DIRECTORY)
            newFile =
                File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".mp4")
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }
            if (currentFile.exists()) {
                val `in`: InputStream = FileInputStream(currentFile)
                val out: OutputStream = FileOutputStream(newFile)

                // Copy the bits from instream to outstream
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                Log.v("vii", "Video file saved successfully.")
            } else {
                Log.v("vii", "Video saving failed. Source file missing.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadVideoFormUri(contentURI: Uri) {
        // val selectedVideoPath = getPath(contentURI)
        // val selectedVideoPath = FileUtils.getPath(THIS!!,contentURI)
        var selectedVideoPath = contentURI.toString()

        Log.d("path", selectedVideoPath!!)
        saveVideoToInternalStorage(selectedVideoPath)
        try {
            val thumbnailFile = File(cacheDir, "image.jpg")
            thumbnailFile.createNewFile()
            //			thumbnailFile.exists();
            Log.d(TAG, "onActivityResult: " + thumbnailFile.exists())
            //Convert bitmap to byte array
            try {
                var bitmap: Bitmap? = null
                bitmap = if (Build.VERSION.SDK_INT <= 29) {
                    ThumbnailUtils.createVideoThumbnail(
                        selectedVideoPath,
                        MediaStore.Images.Thumbnails.MINI_KIND
                    )
                } else {
                    // TODO: 4/17/2020 here we will do code for crete thumbnail for latest api version 29 because createVideoThumbnail is deprecated for this version
                    val signal = CancellationSignal()
                    val size = Size(100, 100)
                    val file = File(selectedVideoPath)
                    ThumbnailUtils.createVideoThumbnail(file, size, signal)
                }
                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
                val bitMapData = bos.toByteArray()

                //write the bytes in file
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(thumbnailFile)
                    val file = File(selectedVideoPath)
                    if (file.exists()) {
                        val fileMeta = HashMap<String, Any>()
                        fileMeta[MediaMetaModel.KEY_FILE_TYPE] =
                            MediaMetaModel.MediaType.videoMP4.toString()
                        addFragment(file, thumbnailFile, ChatModel.MessageType.video, fileMeta)
                        Log.d(TAG, "onActivityResult: $file")
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    if (fos != null) {
                        fos.write(bitMapData)
                        fos.flush()
                        fos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override val activityName: String = ChatPersonalActvity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_MESSAGES,
            ResponseType.RESPONSE_TYPE_USER_MODIFIED,
            ResponseType.RESPONSE_TYPE_USER_BLOCK_MODIFIED,
            ResponseType.RESPONSE_TYPE_USER_ALL_BLOCK,
            ResponseType.RESPONSE_TYPE_ROOM_DETAILS
        )
    }


}