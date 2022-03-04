package com.nightout.chat.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupChatImageAdapter
import com.nightout.base.BaseActivity
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.databinding.GroupinfoActvityBinding
import com.nightout.model.GroupChatImgModel
import com.nightout.utils.AppConstant
import com.nightout.utils.PreferenceKeeper
import org.json.JSONObject

class GroupInfoActvity : BaseActivity(), WebSocketObserver {
    lateinit var groupChatImageAdapter : GroupChatImageAdapter
    lateinit var binding : GroupinfoActvityBinding
    var roomID=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.groupinfo_actvity)
        binding = DataBindingUtil.setContentView(this@GroupInfoActvity,R.layout.groupinfo_actvity)
        setToolBar()
        setTouchNClick(binding.grupInfoToolBar.toolbarCreateGrop)
        setHorizonatlDummyList()
        setFrendListDummy()
        roomID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.ROOM_ID).toString()
        WebSocketSingleton.getInstant()!!.register(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v== binding.grupInfoToolBar.toolbarCreateGrop){
            val jsonObject = JSONObject()
            jsonObject.put("type", "removeUser")
            jsonObject.put("userId", PreferenceKeeper.instance.myUserDetail.id)
            jsonObject.put("roomId", roomID)
            jsonObject.put("request", "room")
            jsonObject.put("room_type","group")

            WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
        }
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

    private fun setFrendListDummy() {
       /* var list = ArrayList<GroupListModel>()
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))

        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        groupListAdapter = GroupListAdapter(this@GroupInfoActvity, list,false, object : GroupListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {

                }

            })

        binding.groupInfoActvitityRecyleFrend.also {
            it.layoutManager =
                LinearLayoutManager(this@GroupInfoActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }*/
    }


    private fun setHorizonatlDummyList() {
        var list = ArrayList<GroupChatImgModel>()
        list.add(GroupChatImgModel(R.drawable.grupimg1))
        list.add(GroupChatImgModel(R.drawable.grupimg2))
        list.add(GroupChatImgModel(R.drawable.grupimg3))
        list.add(GroupChatImgModel(R.drawable.grupimg4))
       groupChatImageAdapter =  GroupChatImageAdapter(this@GroupInfoActvity,list,object :GroupChatImageAdapter.ClickListener{
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
                if (ResponseType.RESPONSE_TYPE_REMOVE_USER.equalsTo(type)) {
                    Log.d("ok", "exit sucess ")
                }else{
                    Log.d("ok", "exit else ")
                }
            }
        }
      catch (e:Exception){

      }

    }

    override val activityName: String = GroupInfoActvity::class.java.name


    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_REMOVE_USER

        )

    }
}