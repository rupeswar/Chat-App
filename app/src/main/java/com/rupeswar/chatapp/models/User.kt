package com.rupeswar.chatapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "gmail") val gmail: String,
    @ColumnInfo(name = "user_name") val userName: String
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): User {
            return User(
                jsonObject.getString("id"),
                jsonObject.optString("gmail"),
                jsonObject.optString("userName")
            )
        }
    }
}