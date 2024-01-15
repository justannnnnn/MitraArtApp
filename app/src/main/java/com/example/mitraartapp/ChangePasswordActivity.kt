package com.example.mitraartapp

import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.mitraartapp.R
import com.google.android.material.textfield.TextInputEditText
import java.sql.ResultSet
import java.sql.SQLException

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Back button
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // Passwords text fields
        var oldPassTextField = findViewById<TextInputEditText>(R.id.oldPass_TextInput)
        var newPassTextField = findViewById<TextInputEditText>(R.id.newPass_TextInput)
        var repNewPassTextField = findViewById<TextInputEditText>(R.id.rep_newPass_TextInput)

        val textFields = listOf<TextInputEditText>(oldPassTextField, newPassTextField, repNewPassTextField)
        oldPassTextField.doAfterTextChanged {
            var old_password = oldPassTextField.text
            if (old_password?.length!! <= 8)
                oldPassTextField.setError("The password must be at least 9 characters long")
            else oldPassTextField.error = null
        }
        newPassTextField.doAfterTextChanged {
            var new_password = newPassTextField.text
            if (newPassTextField.error == "Passwords do not match") newPassTextField.error = null
            if (new_password?.length!! <= 8)
                newPassTextField.setError("The password must be at least 9 characters long")
            else newPassTextField.error = null
        }
        repNewPassTextField.doAfterTextChanged {
            var rep_password = repNewPassTextField.text
            if (repNewPassTextField.error == "Passwords do not match") repNewPassTextField.error = null
            if (rep_password?.length!! <= 8)
                repNewPassTextField.setError("The password must be at least 9 characters long")
            else repNewPassTextField.error = null
        }

        // Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            val dbHandler = DBHandler(this@ChangePasswordActivity)
            val db_password = dbHandler.getPassword()
            Log.e(TAG, "old password: " + db_password)
            if (textFields.any{it.error != null} || textFields.any{it.text.contentEquals("")}) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Not all field are filled in correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!newPassTextField.text.contentEquals(repNewPassTextField.text)) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                newPassTextField.setError("Passwords do not match")
                repNewPassTextField.setError("Passwords do not match")
                newPassTextField.text = null
                repNewPassTextField.text = null
            }
            else if (!oldPassTextField.text.contentEquals(db_password)){
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Wrong old password",
                    Toast.LENGTH_SHORT
                ).show()
                oldPassTextField.setError("Wrong old password")
                oldPassTextField.text = null
            }
            else{
                dbHandler.setPassword(Base64.encodeToString(newPassTextField.text?.toString()?.toByteArray(), 0))
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Password changed",
                    Toast.LENGTH_SHORT
                ).show()
                val updateObj = updatePassword(dbHandler.getEmail(), newPassTextField.text.toString())
                updateObj.execute("")
                Log.e(TAG, "new password: " + dbHandler.getPassword())
                finish()
            }
        }

    }
}