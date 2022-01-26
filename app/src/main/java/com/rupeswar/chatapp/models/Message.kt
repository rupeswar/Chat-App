package com.rupeswar.chatapp.models

import org.json.JSONObject

data class Message(val mid: String, val sender: String, val recipient: String, val message: String, val time: Long) {
    companion object {
        fun fromJSON(jsonObject: JSONObject): Message {
            return Message(
                jsonObject.getString("id"),
                jsonObject.getString("from"),
                jsonObject.getString("to"),
                jsonObject.getString("message"),
                jsonObject.getLong("time")
            )
        }
    }
}