package com.nightout.chat.adapter


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.chat.model.ChatModel
import com.nightout.chat.model.MediaModel
import com.nightout.chat.stickyheader.stickyView.StickHeaderRecyclerView
import com.nightout.databinding.*
 /*import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.request.DownloadRequest
import org.apache.commons.io.FilenameUtils*/
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class ChatAdapter(private val context: Context, private val chatCallbacks: ChatCallbacks) :
    StickHeaderRecyclerView<ChatModel?, HeaderDataImpl?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ROW_TYPE_RIGHT_TEXT, ROW_TYPE_RIGHT_REPlAY -> {
                val binding: RowRightChatTextBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_right_chat_text, parent, false
                )
                RightTextViewHolder(binding)
            }
            ROW_TYPE_RIGHT_IMAGE -> {
                val binding: RowRightChatImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_right_chat_image, parent, false
                )
                RightImageViewHolder(binding)
            }
            ROW_TYPE_RIGHT_VIDEO -> {
                val binding: RowRightChatVideoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_right_chat_video, parent, false
                )
                RightVideoViewHolder(binding)
            }
            ROW_TYPE_RIGHT_DOCUMENT -> {
                val binding: RowRightChatDocBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_right_chat_doc, parent, false
                )
                RightDocViewHolder(binding)
            }
            ROW_TYPE_RIGHT_LOCATION -> {
                val binding: RowRightChatLocationBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_right_chat_location, parent, false
                )
                RightLocationViewHolder(binding)
            }
//            ROW_TYPE_RIGHT_CONTACT -> {
//                val binding: RowRightChatContactBinding = DataBindingUtil.inflate(
//                    LayoutInflater.from(parent.context),
//                    R.layout.row_right_chat_contact, parent, false
//                )
//                RightContactViewHolder(binding)
//            }
            ROW_TYPE_LEFT_TEXT, ROW_TYPE_LEFT_REPlAY -> {
                val binding: RowLeftChatTextBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_left_chat_text, parent, false
                )
                LeftTextViewHolder(binding)
            }
            ROW_TYPE_LEFT_IMAGE -> {
                val binding: RowLeftChatImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_left_chat_image, parent, false
                )
                LeftImageViewHolder(binding)
            }
            ROW_TYPE_LEFT_VIDEO -> {
                val binding: RowLeftChatVideoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_left_chat_video, parent, false
                )
                LeftVideoViewHolder(binding)
            }
            ROW_TYPE_LEFT_DOCUMENT -> {
                val binding: RowLeftChatDocBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_left_chat_doc, parent, false
                )
                LeftDocViewHolder(binding)
            }
            ROW_TYPE_LEFT_LOCATION -> {
                val binding: RowLeftChatLocationBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_left_chat_location, parent, false
                )
                LeftLocationViewHolder(binding)
            }
