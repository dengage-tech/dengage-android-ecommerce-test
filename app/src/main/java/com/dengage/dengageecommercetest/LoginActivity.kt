package com.dengage.dengageecommercetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

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
            if (username.isNotEmpty()) {
                // Save login state (using SharedPreferences similar to UserDefaults)
                getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    .edit() { putBoolean("isLoggedIn", true) }
                getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    .edit() { putString("username", username) }
                // Navigate to the MainActivity (which hosts your tab navigation)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
