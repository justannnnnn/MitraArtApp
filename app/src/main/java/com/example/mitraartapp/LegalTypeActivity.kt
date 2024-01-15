package com.example.mitraartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date

class LegalTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_type)

        val dbHandler = DBHandler(this@LegalTypeActivity)
        val getIDObj = getID(dbHandler.getEmail())
        getIDObj.execute("")
        val user_id = getIDObj.res

        var isRegistered = false

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // Text fields
        var nameTextField = findViewById<TextInputEditText>(R.id.name_TextInput)
        var innTextField = findViewById<TextInputEditText>(R.id.inn_TextInput)
        var ogrnTextField = findViewById<TextInputEditText>(R.id.ogrn_TextInput)
        var orderTextField = findViewById<TextInputEditText>(R.id.acts_TextInput)
        var regDateTextField = findViewById<TextInputEditText>(R.id.address_TextInput)

        val textFields = listOf<TextInputEditText>(nameTextField, innTextField,
            ogrnTextField, orderTextField, regDateTextField)

        val checkObj =  checkLegalType(user_id)
        checkObj.execute("")
        if (checkObj.res == "REGISTERED"){
            isRegistered = true
            nameTextField.setText(checkObj.n)
            innTextField.setText(checkObj.inn)
            ogrnTextField.setText(checkObj.ogrn)
            orderTextField.setText(checkObj.order)
            regDateTextField.setText(checkObj.ra)
        }

        // In-time validation
        var validation = ValidationHelper()
        innTextField.doAfterTextChanged {
            var inn = innTextField.text
            if (!inn?.let { it1 -> validation.rightINN(it1) }!!){
                innTextField.setError("ИНН должен состоять из 12 цифр")
            }
            else innTextField.error = null
        }
        ogrnTextField.doAfterTextChanged {
            var ogrn = ogrnTextField.text
            if (!ogrn?.let { it1 -> validation.rightOGRN(it1) }!!){
                ogrnTextField.setError("ОГРН должен состоять из 13 цифр")
            }
            else ogrnTextField.error = null
        }

        // Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            if (textFields.any{it.error != null} || textFields.any{it.text.contentEquals("")}) {
                Toast.makeText(
                    this@LegalTypeActivity,
                    "Not all field are filled in correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                // если валидация прошла успешно
                val addLegalObj = addLegalType(
                    user_id,
                    nameTextField.text.toString(),
                    innTextField.text.toString(),
                    ogrnTextField.text.toString(),
                    orderTextField.text.toString(),
                    regDateTextField.text.toString(),
                    isRegistered
                )
                addLegalObj.execute("")
                Toast.makeText(
                    this@LegalTypeActivity,
                    "Legal type was saved in DB",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}