package com.rupeswar.chatapp.ui.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.application.ChatApplication
import com.rupeswar.chatapp.databinding.ActivityChatBinding
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.utils.AuthUtil
import com.rupeswar.chatapp.utils.SocketSingleton
import com.rupeswar.chatapp.viewmodels.ChatsViewModel
import com.rupeswar.chatapp.viewmodels.ChatsViewModelFactory
import io.socket.client.Ack
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    companion object {
        private val socket get() = SocketSingleton.socket
    }

    private lateinit var chat: Chat
    private lateinit var adapter: MessageAdapter
    private lateinit var binding: ActivityChatBinding
    private val chatsViewModel: ChatsViewModel by viewModels {
        (application as ChatApplication).run {
            ChatsViewModelFactory(userRepository, chatRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cid = intent.getStringExtra("cid")
        val title = intent.getStringExtra("title")
        var isNewContact = title != null

        val messagesRecyclerView = binding.messages

        adapter = MessageAdapter()
        messagesRecyclerView.adapter = adapter
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        if (isNewContact) {
            binding.title.text = title
        } else {
            chat = chatsViewModel.getChat(cid!!)
            binding.title.text = chat.name
            observeMessages(cid)
        }

//        getMessages()
//        initialiseSocket()

        setMenuItemClickListener()

        binding.home.setOnClickListener {
            onBackPressed()
        }
        binding.title.setOnClickListener {
            Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show()
        }

        val textBox = binding.textBox
        val sendMessage = binding.sendMessage

        textBox.doAfterTextChanged { text ->
            sendMessage.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    if (text != null && text.isNotEmpty()) R.drawable.ic_send_message else R.drawable.ic_voice_message
                )
            )
        }

        sendMessage.setOnClickListener {
            if (textBox.text.isEmpty())
                return@setOnClickListener

            val jsonObject = JSONObject()
            jsonObject.put("from", AuthUtil.currentUser!!.uid)
            jsonObject.put("to", cid)
            jsonObject.put("message", textBox.text.toString())
            socket.emit("message", jsonObject, Ack {
                val messageJSON = it[0] as JSONObject
                val job = chatsViewModel.addMessage(messageJSON)
                if (isNewContact) {
                    job.invokeOnCompletion {
                        chat = chatsViewModel.getChat(cid!!)
                        observeMessages(cid)
                        isNewContact = false
                    }
                }
            })
            textBox.text.clear()
        }
    }

    private fun observeMessages(cid: String) {
        chatsViewModel.getMessages(cid).observe(this) { messages ->
            Log.d("Messages", messages.toString())
            adapter.submitList(messages)
        }
    }

    private fun setMenuItemClickListener() {
        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                else -> {
                    Toast.makeText(this, "Menu Item Selected", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }
}