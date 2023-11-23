package com.example.mitraartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // Text fields
        var surnameTextField = findViewById<TextInputEditText>(R.id.surname_TextInput)
        var nameTextField = findViewById<TextInputEditText>(R.id.name_TextInput)
        var patronymicTextField = findViewById<TextInputEditText>(R.id.patronymic_TextInput)
        var emailTextField = findViewById<TextInputEditText>(R.id.email_TextInput)
        var phoneTextField = findViewById<TextInputEditText>(R.id.phone_TextInput)
        var passwordTextField = findViewById<TextInputEditText>(R.id.create_password_TextInput)
        var repPasswordTextField = findViewById<TextInputEditText>(R.id.repeat_password_TextInput)

        val textFields = listOf<TextInputEditText>(surnameTextField, nameTextField, patronymicTextField,
            emailTextField, phoneTextField, passwordTextField, repPasswordTextField)

        // In-time validation
        var validation = ValidationHelper()
        surnameTextField.doAfterTextChanged {
            var surname = surnameTextField.text
            if (!validation.isText(surname)!!){
                surnameTextField.setError("The surname should consist of letters")
            }
            else surnameTextField.error = null

        }
        nameTextField.doAfterTextChanged {
            var name = nameTextField.text
            if (!validation.isText(name)!!){
                surnameTextField.setError("The name should consist of letters")
            }
            else nameTextField.error = null

        }
        patronymicTextField.doAfterTextChanged {
            var patronymic = patronymicTextField.text
            if (!validation.isText(patronymic)!!){
                patronymicTextField.setError("The patronymic should consist of letters")
            }
            else patronymicTextField.error = null

        }
        emailTextField.doAfterTextChanged {
            var email = emailTextField.text
            if (!validation.isEmail(email)!!){
                emailTextField.setError("Invalid format for email")
            }
            else emailTextField.error = null

        }
        phoneTextField.doAfterTextChanged {
            var phone = phoneTextField.text
            if (!validation.isPhone(phone)!!){
                phoneTextField.setError("Invalid format for phone number")
            }
            else phoneTextField.error = null

        }
        passwordTextField.doAfterTextChanged {
            var password = passwordTextField.text
            if (passwordTextField.error == "Passwords do not match") passwordTextField.error = null
            if (password?.length!! <= 8)
                passwordTextField.setError("The password must be at least 9 characters long")
            else passwordTextField.error = null
        }
        repPasswordTextField.doAfterTextChanged {
            var rep_password = repPasswordTextField.text
            if (repPasswordTextField.error == "Passwords do not match") repPasswordTextField.error = null
            if (rep_password?.length!! <= 8)
                repPasswordTextField.setError("The password must be at least 9 characters long")
            else repPasswordTextField.error = null
        }

        // Gender button
        val manButton = findViewById<Button>(R.id.man_button)
        val womanButton = findViewById<Button>(R.id.woman_button)
        var isMan = true
        manButton.setOnClickListener{
            manButton.setTextColor(resources.getColor(R.color.black))
            womanButton.setTextColor(resources.getColor(R.color.medium_grey))
            isMan = true
        }
        womanButton.setOnClickListener{
            womanButton.setTextColor(resources.getColor(R.color.black))
            manButton.setTextColor(resources.getColor(R.color.medium_grey))
            isMan = false
        }

        // если попали сюда через API
        val dbHandler = DBHandler(this@RegistrationActivity)
        if (dbHandler.tableExists()){
            surnameTextField.setText(dbHandler.getSurname())
            nameTextField.setText(dbHandler.getName())
            emailTextField.setText(dbHandler.getEmail())
        }


        // Checkboxes
        val iAm18 = findViewById<CheckBox>(R.id.i_am_18_checkBox)
        val informedCheckBox = findViewById<CheckBox>(R.id.informed_agreed_checkBox)
        val wantNotifCheckBox = findViewById<CheckBox>(R.id.want_notif_checkBox)
        val checkBoxes = listOf<CheckBox>(iAm18, informedCheckBox, wantNotifCheckBox)
        checkBoxes.map{it.setOnCheckedChangeListener { buttonView, isChecked ->  buttonView.error = null}}


        // Continue button
        var continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener{
            if (textFields.any{it.error != null} || textFields.any{it.text.contentEquals("")}) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Not all field are filled in correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!passwordTextField.text.contentEquals(repPasswordTextField.text)) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                passwordTextField.setError("Passwords do not match")
                repPasswordTextField.setError("Passwords do not match")
                passwordTextField.text = null
                repPasswordTextField.text = null
            }
            else if (checkBoxes.any { !it.isChecked }) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "It is necessary to mark all checkboxes",
                    Toast.LENGTH_SHORT
                ).show()
                checkBoxes.map{if (!it.isChecked) it.setError("Not marked")}
            }



        }

    }
}