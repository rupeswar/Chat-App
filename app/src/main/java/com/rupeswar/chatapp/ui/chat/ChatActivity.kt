package com.rupeswar.chatapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.databinding.ActivityChatBinding
import com.rupeswar.chatapp.models.Message

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cid = intent.getStringExtra("cid")
        val messageList = ArrayList<Message>()

        val yourMessage = Message("0", "You", "Hello!", System.currentTimeMillis())
        val hisMessage = Message("0", "He", "Hello!", System.currentTimeMillis())
        messageList.add(yourMessage)
        messageList.add(hisMessage)
        messageList.add(yourMessage)
        messageList.add(hisMessage)
        messageList.add(yourMessage)
        messageList.add(hisMessage)
        messageList.add(yourMessage)
        messageList.add(hisMessage)
        messageList.add(yourMessage)
        messageList.add(hisMessage)

        val messages = binding.messages

        val adapter = MessageAdapter()
        messages.adapter = adapter
        messages.layoutManager = LinearLayoutManager(this)

        adapter.updateMessages(messageList)
    }
}