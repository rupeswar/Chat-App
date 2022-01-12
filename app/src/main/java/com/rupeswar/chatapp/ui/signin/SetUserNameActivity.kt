package com.rupeswar.chatapp.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.databinding.ActivitySetUserNameBinding
import com.rupeswar.chatapp.ui.main.MainActivity
import com.rupeswar.chatapp.utils.AuthUtil
import com.rupeswar.chatapp.utils.VolleySingleton
import org.json.JSONObject

class SetUserNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetUserNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText = binding.userName
        binding.next.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", AuthUtil.currentUser!!.uid)
            jsonObject.put("userName", editText.text)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                "${getString(R.string.server_base_url)}/setUserName",
                jsonObject,
                {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                {
                    Log.d("Set Username", "An Error Occurred... ${it.message}")
                })

            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }
}