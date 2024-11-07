package com.gereso.terramasterhub

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messages: List<Message>, private val currentUserId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CURRENT_USER_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_USER_MESSAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CURRENT_USER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewliststyle, parent, false)
                CurrentUserMessageViewHolder(view)
            }
            VIEW_TYPE_OTHER_USER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewliststyle1, parent, false)
                OtherUserMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sortedMessages = messages.sortedBy { it.timestamp }
        val message = sortedMessages[position]

        when (holder.itemViewType) {
            VIEW_TYPE_CURRENT_USER_MESSAGE -> {
                (holder as CurrentUserMessageViewHolder).bindCurrentUser(message)
            }
            VIEW_TYPE_OTHER_USER_MESSAGE -> {
                (holder as OtherUserMessageViewHolder).bindOtherUser(message)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == currentUserId) {
            VIEW_TYPE_CURRENT_USER_MESSAGE
        } else {
            VIEW_TYPE_OTHER_USER_MESSAGE
        }
    }

    class CurrentUserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val textMessageTextView: TextView = itemView.findViewById(R.id.textMessage)
        private val timeStampTextView: TextView = itemView.findViewById(R.id.timeStamp)

        fun bindCurrentUser(message: Message) {
            userNameTextView.text = message.senderName
            textMessageTextView.text = message.message
            timeStampTextView.text = android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.timestamp)
        }
    }

    class OtherUserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val textMessageTextView: TextView = itemView.findViewById(R.id.textMessage)
        private val timeStampTextView: TextView = itemView.findViewById(R.id.timeStamp)

        fun bindOtherUser(message: Message) {
            userNameTextView.text = message.senderName
            textMessageTextView.text = message.message
            timeStampTextView.text = android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.timestamp)
        }
    }
}
