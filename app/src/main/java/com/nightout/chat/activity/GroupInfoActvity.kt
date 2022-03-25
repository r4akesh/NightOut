package com.nightout.chat.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.gson.reflect.TypeToken
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nightout.R
import com.nightout.adapter.GroupChatImageAdapter

import com.nightout.base.BaseActivity
import com.nightout.chat.adapter.GroupListInfoAdapter
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.model.FSRoomModel
import com.nightout.chat.model.ResponseModel
import com.nightout.chat.model.RoomResponseModel
import com.nightout.databinding.GroupinfoActvityBinding
import com.nightout.model.FSUsersModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.APIClient
import org.json.JSONObject
import java.util.HashMap

class GroupInfoActvity : BaseActivity(), WebSocketObserver {
    lateinit var groupChatImageAdapter : GroupChatImageAdapter
    lateinit var binding : GroupinfoActvityBinding
    var roomID=""
        var isExitGroupClick= false
    var imageGrpUrl=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.groupinfo_actvity)
        binding = DataBindingUtil.setContentView(this@GroupInfoActvity,R.layout.groupinfo_actvity)
        setToolBar()
        setTouchNClick(binding.grupInfoToolBar.toolbarCreateGrop)
        setTouchNClick(binding.groupInfoEditImg)


        roomID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.ROOM_ID).toString()
        WebSocketSingleton.getInstant()!!.register(this)
        roomInfo//get detail
        var  allChatMsg = intent.getStringArrayListExtra(AppConstant.INTENT_EXTRAS.ALLCHAT_MSG) as ArrayList<String>
        Log.d("TAG", "onCreate: "+allChatMsg)
        if(allChatMsg.size>0){
            setHorizonatlList(allChatMsg)
            binding.groupInfoActvitityView.visibility= VISIBLE
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v== binding.grupInfoToolBar.toolbarCreateGrop){
            //exit group
            val jsonObject = JSONObject()
            jsonObject.put("type", "removeUser")
            jsonObject.put("userId", PreferenceKeeper.instance.myUserDetail.id)
            jsonObject.put("roomId", roomID)
            jsonObject.put("request", "room")
            jsonObject.put("room_type","group")
            isExitGroupClick=true
            WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
        }
        else if(v==binding.groupInfoEditImg){
            registerFroResultGrupEdit.launch(
                Intent(this@GroupInfoActvity,CreateGroupActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_EDITGROUP,true)
                    .putExtra(AppConstant.INTENT_EXTRAS.GROUP_NAME,binding.groupInfoActvitityName.text.toString())
                    .putExtra(AppConstant.INTENT_EXTRAS.GROUP_IMG_URL,imageGrpUrl)
                    .putExtra(AppConstant.INTENT_EXTRAS.GROUP_ROOMID,roomID)
            )

        }
    }

    var registerFroResultGrupEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==Activity.RESULT_OK){
           binding.groupInfoActvitityName.text= it.data?.getStringExtra(AppConstant.INTENT_EXTRAS.GROUP_NAME)
            imageGrpUrl= it.data?.getStringExtra(AppConstant.INTENT_EXTRAS.GROUP_IMG_URL).toString()
            Utills.setImageFullPath(this@GroupInfoActvity,binding.groupInfoActvitityImage,imageGrpUrl)
        }
    }

    override fun onBackPressed() {
        var myIntent= Intent()
        myIntent.putExtra(AppConstant.INTENT_EXTRAS.GROUP_NAME,binding.groupInfoActvitityName.text.toString())
        myIntent.putExtra(AppConstant.INTENT_EXTRAS.GROUP_IMG_URL,imageGrpUrl)
        myIntent.putExtra(AppConstant.INTENT_EXTRAS.GROUP_IMG_URL,imageGrpUrl)
       // myIntent.putExtra(AppConstant.INTENT_EXTRAS.IS_GROUP_MODIFIED,true)
        setResult(Activity.RESULT_OK,myIntent);
        super.onBackPressed()
    }

    private val roomInfo: Unit
        get() {
            val messageMap = HashMap<String?, Any?>()
            messageMap["type"] = "roomsDetails"
            messageMap["roomId"] = roomID
            messageMap[APIClient.KeyConstant.REQUEST_TYPE_KEY] = APIClient.KeyConstant.REQUEST_TYPE_ROOM
            WebSocketSingleton.getInstant()?.sendMessage(JSONObject(messageMap))
        }

    private fun setToolBar() {
         binding.grupInfoToolBar.toolbarTitle.setText("Group Information")
         binding.grupInfoToolBar.toolbar3dot.visibility=GONE
         binding.grupInfoToolBar.toolbarBell.visibility=GONE
         binding.grupInfoToolBar.toolbarCreateGrop.visibility= VISIBLE
         binding.grupInfoToolBar.toolbarCreateGrop.setText("Exit Group")
        setTouchNClick(binding.grupInfoToolBar.toolbarBack)
         binding.grupInfoToolBar.toolbarBack.setOnClickListener { finish() }
    }

    private fun setFrendListDummy(rsData: ResponseModel<RoomResponseModel>) {
        var selectedUserList = ArrayList<FSUsersModel>()
        for(i in 0 until rsData.getData().roomList[0].userList.size){
            var userID= rsData.getData().roomList[0].userList[i]
            for(j in 0 until rsData.getData().userList.size){
                if(userID == rsData.getData().userList[j].id){
                    selectedUserList.add( rsData.getData().userList[j])
                }
            }
        }
        binding.groupInfoActvitityTotParticipants.text = ""+selectedUserList.size+" Participants"
       var  groupListAdapter = GroupListInfoAdapter(this@GroupInfoActvity, selectedUserList,false, object : GroupListInfoAdapter.ClickListener {
                override fun onClickChk(pos: Int) {

                }

            })

        binding.groupInfoActvitityRecyleFrend.also {
            it.layoutManager =
                LinearLayoutManager(this@GroupInfoActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }
    }


    private fun setHorizonatlList(allChatMsgList: ArrayList<String>) {
       groupChatImageAdapter =  GroupChatImageAdapter(this@GroupInfoActvity,allChatMsgList,object :GroupChatImageAdapter.ClickListener{
           override fun onClick(pos: Int) {

           }

       } )

        binding.groupInfoActvitityRcyleHorzntl.also {
            it.layoutManager = LinearLayoutManager(this@GroupInfoActvity,LinearLayoutManager.HORIZONTAL,false)
            it.adapter = groupChatImageAdapter
        }
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            runOnUiThread {
                Log.d("ok", "received message GroupInfo: $response")
                if (ResponseType.RESPONSE_TYPE_ROOM_MODIFIED.equalsTo(type)) {
                    if(isExitGroupClick) {
                        MyApp.setStatus(AppConstant.INTENT_EXTRAS.IsUserExitClickBtn,true)
                        var myIntnt = Intent()
                        myIntnt.putExtra(AppConstant.INTENT_EXTRAS.IS_GROUP_EXIT,true)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
                else if (ResponseType.RESPONSE_TYPE_ROOM_DETAILS.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> =
                            Gson().fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                        for (element in roomResponseModelResponseModel.getData().userList) {
                            //UserDetails.instance.chatUsers.put(element.id, element)
                            MyApp.fetchUserDetailChatUsers().put(element.id, element)
                        }
                        setFrendListDummy(roomResponseModelResponseModel)
                        setData(roomResponseModelResponseModel.getData().roomList[0])

                        //  val roomDetails: FSRoomModel = roomResponseModelResponseModel.getData().roomList.get(0)_senderDetails = roomDetails.senderUserDetail!!
                        /*for (FSRoomModel elemen  t : roomResponseModelResponseModel.getData().getRoomList()) {
                            for (String userId : element.getUserList()) {
                                if (!userId.equals(UserDetails.myDetail.getId())) {
                                    element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                    break;
                                }
                            }
                        }*/
                       // joinCommand()

                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }


                else{
                    Log.d("ok", "exit else ")
                }
            }
        }
      catch (e:Exception){

      }

    }

    private fun setData(gData: FSRoomModel) {
        try {

            binding.groupInfoActvitityName.text = gData?.groupDetails?.group_name
            try {
                binding.groupInfoActvitityDate.text = "Created "+MyApp.dateZoneToDateFormat(gData?.create_time)
            } catch (e: Exception) {
            }
            Utills.setImageFullPath(this@GroupInfoActvity,binding.groupInfoActvitityImage,gData?.groupDetails?.about_pic)
              imageGrpUrl= gData?.groupDetails?.about_pic.toString()
            Log.d("received message", "setData image: "+gData?.groupDetails?.about_pic)
        } catch (e: Exception) {
            Log.d("TAG", "setData: "+e)
        }


    }

    override val activityName: String = GroupInfoActvity::class.java.name


    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_ROOM_MODIFIED ,
            ResponseType.RESPONSE_TYPE_ROOM_DETAILS
        )

    }
}