package com.example.mitraartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.textfield.TextInputEditText
import java.sql.PreparedStatement

class LoginEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_entry)


        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // Close button
        var closeButton = findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            finish()
        }

        // Login and password textFields
        val login = findViewById<TextInputEditText>(R.id.login_TextInput).text
        val password = findViewById<TextInputEditText>(R.id.password_TextInput).text

        // Entry button
        val enterButton = findViewById<Button>(R.id.enter_button)
        enterButton.setOnClickListener{

        }

        // Forget login or password button


    }
}