package com.example.mitraartapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Base64
import android.util.Log

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date


object ConnectionHelper {
    @SuppressLint("NewApi")
    fun CONN(): Connection? {
        val _user = "sa"
        val _pass = "An4an2an5!!!"
        val _DB = "MithraArt"
        //val _server = "DESKTOP-4LKM9BH\\SQLEXPRESS01"
        val _server = "192.168.1.24"
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        var ConnURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            ConnURL = ("jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";")
            conn = DriverManager.getConnection(ConnURL)
            //conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.24", _user, _pass)
            if (conn == null) throw SQLException()
        } catch (se: SQLException) {
            Log.e("ERRO", se.message!!)
        } catch (e: ClassNotFoundException) {
            Log.e("ERRO", e.message!!)
        } catch (e: Exception) {
            Log.e("ERRO", e.message!!)
        }
        return conn
    }
}

// получение ID пользователя по почте
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

// получение пользовательской информации
class getUserInfo(login: String?, column: String?) : AsyncTask<String, Unit, String>() {
    val e = login
    val c = column
    var res = "nothing"
    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute();
        try {
            val connect = ConnectionHelper.CONN();
            var queryStmt = "SELECT " + c + " FROM dbo.Account WHERE Email = " + "'" + e + "'"
            val preparedStatement = connect?.prepareStatement(queryStmt);
            var resultQuery: ResultSet? = null
            resultQuery = preparedStatement?.executeQuery()
            // если в ResultSet есть строка - пользователь есть в БД
            if (resultQuery!!.next()){
                //val columns = resultQuery.getMetaData().getColumnCount();
                // получаем значение нужной нам колонки
                res = resultQuery.getString(1)
            }
            else res = "NOT FOUND"
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

// смена пароля в глобальной БД
class updatePassword(email: String?, password: String?, firstName: String? = null, lastName :String? = null) : AsyncTask<String, Unit, String>() {
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
}

// добавление пользователя
class addUser(surname: String, name: String, patronymic: String, email: String?, password: String?, phone: String?, gender: Boolean) : AsyncTask<String, Unit, String>() {
    val e = email
    val pas = password
    val n = name
    val sn = surname
    val pat = patronymic
    val ph = phone
    val g = if (gender) "1" else "0"
    //var res = "nothing"
    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute();
        try {
            val connect = ConnectionHelper.CONN();
            var queryStmt = "INSERT INTO dbo.Account (FirstName, MiddleName, LastName, " +
                    "IsActive, IsLegal, StatusId, RegistrationDate, LastLoginDate,  " +
                    "UserName, NormalizedUserName, Email, NormalizedEmail, " +
                    "EmailConfirmed, PasswordHash, PhoneNumber, PhoneNumberConfirmed, TwoFactorEnabled, LockoutEnabled, AccessFailedCount, AllowNotifications, Gender, FullName) " +
                    "VALUES ('" + n + "', '" + pat + "', '" + sn + "', 1, 0, 5, SYSDATETIME(), " +
                    "SYSDATETIME(), '" + e + "', '" + e?.uppercase() + "', '" + e + "', '" +
                    e?.uppercase() + "', 0, '" + Base64.encodeToString(pas?.toByteArray(), 0) +
                    "', '" + ph + "', 0, 0, 0, 0, 1, " + g + ", '" + sn + n + pat + "')"
            val preparedStatement = connect?.prepareStatement(queryStmt)
            preparedStatement?.executeUpdate()
            preparedStatement?.close()

        } catch (e : SQLException) {
            e.printStackTrace()
        } catch (e : Exception) {
            Log.e(TAG, "Exception. Please check your code and database.")
        }
    }
    @Deprecated("Deprecated in Java")
    override protected fun doInBackground(vararg params : String) : String? {
        return "nothing"
    }
}

// проверка на существования пользователя в БД
class checkUser(email: String?, password: String?, firstName: String? = null, lastName :String? = null) : AsyncTask<String, Unit, String>() {
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
            var queryStmt = "SELECT Email FROM dbo.Account WHERE Email = " + "'" + e + "'"
            if (e == null) {
                queryStmt = "SELECT Email FROM dbo.Account WHERE FirstName = '" + fn + "' AND LastName = '" + ln + "'"
            }
            //TODO: разобраться с хэшированием и с логикой входа с паролем
            //if (p != null) queryStmt += "AND PasswordHash = '" + p + "'"
            val preparedStatement = connect?.prepareStatement(queryStmt);
            var resultQuery: ResultSet? = null
            resultQuery = preparedStatement?.executeQuery()
            // если в ResultSet есть строка - пользователь есть в БД
            if (resultQuery!!.next()){
                res = resultQuery.getString(1)
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

// добавление физ. лица
class addPhysicalType(id: String, name: String, surname: String, patronymic: String, birthDate: String, passport: String, passDate: String, passWho: String, passResidence: String, factResidence: String, isRegistered: Boolean = false) : AsyncTask<String, Unit, String>() {
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
                        "', CAST('" + pd + "' as DATETIME), '" + pw + "', '" + pr + "', '" + fr + "')"
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

// проверка на существование физ. лица
class checkPhysicalType(id: String) : AsyncTask<String, Unit, String>() {
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

// добавление ИП
class addIPType(id: String, name: String, inn: String, ogrn: String, passport: String, passDate: String, passResidence: String, factResidence: String, isRegistered: Boolean = false) : AsyncTask<String, Unit, String>(){
    val id = id
    val pas = passport
    val n = name
    val inn = inn
    val ogrn = ogrn
    val pd = passDate
    val pr = passResidence
    val fr = factResidence
    val is_reg = isRegistered
    var queryStmt = ""
    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute();
        try {
            val connect = ConnectionHelper.CONN()
            if (is_reg){
                queryStmt = "SET DATEFORMAT YMD UPDATE dbo.IPTypes SET Name = '" + n + "', INN = '" + inn + "', OGRN = '" + ogrn + "', " +
                        "Passport = '" + pas + "', PassportDate = CAST('" + pd + "' as DATETIME), PassportResidence = '" + pr + "', FacticalResidence = '" + fr + "' WHERE Id = '" + id + "'"
            }
            else {
                queryStmt = "SET DATEFORMAT YMD INSERT INTO dbo.IPTypes(Id, Name, INN, OGRN, " +
                        "Passport, PassportDate, PassportResidence,  FacticalResidence) " +
                        "VALUES ('" + id + "', '" + n + "', '" + inn + "', '" + ogrn + "', '" + pas +
                        "', CAST('" + pd + "' as DATETIME), '" + pr + "', '" + fr + "')"
            }

            var preparedStatement = connect?.prepareStatement(queryStmt)
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

// проверка на существование ИП
class checkIPType(id: String) : AsyncTask<String, Unit, String>() {
    val id = id
    var inn = ""
    var n = ""
    var ogrn = ""
    var pas = ""
    var pd = Date()
    var pr = ""
    var fr = ""
    var res = "nothing"
    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute();
        try {
            val connect = ConnectionHelper.CONN();
            var queryStmt = "SELECT Name, INN, OGRN, Passport, " +
                    "PassportDate, PassportResidence, FacticalResidence" +
                    " FROM dbo.IPTypes WHERE Id = " + "'" + id + "'"
            val preparedStatement = connect?.prepareStatement(queryStmt);
            var resultQuery: ResultSet? = null
            resultQuery = preparedStatement?.executeQuery()
            // если в ResultSet есть строка - пользователь есть в БД
            if (resultQuery!!.next()){
                n = resultQuery.getString(1)
                inn = resultQuery.getString(2)
                ogrn = resultQuery.getString(3)
                pas = resultQuery.getString(4)
                pd = resultQuery.getDate(5)
                pr = resultQuery.getString(6)
                fr = resultQuery.getString(7)
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