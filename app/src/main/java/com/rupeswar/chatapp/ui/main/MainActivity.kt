package com.rupeswar.chatapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.application.ChatApplication
import com.rupeswar.chatapp.databinding.ActivityMainBinding
import com.rupeswar.chatapp.utils.AuthUtil
import com.rupeswar.chatapp.utils.SocketSingleton
import com.rupeswar.chatapp.viewmodels.ChatsViewModel
import com.rupeswar.chatapp.viewmodels.ChatsViewModelFactory
import org.json.JSONArray
import org.json.JSONObject
import androidx.activity.viewModels
import com.rupeswar.chatapp.ui.contact.AddContactActivity
import io.socket.client.Ack

class MainActivity : AppCompatActivity() {

    companion object {
        private val socket get() = SocketSingleton.socket
    }

    private lateinit var binding: ActivityMainBinding
    private val chatsViewModel: ChatsViewModel by viewModels {
        (application as ChatApplication).run {
            ChatsViewModelFactory(userRepository, chatRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this.supportFragmentManager, this.lifecycle)
        val pager: ViewPager2 = binding.pager
        pager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = if (position == 0) "Chats" else "Invalid"
        }.attach()

        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener {
            val addContactIntent = Intent(this, AddContactActivity::class.java)
            startActivity(addContactIntent)
        }

        initialiseSocket()
    }

    private fun initialiseSocket() {
        val serverURL = getString(R.string.server_base_url)
        SocketSingleton.initialiseSocket(serverURL)

        getMessages()

        socket.on("message") {
            val messageJSON = it[0] as JSONObject
            chatsViewModel.addMessage(messageJSON)
        }
    }

    private fun getMessages() {
        socket.emit("chats", AuthUtil.currentUser!!.uid, Ack {
            val jsonArray = it[0] as JSONArray

            for (i in 0 until jsonArray.length()) {
                val messageJSON = jsonArray.getJSONObject(i)
                chatsViewModel.addMessage(messageJSON)
            }
        })
    }


}