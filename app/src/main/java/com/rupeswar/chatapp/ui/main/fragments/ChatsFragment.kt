package com.rupeswar.chatapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.models.Message

class ChatsFragment : Fragment() {
    private var root: View? = null
    private val IMAGE_URL = "https://github.com/rupeswar/codeforces_assistant/blob/master/assets/ic_codeforces.png?raw=true"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_chats, container, false)
        val chats = root!!.findViewById<RecyclerView>(R.id.chats)
        val adapter = ChatsAdapter()
        chats.adapter = adapter
        chats.layoutManager = LinearLayoutManager(root!!.context)

        val chatList = ArrayList<Chat>()
        chatList.add(Chat("1", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("2", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("3", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("4", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("5", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("6", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("7", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("8", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("9", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))
        chatList.add(Chat("0", "A Rupeswar Subudhi", Message("0", "You", "Hello!", System.currentTimeMillis()), IMAGE_URL))

        adapter.updateChats(chatList)

        Toast.makeText(context, "Chats Added", Toast.LENGTH_SHORT).show()

        return root
    }
}