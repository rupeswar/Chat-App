package com.rupeswar.chatapp.utils

import com.rupeswar.chatapp.models.User

class AuthUtil {
    companion object {
        var currentUser: User? = null
        var authToken: String? = null
    }
}