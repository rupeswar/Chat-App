package com.rupeswar.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.rupeswar.chatapp.models.Chat
import com.rupeswar.chatapp.models.Message
import com.rupeswar.chatapp.models.User
import com.rupeswar.chatapp.repositories.ChatRepository
import com.rupeswar.chatapp.repositories.UserRepository
import com.rupeswar.chatapp.utils.AuthUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.IllegalArgumentException

class ChatsViewModel(private val userRepository: UserRepository, private val chatRepository: ChatRepository) : ViewModel() {

    // Using LiveData and caching what allUsers returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: LiveData<List<User>> = userRepository.allUsers.asLiveData()
    val allChats: LiveData<ArrayList<Chat>> = chatRepository.allChats

    private val messageJSONFlow = MutableSharedFlow<JSONObject>()

    init {
        val messageFlow = messageJSONFlow.map { messageJSON ->
            val senderId = messageJSON.getString("from")
            val recipientId = messageJSON.getString("to")
            val sender = getUserNameOrYou(senderId)
            val recipient = getUserNameOrYou(recipientId)
            messageJSON.put("from", sender)
            messageJSON.put("to", recipient)

            val message = Message.fromJSON(messageJSON)

            if(sender == "You")
                arrayOf(recipientId, recipient, message)
            else
                arrayOf(senderId, sender, message)
        }

        viewModelScope.launch {
            messageFlow
                .buffer()
                .collect {
                chatRepository.addMessage(it[0] as String, it[1] as String, it[2] as Message)
            }
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }

    suspend fun getUser(userId: String): User {
        return userRepository.getUser(userId)
    }

    suspend fun getUserNameOrYou(userId: String): String {
        return if(userId == AuthUtil.currentUser!!.uid) "You" else getUser(userId).userName
    }

    suspend fun getUserByUserName(userName: String): User? {
        return userRepository.getUserByUserName(userName)
    }

    fun getChat(chatId: String): Chat {
        return chatRepository.getChat(chatId)
    }

    fun addMessage(messageJSON: JSONObject) = viewModelScope.launch {
        messageJSONFlow.emit(messageJSON)
    }

    fun getMessages(chatId: String): LiveData<ArrayList<Message>> {
        return chatRepository.getChatMessages(chatId)
    }
}

class ChatsViewModelFactory(private val userRepository: UserRepository, private val chatRepository: ChatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatsViewModel(userRepository, chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}