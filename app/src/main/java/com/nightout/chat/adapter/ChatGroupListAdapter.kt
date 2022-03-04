package com.nightout.chat.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.chat.model.FSRoomModel
import com.nightout.chat.utility.TimeShow.timeFormatYesterdayToDay
import com.nightout.chat.utility.UserDetails
import com.nightout.databinding.ChatItemBinding

import com.nightout.model.FSUsersModel
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import java.util.ArrayList


class ChatGroupListAdapter(
    var context: Context,
    var clickListener: ClickListener, ) :
    RecyclerView.Adapter<ChatGroupListAdapter.ViewHolder>() {
    private val arrayList: ArrayList<FSRoomModel> = ArrayList<FSRoomModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ChatItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.chat_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: FSRoomModel = arrayList[position]
        if (item.isGroup) {
            viewHolder.binding.chatItemTitle.text = item.groupDetails!!.group_name
            viewHolder.binding.chatItemSubTitle.text = item.lastMessage
            viewHolder.binding.chatItemTime.text = timeFormatYesterdayToDay(item.lastMessageTime, "yyyy-MM-dd'T'HH:mm:ss.SSS")
         //   viewHolder.binding.chatItemCount.text = timeFormatYesterdayToDay(item.lastMessageTime, "yyyy-MM-dd'T'HH:mm:ss.SSS")

            Utills.setImageFullPath(context,viewHolder.binding.chatItemProfile,item.groupDetails!!.about_pic)
            if (item.unread != null && item.unread!![PreferenceKeeper.instance.myUserDetail.id] != null) {
                val unreadCount: Int = item.unread!![PreferenceKeeper.instance.myUserDetail.id]!!
                if (unreadCount > 0) {
                    viewHolder.binding.chatItemCount.visibility = View.VISIBLE
                } else {
                    viewHolder.binding.chatItemCount.visibility = View.INVISIBLE
                }
                if (unreadCount > 99) {
                    viewHolder.binding.chatItemCount.text = "99+"
                } else {
                    viewHolder.binding.chatItemCount.text = unreadCount.toString()
                }
            } else {
                viewHolder.binding.chatItemCount.visibility = View.INVISIBLE
            }
        }



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(item)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: ChatItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ChatItemBinding = itemView

    }
    fun addAll(list: java.util.ArrayList<FSRoomModel>?) {
        this.arrayList.clear()
        this.arrayList.addAll(list!!)
        notifyDataSetChanged()
    }

    fun add(item: FSRoomModel) {
        arrayList.add(item)
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: FSRoomModel) {
        var isAlreadyAdded = false
        for (element in arrayList) {
            if (element.roomId == item.roomId) {
                isAlreadyAdded = true
                break
            }
        }
        if (isAlreadyAdded) {
            updateElement(item)
        } else {
            arrayList.add(item)
            notifyDataSetChanged()
        }
    }
    fun updateUserElement(element: FSUsersModel) {
        for (i in arrayList.indices) {
            if (arrayList[i].senderUserDetail!!.id == element.id) {
                arrayList[i].senderUserDetail = element
                notifyDataSetChanged()
            }
        }
    }

    fun updateElement(element: FSRoomModel) {
        for (i in this.arrayList.indices) {
            if (arrayList[i].roomId == element.roomId) {
//                this.list.set(i, element);
                arrayList.removeAt(i)
                arrayList.add(0, element)
                notifyDataSetChanged()
                break
            }
        }
    }

    interface ClickListener {
        fun onClick(item: FSRoomModel)


    }


}

