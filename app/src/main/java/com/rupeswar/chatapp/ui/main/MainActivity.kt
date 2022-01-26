package com.rupeswar.chatapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.databinding.ActivityMainBinding
import com.rupeswar.chatapp.utils.SocketSingleton

class MainActivity : AppCompatActivity() {

    companion object {
        private val socket get() = SocketSingleton.socket
    }

    private lateinit var binding: ActivityMainBinding

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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val serverURL = getString(R.string.server_base_url)
        SocketSingleton.initialiseSocket(serverURL)

        socket.on("connect") {
            socket.emit("test", "Hello")
        }
    }


}