package com.rupeswar.chatapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rupeswar.chatapp.daos.UserDao
import com.rupeswar.chatapp.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class ChatAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ChatAppDatabase? = null

        fun getDatabase(context: Context): ChatAppDatabase {
            // If the INSTANCE is not null, then return it,
            // If it is, then create the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatAppDatabase::class.java,
                    "chat_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}