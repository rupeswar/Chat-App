package com.rupeswar.chatapp.models

import org.json.JSONObject

data class User(val uid: String, val gmail: String, val userName: String) {
    companion object {
        fun fromJson(jsonObject: JSONObject): User {
            return User(jsonObject.getString("id"), jsonObject.optString("gmail"), jsonObject.optString("userName"))
        }
    }
}