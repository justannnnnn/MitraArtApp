package com.example.mitraartapp

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class PhysicalTypeActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_type)

        val dbHandler = DBHandler(this@PhysicalTypeActivity)
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
        var surnameTextField = findViewById<TextInputEditText>(R.id.surname_TextInput)
        var nameTextField = findViewById<TextInputEditText>(R.id.name_TextInput)
        var patronymicTextField = findViewById<TextInputEditText>(R.id.patronymic_TextInput)
        var birthDateTextField = findViewById<TextInputEditText>(R.id.bd_TextInput)
        var passportTextField = findViewById<TextInputEditText>(R.id.passport_TextInput)
        var passDateTextField = findViewById<TextInputEditText>(R.id.pass_date_TextInput)
        var whoPassTextField = findViewById<TextInputEditText>(R.id.who_pass_TextInput)
        var passResidenceTextField = findViewById<TextInputEditText>(R.id.pass_residence_TextInput)
        var factResidenceTextField = findViewById<TextInputEditText>(R.id.fact_residence_TextInput)

        val textFields = listOf<TextInputEditText>(surnameTextField, nameTextField, patronymicTextField,
            birthDateTextField, passportTextField, passDateTextField, whoPassTextField,
            passResidenceTextField, factResidenceTextField)


        val checkObj =  checkPhysicalType(user_id)
        checkObj.execute("")
        if (checkObj.res == "REGISTERED"){
            isRegistered = true
            surnameTextField.setText(checkObj.sn)
            nameTextField.setText(checkObj.n)
            patronymicTextField.setText(checkObj.pat)
            birthDateTextField.setText(checkObj.bd.toString())
            passportTextField.setText(checkObj.pas)
            passDateTextField.setText(checkObj.pd.toString())
            whoPassTextField.setText(checkObj.pw)
            passResidenceTextField.setText(checkObj.pr)
            factResidenceTextField.setText(checkObj.fr)
        }

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
                nameTextField.setError("The name should consist of letters")
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
        birthDateTextField.inputType = InputType.TYPE_NULL
        birthDateTextField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Введите дату рождения")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.show(supportFragmentManager, "tag")
                datePicker.addOnPositiveButtonClickListener {
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val date = dateFormatter.format(Date(it))
                    birthDateTextField.setText(date)
                }
            }
        }
        passportTextField.doAfterTextChanged{
            var passport = passportTextField.text
            if (!passport?.let { it1 -> validation.rightPassport(it1) }!!){
                passportTextField.setError("Серия и номер паспорта состоят из 10 цифр")
            }
            else passportTextField.error = null
        }
        passDateTextField.inputType = InputType.TYPE_NULL
        passDateTextField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Введите дату рождения")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.show(supportFragmentManager, "tag")
                datePicker.addOnPositiveButtonClickListener {
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val date = dateFormatter.format(Date(it))
                    passDateTextField.setText(date)
                }
            }
        }
        whoPassTextField.doAfterTextChanged {
            val who_pass = whoPassTextField.text
            if (!validation.isText(who_pass)!!){
                whoPassTextField.setError("В этом поле не может быть цифр")
            }
            else whoPassTextField.error = null
        }

        // Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            if (textFields.any{it.error != null} || textFields.any{it.text.contentEquals("")}) {
                Toast.makeText(
                    this@PhysicalTypeActivity,
                    "Not all field are filled in correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
            // если валидация прошла успешно
                val addPhysObj = addPhysicalType(
                        user_id,
                        nameTextField.text.toString(),
                        surnameTextField.text.toString(),
                        patronymicTextField.text.toString(),
                        birthDateTextField.text.toString(),
                        passportTextField.text.toString(),
                        passDateTextField.text.toString(),
                        whoPassTextField.text.toString(),
                        passResidenceTextField.text.toString(),
                        factResidenceTextField.text.toString(),
                        isRegistered
                )
                addPhysObj.execute("")
                Toast.makeText(
                    this@PhysicalTypeActivity,
                    "Physical type was saved in DB",
                    Toast.LENGTH_SHORT
                ).show()
                dbHandler.setSurname(surnameTextField.text.toString())
                dbHandler.setName(nameTextField.text.toString())
                finish()
            }
        }

    }
}