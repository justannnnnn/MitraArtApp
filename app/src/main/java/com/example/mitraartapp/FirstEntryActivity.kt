package com.example.mitraartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class FirstEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_entry)

        // Entry by login button
        var buttonLogin = findViewById<Button>(R.id.enter_by_login_button)
        buttonLogin.setOnClickListener{

        }

        // Entry by VK button
        var buttonVK = findViewById<Button>(R.id.enter_by_vk_button)
        buttonLogin.setOnClickListener{

        }

        // Entry by Google button
        var buttonGoogle = findViewById<Button>(R.id.enter_by_google_button)
        buttonLogin.setOnClickListener{

        }

        // Create account button
        var buttonCreateAccount = findViewById<Button>(R.id.create_account_button)
        buttonCreateAccount.setOnClickListener{

        }
    }
}