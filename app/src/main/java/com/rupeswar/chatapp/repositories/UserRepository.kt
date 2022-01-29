package com.rupeswar.chatapp.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.rupeswar.chatapp.daos.UserDao
import com.rupeswar.chatapp.models.User
import com.rupeswar.chatapp.utils.SocketSingleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
            suspendCoroutine { suspendCoroutine ->
                SocketSingleton.socket.let { socket ->
                    socket.emit("user", userId)
                    socket.on("user"){
                        val userJSON = it[0] as JSONObject
                        val user = User.fromJson(userJSON)
                        suspendCoroutine.resume(user)
                        socket.off("user")
                    }
                }
            }
        }
    }

    suspend fun getUserByUserName(userName: String): User? {
        return null ?: run {
            suspendCoroutine<User?> { suspendCoroutine ->
                SocketSingleton.socket.let { socket ->
                    Log.d("Find", "Sending Request")
                    socket.emit("add-user", userName)
                    socket.on("add-user"){
                        val userJSON = it[0] as JSONObject

                        val user = User.fromJson(userJSON)
                        suspendCoroutine.resume(user)
                        Log.d("Find", "Request received")
                        socket.off("add-user")
                        socket.off("add-user-error")
                    }
                    socket.on("add-user-error"){
                        suspendCoroutine.resume(null)
                        socket.off("add-user")
                        socket.off("add-user-error")
                    }
                }
            }
        }
    }
}