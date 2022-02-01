package com.rupeswar.chatapp.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.rupeswar.chatapp.daos.UserDao
import com.rupeswar.chatapp.models.User
import com.rupeswar.chatapp.utils.SocketSingleton
import io.socket.client.Ack
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UserRepository(private val userDao: UserDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun getUser(userId: String): User {
        return userDao.getUser(userId) ?: run {
            suspendCoroutine { coroutine ->
                SocketSingleton.socket.let { socket ->
                    socket.emit("user", userId, Ack{
                        val userJSON = it[0] as JSONObject
                        val user = User.fromJson(userJSON)
                        coroutine.resume(user)
                    })
                }
            }
        }
    }

    suspend fun getUserByUserName(userName: String): User? {
        return null ?: run {
            suspendCoroutine { coroutine ->
                SocketSingleton.socket.let { socket ->
                    Log.d("Find", "Sending Request")
                    socket.emit("add-user", userName, Ack{
                        val status = it[0] as String

                        if(status == "error") {
                            coroutine.resume(null)
                            return@Ack
                        }

                        val userJSON = it[1] as JSONObject

                        val user = User.fromJson(userJSON)
                        coroutine.resume(user)
                        Log.d("Find", "Request received")
                    })
                }
            }
        }
    }
}