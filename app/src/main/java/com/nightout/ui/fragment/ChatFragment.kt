package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.ChatAdapter
import com.nightout.databinding.FragmentChatBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.ChatModel
import com.nightout.ui.activity.ChatPersonalActvity
import com.nightout.ui.activity.CreateGroupActvity

class ChatFragment() : Fragment() , View.OnClickListener {

    lateinit var binding : FragmentChatBinding
    private var onMenuOpenListener: OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        setDuumyList()
        initView()
        return binding.root
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

    lateinit var chatAdapter: ChatAdapter
    private fun setDuumyList() {
        var list = ArrayList<ChatModel>()
        list.add(ChatModel("Vanity Night Club Group","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_1,"7"))
        list.add(ChatModel("Feel The Beat","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_2,"7"))
        list.add(ChatModel("Night Beats","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_3,"14"))

        list.add(ChatModel("Vanity Night Club Group","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_1,"14"))
        list.add(ChatModel("Feel The Beat","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_2,"3"))
        list.add(ChatModel("Night Beats","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_3,"3"))

        list.add(ChatModel("Vanity Night Club Group","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_1,"5"))
        list.add(ChatModel("Feel The Beat","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_2,"4"))
        list.add(ChatModel("Night Beats","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_3,"3"))

        list.add(ChatModel("Vanity Night Club Group","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_1,"5"))
        list.add(ChatModel("Feel The Beat","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_2,"4"))
        list.add(ChatModel("Night Beats","You , Jane Cooper, Eleanor Pana....",R.drawable.chat_ic_3,"3"))

        chatAdapter = ChatAdapter(requireContext(),list,object:ChatAdapter.ClickListener{
            override fun onClick(pos: Int) {
              startActivity(Intent(requireActivity(), ChatPersonalActvity::class.java))
            }

        })
        binding.fragmentChatRecycler.also {
            it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            it.adapter = chatAdapter
        }
    }

}