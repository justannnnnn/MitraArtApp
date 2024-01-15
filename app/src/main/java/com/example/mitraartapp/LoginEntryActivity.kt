package com.example.mitraartapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

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
        val loginTextField = findViewById<TextInputEditText>(R.id.login_TextInput)
        val passwordTextField = findViewById<TextInputEditText>(R.id.password_TextInput)
        val textFields = listOf<TextInputEditText>(loginTextField, passwordTextField)
        loginTextField.doAfterTextChanged { loginTextField.error = null }
        passwordTextField.doAfterTextChanged { passwordTextField.error = null }

        // Entry button
        val enterButton = findViewById<Button>(R.id.enter_button)
        enterButton.setOnClickListener{
            if (textFields.any{it.text.contentEquals("")}){
                Toast.makeText(this@LoginEntryActivity, "Not all fields are filled", Toast.LENGTH_SHORT).show()
            }
            val login = loginTextField.text.toString()
            val password = Base64.encodeToString(passwordTextField.text.toString().toByteArray(), 0)
            val checkUserObj = checkUser(email = login, password = password)
            checkUserObj.execute("")
            var result = checkUserObj.res
            if (result.equals("REGISTERED")){
                var getUserFirst = getUserInfo(login, "FirstName")
                var getUserLast = getUserInfo(login, "LastName")
                getUserFirst.execute("")
                getUserLast.execute("")
                val first_name = getUserFirst.res
                val last_name = getUserLast.res
                val dbHandler = DBHandler(this@LoginEntryActivity)
                // TODO: не факт, что хэш код подходит - тестовая версия
                dbHandler.deleteTable()
                dbHandler.createTable()
                dbHandler!!.addNewAccount(login, password, first_name, last_name)
                val intent = Intent(this@LoginEntryActivity, RegisteredAccountActivity::class.java)
                startActivity(intent)
            }
            else if (result.equals("NOT REGISTERED")){
                Toast.makeText(this@LoginEntryActivity, "Login or password entered incorrectly", Toast.LENGTH_SHORT).show()
                textFields.map { it.error = "Invalid login or password" }
            }

        // Forget login or password button


        }
    }
}