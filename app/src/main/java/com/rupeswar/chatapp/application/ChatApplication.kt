package com.rupeswar.chatapp.application

import android.app.Application
import com.rupeswar.chatapp.database.ChatAppDatabase
import com.rupeswar.chatapp.repositories.ChatRepository
import com.rupeswar.chatapp.repositories.UserRepository

class ChatApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ChatAppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val chatRepository by lazy { ChatRepository() }
}