package com.rupeswar.chatapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.models.Message

class ChatRepository {
    val allChats = MutableLiveData<ArrayList<Chat>>(ArrayList())
    private val chatsMap = HashMap<String, Chat>()
    private val chatMessages = HashMap<String, MutableLiveData<ArrayList<Message>>>()

    fun addMessage(chatId: String, chatRecipient: String, message: Message) {
        val chat = chatsMap[chatId] ?: let {
            val chat = Chat(chatId, chatRecipient, message, null)
             allChats.run {
                 Log.d("LiveData", "Changing allChats")
                 setValue(ArrayList(value!!).apply { add(chat) })
             }
            chatsMap[chatId] = chat
            chatMessages[chatId] = MutableLiveData(ArrayList())
            chat
        }
        chat.lastMessage = message
        chatMessages[chatId]!!.run {
            Log.d("LiveData", "Changing chatMessages")
            setValue(ArrayList(value!!).apply { add(message) })
        }
    }

    fun getChatMessages(chatId: String): LiveData<ArrayList<Message>> {
        return chatMessages[chatId]!!
    }

    fun getChat(chatId: String): Chat {
        return chatsMap[chatId]!!
    }
}