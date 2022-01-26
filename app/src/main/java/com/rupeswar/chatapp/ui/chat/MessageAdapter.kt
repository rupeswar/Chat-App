package com.rupeswar.chatapp.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.models.Message

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val messages = ArrayList<Message>()
    private val OUTGOING_MESSAGE = 1
    private val FOLLOW_UP_MESSAGE = 2

    private fun checkMessageType(messageType: Int, messageParam: Int): Boolean {
        return messageType and messageParam == messageParam
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: LinearLayout = itemView.findViewById(R.id.message_container)
        val messageBox: LinearLayout = itemView.findViewById(R.id.message_box)
        val image: ImageView = itemView.findViewById(R.id.image)
        val text: TextView = itemView.findViewById(R.id.text)
        val tail: ImageView = itemView.findViewById(R.id.tail)
        var mid: String? = null
    }

    override fun getItemViewType(position: Int): Int {
        var messageType = 0
        if (position > 0 && messages[position].sender == messages[position - 1].sender)
            messageType = messageType xor FOLLOW_UP_MESSAGE
        if (messages[position].sender == "You")
            messageType = messageType xor OUTGOING_MESSAGE
        return messageType
    }

    override fun onCreateViewHolder(parent: ViewGroup, messageType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (checkMessageType(messageType, OUTGOING_MESSAGE)) R.layout.item_outgoing_message else R.layout.item_incoming_message,
            parent,
            false
        )
        val viewHolder = MessageViewHolder(view)

        if (checkMessageType(messageType, FOLLOW_UP_MESSAGE)) {
            viewHolder.tail.visibility = View.INVISIBLE
            viewHolder.messageBox.background = AppCompatResources.getDrawable(
                parent.context,
                if (checkMessageType(messageType, OUTGOING_MESSAGE))
                    R.drawable.outgoing_follow_up_message_background
                else
                    R.drawable.incoming_follow_up_message_background
            )
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.text.text = message.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateMessages(updatedMessages: ArrayList<Message>) {
        messages.clear()
        messages.addAll(updatedMessages)

        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        messages.add(message)

        notifyItemInserted(messages.size-1)
    }
}