package com.rupeswar.chatapp.ui.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupeswar.chatapp.databinding.ActivityChatBinding
import com.rupeswar.chatapp.models.Message
import com.rupeswar.chatapp.utils.AuthUtil
import com.rupeswar.chatapp.utils.SocketSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    companion object {
        private val socket get() = SocketSingleton.socket
    }

    private lateinit var adapter: MessageAdapter
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cid = intent.getStringExtra("cid")

        val messages = binding.messages

        adapter = MessageAdapter()
        messages.adapter = adapter
        messages.layoutManager = LinearLayoutManager(this)

        getMessages()
        initialiseSocket()

        setMenuItemClickListener()

        binding.home.setOnClickListener {
            onBackPressed()
        }
        binding.title.setOnClickListener {
            Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show()
        }

        val textBox = binding.textBox
        binding.sendMessage.setOnClickListener {
            if(textBox.text.isEmpty())
                return@setOnClickListener

            val jsonObject = JSONObject()
            jsonObject.put("from", AuthUtil.currentUser!!.uid)
            jsonObject.put("to", AuthUtil.currentUser!!.uid)
            jsonObject.put("message", textBox.text.toString())
            socket.emit("message", jsonObject)
            textBox.text.clear()
        }
    }

    private fun initialiseSocket() {
        socket.on("message"){
            val messageJSON = it[0] as JSONObject
            val message = Message.fromJSON(messageJSON)

            GlobalScope.launch(Dispatchers.Main) {
                adapter.addMessage(message)
            }
        }
    }

    private fun getMessages() {
        socket.emit("chats", AuthUtil.currentUser!!.uid)
        socket.on("chats"){
            val jsonArray = it[0] as JSONArray
            val messageList = ArrayList<Message>()

            for(i in 0 until  jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                messageList.add(Message.fromJSON(jsonObject))
            }

            GlobalScope.launch(Dispatchers.Main) {
                adapter.updateMessages(messageList)
            }
        }
    }

    private fun setMenuItemClickListener() {
        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when(it.itemId) {
                else -> {
                    Toast.makeText(this, "Menu Item Selected", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }
}