package com.example.mitraartapp

import android.annotation.SuppressLint
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.SQLException


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