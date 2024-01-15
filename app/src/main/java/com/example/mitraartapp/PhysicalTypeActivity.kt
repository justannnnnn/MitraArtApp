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


        val checkObj =  checkUser(user_id)
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


    class addPhysicalType(id: String, name: String, surname: String, patronymic: String,
                          birthDate: String, passport: String, passDate: String, passWho: String,
                          passResidence: String, factResidence: String, isRegistered: Boolean = false) : AsyncTask<String, Unit, String>() {
        val id = id
        val bd = birthDate
        val pas = passport
        val n = name
        val sn = surname
        val pat = patronymic
        val pd = passDate
        val pw = passWho
        val pr = passResidence
        val fr = factResidence
        val is_reg = isRegistered
        var queryStmt = ""
        //var res = "nothing"
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute();
            try {
                val connect = ConnectionHelper.CONN()
                if (is_reg){
                    queryStmt = "UPDATE dbo.PhysicalTypes SET FirstName = '" + n + "', MiddleName = '" + pat + "', LastName = '" + sn + "', " +
                            "BirthDate = CAST('" + bd + "' as DATETIME), Passport = '" + pas + "', PassportDate = CAST('" + pd + "' as DATETIME), " +
                            "PassportWho = '" + pw + "', PassportResidence = '" + pr + "', FacticalResidence = '" + fr + "' WHERE Id = '" + id + "'"
                }
                else {
                    queryStmt = "INSERT INTO dbo.PhysicalTypes(Id, FirstName, MiddleName, LastName, " +
                            "BirthDate, Passport, PassportDate, PassportWho, PassportResidence,  " +
                            "FacticalResidence) " +
                            "VALUES ('" + id + "', '" + n + "', '" + pat + "', '" + sn + "', CAST('" + bd + "' as DATETIME), '" + pas +
                            "', CONVERT('" + pd + "' as DATETIME), '" + pw + "', '" + pr + "', '" + fr + "')"
                }

                var preparedStatement = connect?.prepareStatement(queryStmt)
                preparedStatement?.executeUpdate()
                preparedStatement?.close()

                queryStmt = "UPDATE dbo.Account SET FirstName = '" + n + "', MiddleName = '" + pat + "', LastName = '" + sn + "' WHERE Id = '" + id + "'"
                preparedStatement = connect?.prepareStatement(queryStmt)
                preparedStatement?.executeUpdate()
                preparedStatement?.close()

            } catch (e : SQLException) {
                e.printStackTrace()
            } catch (e : Exception) {
                //Log("Exception. Please check your code and database.")
            }
        }
        @Deprecated("Deprecated in Java")
        override protected fun doInBackground(vararg params : String) : String? {
            return "nothing"
        }
    }

    // объект для получения ID из глобальной БД
    class getID(email: String?) : AsyncTask<String, Unit, String>() {
        val e = email
        var res = "nothing"
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute();
            try {
                val connect = ConnectionHelper.CONN();
                var queryStmt = "SELECT Id FROM dbo.Account WHERE Email = '" + e + "'"
                val preparedStatement = connect?.prepareStatement(queryStmt);
                var resultQuery: ResultSet? = null
                resultQuery = preparedStatement?.executeQuery()
                // если в ResultSet есть строка - пользователь есть в БД
                if (resultQuery!!.next()){
                    res = resultQuery.getString(1)
                }
                preparedStatement?.close()

            } catch (e : SQLException) {
                e.printStackTrace()
            } catch (e : Exception) {
                Log.e(ContentValues.TAG, "Exception. Please check your code and database.")
            }
        }
        @Deprecated("Deprecated in Java")
        override protected fun doInBackground(vararg params : String) : String? {
            return "nothing"
        }
    }

    //объект для проверки, есть ли уже запись
    class checkUser(id: String) : AsyncTask<String, Unit, String>() {
        val id = id
        var bd = Date()
        var pas = ""
        var n = ""
        var sn = ""
        var pat = ""
        var pd = Date()
        var pw = ""
        var pr = ""
        var fr = ""
        var res = "nothing"
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute();
            try {
                val connect = ConnectionHelper.CONN();
                var queryStmt = "SELECT FirstName, MiddleName, LastName, BirthDate, Passport, " +
                                "PassportDate, PassportWho, PassportResidence, FacticalResidence" +
                                " FROM dbo.PhysicalTypes WHERE Id = " + "'" + id + "'"
                val preparedStatement = connect?.prepareStatement(queryStmt);
                var resultQuery: ResultSet? = null
                resultQuery = preparedStatement?.executeQuery()
                // если в ResultSet есть строка - пользователь есть в БД
                if (resultQuery!!.next()){
                    n = resultQuery.getString(1)
                    pat = resultQuery.getString(2)
                    sn = resultQuery.getString(3)
                    bd = resultQuery.getDate(4)
                    pas = resultQuery.getString(5)
                    pd = resultQuery.getDate(6)
                    pw = resultQuery.getString(7)
                    pr = resultQuery.getString(8)
                    fr = resultQuery.getString(9)
                    res = "REGISTERED"
                }
                else res = "NOT REGISTERED"
                preparedStatement?.close()

            } catch (e : SQLException) {
                e.printStackTrace()
                res = e.toString()
            } catch (e : Exception) {
                res = "Exception. Please check your code and database."
            }
        }
        @Deprecated("Deprecated in Java")
        override protected fun doInBackground(vararg params : String) : String? {
            return "nothing"
        }
    }

    /*class updatePhysicalType(id: String, fn, ln, ) : AsyncTask<String, Unit, String>() {
        val e = email
        val p = password
        val fn = firstName
        val ln = lastName
        var res = "nothing"
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute();
            try {
                val connect = ConnectionHelper.CONN();
                var queryStmt = "UPDATE dbo.Account SET PasswordHash = " + "'" + Base64.encodeToString(
                    p?.toByteArray(), 0) + "' WHERE Email = '" + e + "'"
                val preparedStatement = connect?.prepareStatement(queryStmt)
                preparedStatement?.executeUpdate()
                preparedStatement?.close()

            } catch (e : SQLException) {
                e.printStackTrace()
            } catch (e : Exception) {
                //Log("Exception. Please check your code and database.")
            }
        }
        @Deprecated("Deprecated in Java")
        override protected fun doInBackground(vararg params : String) : String? {
            return "nothing"
        }
    }*/
}