//            ROW_TYPE_LEFT_CONTACT -> {
//                val binding: RowLeftChatContactBinding = DataBindingUtil.inflate(
//                    LayoutInflater.from(parent.context),
//                    R.layout.row_left_chat_contact, parent, false
//                )
//                LeftContactViewHolder(binding)
//            }
            else -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.header1_item_recycler, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val tv = header.findViewById<TextView>(R.id.tvHeader)
        val `object`: HeaderDataImpl = getHeaderDataInPosition(headerPosition)!!
        tv.text = `object`.title
    }

    interface ChatCallbacks {
        fun onClickDownload(
            chatModel: ChatModel?,
            messageContent: MediaModel?,
            stopDownloading: Boolean,
            onDownloadListener: OnDownloadListener?
        )

        fun onClickContact(chatModel: ChatModel?, contactModel: ContactModel?)
        fun onClickLocation(chatModel: ChatModel?, contactModel: LocationModel?)
        fun onLongClick(chatModel: ChatModel?)
    }

    internal interface HolderCallback {
        val chatDownloadPercentage: TextView?
        val chatDownloadStatus: ImageView?
    }

    internal class BroadcastProgress(var appRowAppDownloadStatusText: TextView) :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val progress = intent.getIntExtra(BroadCastConstants.INTENT_PROGRESS_COUNT, 0)
            appRowAppDownloadStatusText.text = "$progress%"
        }
    }

    internal inner class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var tvHeader: TextView
        override fun bindData(position: Int) {
            val `object`: HeaderDataImpl = getHeaderDataInPosition(position)!!
            tvHeader.text = `object`.title
        }

        init {
            tvHeader = itemView.findViewById(R.id.tvHeader)
        }
    }

    internal inner class RightTextViewHolder(var binding: RowRightChatTextBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            binding.chatRightTextMessage.text = `object`.message
           // binding.chatRightTextUserName.text = `object`.sender_detail.name
            binding.chatRightTextUserName.visibility=GONE
            binding.chatRightTextTime.text = `object`.message_on
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class RightImageViewHolder(var binding: RowRightChatImageBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = `object`.message_content as MediaModel
            Glide.with(context).load(messageContent.file_url).into(binding.chatRightImageImage)
            binding.chatRightImageTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    `object`,
                    messageContent,
                    `object`.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    null
                )
            }
        }
    }

    internal inner class RightVideoViewHolder(var binding: RowRightChatVideoBinding) :
        MediaViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = `object`.message_content as MediaModel
            val onDownloadListener = super.getDownloadListener(position, `object`, messageContent)
            Log.d(TAG, "bindData: " + messageContent.file_meta.thumbnail)
            Glide.with(context).load(messageContent.file_meta.thumbnail)
                .into(binding.chatRightVideoImage)
            binding.chatRightVideoTime.text = `object`.message_on
            binding.chatRightVideoTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    `object`,
                    messageContent,
                    `object`.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    onDownloadListener
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }

        override val chatDownloadPercentage: TextView?
            get() = binding.chatRightDownloadPercentage
        override val chatDownloadStatus: ImageView?
            get() = binding.chatRightDownloadStatus


        private val TAG = "RightVideoViewHolder"

    }

    internal inner class RightDocViewHolder(var binding: RowRightChatDocBinding) :
        MediaViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = `object`.message_content as MediaModel
            val onDownloadListener = super.getDownloadListener(position, `object`, messageContent)
            Log.d(TAG, "bindData: " + messageContent.file_meta.thumbnail)
            binding.chatRightDocFileName.text = messageContent.file_url
            binding.chatRightDocTime.text = `object`.message_on
            try {
                val url = URL(messageContent.file_url)
                binding.chatRightDocFileName.text = FilenameUtils.getName(url.path)
                binding.chatRightDocType.text = FilenameUtils.getExtension(url.path)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    `object`,
                    messageContent,
                    `object`.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    onDownloadListener
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }

        override val chatDownloadPercentage: TextView
            get() = binding.chatLeftDocDownloadPercentage
        override val chatDownloadStatus: ImageView
            get() = binding.chatLeftDocDownloadStatus


        private val TAG = "RightDocViewHolder"

    }

    internal inner class RightLocationViewHolder(var binding: RowRightChatLocationBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: LocationModel = `object`.message_content as LocationModel
            binding.chatRightLocationName.text = messageContent.name
            binding.chatRightLocationAddress.text = messageContent.address
            binding.chatRightLocationTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickLocation(
                    `object`,
                    messageContent
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class RightContactViewHolder(var binding: RowRightChatContactBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: ContactModel = `object`.message_content as ContactModel
            val fullName: String = messageContent.first_name + " " + messageContent.last_name
            binding.chatRightContactName.text = fullName
            binding.chatRightContactNumber.text = messageContent.mobile
            binding.chatRightContactTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickContact(
                    `object`,
                    messageContent
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class LeftLocationViewHolder(var binding: RowLeftChatLocationBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: LocationModel = `object`.message_content as LocationModel
            binding.chatLeftLocationName.text = messageContent.name
            binding.chatLeftLocationAddress.text = messageContent.address
            binding.chatLeftLocationTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickLocation(
                    `object`,
                    messageContent
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class LeftContactViewHolder(var binding: RowLeftChatContactBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: ContactModel = `object`.message_content as ContactModel
            val fullName: String = messageContent.first_name + " " + messageContent.last_name
            binding.chatLeftContactName.text = fullName
            binding.chatLeftContactNumber.text = messageContent.mobile
            binding.chatLeftContactTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickContact(
                    `object`,
                    messageContent
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class LeftTextViewHolder(var binding: RowLeftChatTextBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            binding.chatLeftTextMessage.text = `object`.message
            binding.chatLeftTextUserName.text = `object`.sender_detail.name
            binding.chatLeftTextTime.text = `object`.message_on
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }
    }

    internal inner class LeftImageViewHolder(var binding: RowLeftChatImageBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val chatModel: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = chatModel.message_content as MediaModel
            Glide.with(context).load(messageContent.file_url).into(binding.chatLeftImageImage)
            binding.chatLeftImageTime.text = chatModel.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    chatModel,
                    messageContent,
                    chatModel.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    null
                )
            }
        }
    }

    internal abstract inner class MediaViewHolder(itemView: View) : BaseViewHolder(itemView),
        HolderCallback {
        fun getDownloadListener(
            position: Int,
            chatModel: ChatModel,
            messageContent: MediaModel
        ): OnDownloadListener {
            Log.d(TAG, "bindData: " + messageContent.file_meta.thumbnail)
            if (chatModel.downloadStatus !== ChatModel.DownloadStatus.DOWNLOADED) {
                val task: DownloadRequest? =
                    DownloadUtility.downloadList[stringToMD5(messageContent.file_url)]
                if (task != null) {
                    chatModel.downloadStatus = ChatModel.DownloadStatus.DOWNLOADING
                } else {
                    chatModel.downloadStatus = ChatModel.DownloadStatus.PENDING
                }
            }
            when {
                chatModel.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING -> {
                    chatDownloadStatus!!.setImageResource(R.drawable.ic_chat_loading)
                    val rotateAnimation = RotateAnimation(
                        0f, 359f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    rotateAnimation.duration = 1000
                    rotateAnimation.repeatCount = Animation.INFINITE
                    chatDownloadStatus!!.startAnimation(rotateAnimation)
                }
                chatModel.downloadStatus === ChatModel.DownloadStatus.PENDING -> {
                    chatDownloadStatus!!.setImageResource(R.drawable.ic_chat_download)
                    chatDownloadStatus!!.clearAnimation()
                    chatDownloadPercentage!!.text = "Download"
                }
                chatModel.downloadStatus === ChatModel.DownloadStatus.DOWNLOADED -> {
                    chatDownloadStatus!!.setImageResource(R.drawable.ic_chat_open_file)
                    chatDownloadStatus!!.clearAnimation()
                    chatDownloadPercentage!!.text = "Open"
                }
            }
            context.registerReceiver(
                BroadcastProgress(chatDownloadPercentage!!),
                IntentFilter(BroadCastConstants.INTENT_REFRESH + stringToMD5(messageContent.file_url))
            )
            return object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadDir = context.getExternalFilesDir(null)
                    val fileName: String = messageContent.file_url
                    val downloadFile = File(downloadDir!!.absolutePath + "/" + fileName)
                    DownloadUtility.downloadList.remove(stringToMD5(messageContent.file_url))
                    chatModel.downloadStatus = ChatModel.DownloadStatus.DOWNLOADED
                    notifyDataSetChanged()
                    Log.d(TAG, "onDownloadComplete: ")
                }

                override fun onError(error: Error) {
                    DownloadUtility.downloadList.remove(stringToMD5(messageContent.file_url))
                    //					items.get(getAdapterPosition()).setDownloadStatus(AppListModel.Level.PENDING);
                    chatModel.downloadStatus = ChatModel.DownloadStatus.PENDING
                    notifyDataSetChanged()
                    Log.e(TAG, "onError: ")
                    println(error)
                }
            }
        }


        private val TAG = "MediaViewHolder"

    }

    internal inner class LeftVideoViewHolder(var binding: RowLeftChatVideoBinding) :
        MediaViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = `object`.message_content as MediaModel
            val onDownloadListener = super.getDownloadListener(position, `object`, messageContent)
            Log.d(TAG, "bindData: " + messageContent.file_meta.thumbnail)
            Glide.with(context).load(messageContent.file_meta!!.thumbnail)
                .into(binding.chatLeftViewImage)
            binding.chatLeftViewTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    `object`,
                    messageContent,
                    `object`.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    onDownloadListener
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }

        override val chatDownloadPercentage: TextView
            get() = binding.chatLeftDownloadPercentage
        override val chatDownloadStatus: ImageView
            get() = binding.chatLeftDownloadStatus


        private val TAG = "LeftVideoViewHolder"

    }

    internal inner class LeftDocViewHolder(var binding: RowLeftChatDocBinding) :
        MediaViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val `object`: ChatModel = getDataInPosition(position)!!
            val messageContent: MediaModel = `object`.message_content as MediaModel
            val onDownloadListener = super.getDownloadListener(position, `object`, messageContent)
            Log.d(TAG, "bindData: " + messageContent.file_meta!!.thumbnail)
            binding.chatLeftDocTime.text = `object`.message_on
            binding.root.setOnClickListener {
                chatCallbacks.onClickDownload(
                    `object`,
                    messageContent,
                    `object`.downloadStatus === ChatModel.DownloadStatus.DOWNLOADING,
                    onDownloadListener
                )
            }
            binding.root.setOnLongClickListener {
                chatCallbacks.onLongClick(`object`)
                true
            }
        }

        override val chatDownloadPercentage: TextView
            get() = binding.chatLeftDocDownloadPercentage
        override val chatDownloadStatus: ImageView
            get() = binding.chatLeftDocDownloadStatus


        private val TAG = "LeftDocViewHolder"

    }
}