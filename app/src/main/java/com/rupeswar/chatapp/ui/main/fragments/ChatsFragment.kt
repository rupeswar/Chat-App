package com.rupeswar.chatapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.application.ChatApplication
import com.rupeswar.chatapp.viewmodels.ChatsViewModel
import com.rupeswar.chatapp.viewmodels.ChatsViewModelFactory

class ChatsFragment : Fragment() {
    private var root: View? = null
    private val chatsViewModel: ChatsViewModel by activityViewModels{
        (requireActivity().application as ChatApplication).run {
            ChatsViewModelFactory(userRepository, chatRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_chats, container, false)
        val chatsRecyclerView = root!!.findViewById<RecyclerView>(R.id.chats)
        val adapter = ChatsAdapter()
        chatsRecyclerView.adapter = adapter
        chatsRecyclerView.layoutManager = LinearLayoutManager(root!!.context)

//        val chatList = ArrayList<Chat>()
//        chatList.add(Chat("1", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("2", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("3", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("4", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("5", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("6", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("7", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("8", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("9", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))
//        chatList.add(Chat("0", "A Rupeswar Subudhi", Message("0", "You", "Rupeswar", "Hello!", System.currentTimeMillis()), IMAGE_URL))

//        adapter.submitList(chatList)

        chatsViewModel.allChats.observe(this.viewLifecycleOwner){chats ->
            Log.d("Chats", chats.toString())
            adapter.submitList(chats)
        }

        Toast.makeText(context, "Chats Added", Toast.LENGTH_SHORT).show()

        return root
    }
}