package com.rupeswar.chatapp.daos

import androidx.room.*
import com.rupeswar.chatapp.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE uid = :userId LIMIT 1")
    suspend fun getUser(userId: String): User?
}