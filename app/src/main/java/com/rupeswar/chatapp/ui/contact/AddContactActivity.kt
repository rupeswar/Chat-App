package com.rupeswar.chatapp.ui.contact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.rupeswar.chatapp.application.ChatApplication
import com.rupeswar.chatapp.databinding.ActivityAddContactBinding
import com.rupeswar.chatapp.ui.chat.ChatActivity
import com.rupeswar.chatapp.viewmodels.ChatsViewModel
import com.rupeswar.chatapp.viewmodels.ChatsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddContactBinding
    private val chatsViewModel: ChatsViewModel by viewModels{
        (application as ChatApplication).run {
            ChatsViewModelFactory(userRepository, chatRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addContact.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                val user = chatsViewModel.getUserByUserName(binding.userName.text.toString())

                if (user != null) {
                    val chatIntent = Intent(this@AddContactActivity, ChatActivity::class.java)
                    chatIntent.putExtra("cid", user.uid)
                    chatIntent.putExtra("title", user.userName)
                    startActivity(chatIntent)
                    finish()
                } else {
                    Log.d("Add Contact", "Invalid Username")
                    GlobalScope.launch(Dispatchers.Main){
                        Toast.makeText(
                            this@AddContactActivity,
                            "Invalid Username",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}