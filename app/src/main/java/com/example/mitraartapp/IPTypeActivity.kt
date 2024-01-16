package com.example.mitraartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date

class IPTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iptype)

        val dbHandler = DBHandler(this@IPTypeActivity)
        val getIDObj = getID(dbHandler.getEmail())
        getIDObj.execute("")
        val user_id = getIDObj.res

        var isRegistered = false

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            MaterialAlertDialogBuilder(this@IPTypeActivity)
                .setTitle("Вы уверены, что хотите выйти?")
                .setMessage("Чтобы сохранить данные, нажмите на кнопку в конце страницы")
                .setNegativeButton("Выйти") { dialog, which ->
                    // Respond to negative button press
                    dialog.cancel()
                    finish()
                }
                .setPositiveButton("Остаться") { dialog, which ->
                    // Respond to positive button press
                    dialog.cancel()
                }
                .show()
        }

        // Text fields
        var nameTextField = findViewById<TextInputEditText>(R.id.name_TextInput)
        var innTextField = findViewById<TextInputEditText>(R.id.inn_TextInput)
        var ogrnTextField = findViewById<TextInputEditText>(R.id.ogrn_TextInput)
        var passportTextField = findViewById<TextInputEditText>(R.id.passport_TextInput)
        var passDateTextField = findViewById<TextInputEditText>(R.id.pass_date_TextInput)
        var passResidenceTextField = findViewById<TextInputEditText>(R.id.pass_residence_TextInput)
        var factResidenceTextField = findViewById<TextInputEditText>(R.id.fact_residence_TextInput)

        val textFields = listOf<TextInputEditText>(nameTextField, innTextField,
            ogrnTextField, passportTextField, passDateTextField,
            passResidenceTextField, factResidenceTextField)

        val checkObj =  checkIPType(user_id)
        checkObj.execute("")
        if (checkObj.res == "REGISTERED"){
            isRegistered = true
            nameTextField.setText(checkObj.n)
            innTextField.setText(checkObj.inn)
            ogrnTextField.setText(checkObj.ogrn)
            passportTextField.setText(checkObj.pas)
            passDateTextField.setText(checkObj.pd.toString())
            passResidenceTextField.setText(checkObj.pr)
            factResidenceTextField.setText(checkObj.fr)
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

        // Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            if (textFields.any{it.error != null} || textFields.any{it.text.contentEquals("")}) {
                Toast.makeText(
                    this@IPTypeActivity,
                    "Not all field are filled in correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                // если валидация прошла успешно
                val addIPObj = addIPType(
                    user_id,
                    nameTextField.text.toString(),
                    innTextField.text.toString(),
                    ogrnTextField.text.toString(),
                    passportTextField.text.toString(),
                    passDateTextField.text.toString(),
                    passResidenceTextField.text.toString(),
                    factResidenceTextField.text.toString(),
                    isRegistered
                )
                addIPObj.execute("")
                Toast.makeText(
                    this@IPTypeActivity,
                    "IP type was saved in DB",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}