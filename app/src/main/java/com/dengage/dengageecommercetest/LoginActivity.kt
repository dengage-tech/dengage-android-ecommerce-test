package com.dengage.dengageecommercetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.dengage.dengageecommercetest.data.CartManager
import com.dengage.sdk.Dengage

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.etUsername)
        loginButton = findViewById(R.id.btnLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            CartManager.init(applicationContext, username)
            if (username.isNotEmpty()) {
                val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                prefs.edit() { putBoolean("isLoggedIn", true) }
                prefs.edit() { putString("username", username) }
                Dengage.setContactKey(username)
                Handler(Looper.getMainLooper()).postDelayed({
                    Dengage.getInAppMessages()
                }, 2000)

                Dengage.getInAppMessages()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
