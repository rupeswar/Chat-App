package com.rupeswar.chatapp.ui.main.fragments

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.ui.chat.ChatActivity
import com.rupeswar.chatapp.utils.TimeUtils
import com.rupeswar.chatapp.utils.VolleySingleton
import org.json.JSONObject

class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private val chats = ArrayList<Chat>()

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.chat_name)
        val time: TextView = itemView.findViewById(R.id.chat_time)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        val viewHolder = ChatsViewHolder(view)

        view.setOnClickListener {
            Toast.makeText(parent.context, "Selected ${viewHolder.name.text}", Toast.LENGTH_SHORT).show()
            val jsonObject = JSONObject()
            jsonObject.put("body", "This message is from Chat App Android XD XD")
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "${parent.context.getString(R.string.server_base_url)}/api/test", jsonObject, {
                Toast.makeText(parent.context, "Received Response", Toast.LENGTH_SHORT).show()
                Log.d("API Test Success", it.toString())
            }, {
                Toast.makeText(parent.context, "An Error Occurred...", Toast.LENGTH_SHORT).show()
                Log.d("API Test Failed", it.message.toString())
            })

            VolleySingleton.getInstance(parent.context).addToRequestQueue(jsonObjectRequest)

            val chatIntent = Intent(parent.context, ChatActivity::class.java)
            val chat = chats[viewHolder.adapterPosition]
            chatIntent.putExtra("cid", chat.cid)

            parent.context.startActivity(chatIntent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val currentChat = chats[position]
        holder.name.text = currentChat.name
        holder.lastMessage.text = currentChat.lastMessage.message
        holder.time.text = TimeUtils.getDateOrTime(currentChat.lastMessage.time)

//        TODO("implement read receipts")
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun updateChats(updatedChats: ArrayList<Chat>) {
        chats.clear()
        chats.addAll(updatedChats)

        notifyDataSetChanged()
    }
}