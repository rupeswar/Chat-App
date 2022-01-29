package com.rupeswar.chatapp.ui.main.fragments

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.ui.chat.ChatActivity
import com.rupeswar.chatapp.utils.TimeUtils

class ChatsAdapter : ListAdapter<Chat, ChatsAdapter.ChatsViewHolder>(ChatsComparator()) {

//    private val chats = ArrayList<Chat>()

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.chat_name)
        val time: TextView = itemView.findViewById(R.id.chat_time)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
    }

    class ChatsComparator : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.cid == newItem.cid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        val viewHolder = ChatsViewHolder(view)

        view.setOnClickListener {
            Toast.makeText(parent.context, "Selected ${viewHolder.name.text}", Toast.LENGTH_SHORT).show()

            val chatIntent = Intent(parent.context, ChatActivity::class.java)
            val chat = getItem(viewHolder.adapterPosition)
            chatIntent.putExtra("cid", chat.cid)

            parent.context.startActivity(chatIntent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val currentChat = getItem(position)
        Log.d("Bind Chat", currentChat.toString())
        holder.name.text = currentChat.name
        holder.lastMessage.text = currentChat.lastMessage.message
        holder.time.text = TimeUtils.getDateOrTime(currentChat.lastMessage.time)

//        TODO("implement read receipts")
    }
}