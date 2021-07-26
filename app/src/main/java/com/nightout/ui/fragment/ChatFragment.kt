package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.ChatAdapter
import com.nightout.databinding.FragmentChatBinding
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.model.ChatModel
import com.nightout.ui.activity.ChatPersonalActvity

class ChatFragment : Fragment() {

    lateinit var binding : FragmentChatBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        setDuumyList()
        return binding.root
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