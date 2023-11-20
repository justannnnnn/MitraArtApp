package com.example.mitraartapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHandler  // creating a constructor for our database handler.
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LOGIN_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)")

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query)
    }

    // this method is use to add new course to our sqlite database.
    fun addNewAccount(
        accountLogin: String?,
        accountPassword: String?
    ) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase

        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(LOGIN_COL, accountLogin)
        values.put(PASSWORD_COL, accountPassword)

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values)

        // at last we are closing our
        // database after adding database.
        db.close()
    }

    @SuppressLint("Range")
    fun tableExists(): Boolean {
        val db = this.readableDatabase
        var cnt = 0
        val cursor = db.rawQuery("SELECT * FROM " + DBHandler.TABLE_NAME, null)
        while (cursor.moveToNext()){
            cnt += 1
        }
        return cnt > 0


        /*if (db == null || !db.isOpen || TABLE_NAME == null) {
            return false
        }
        var count = 0
        val args = arrayOf("table", TABLE_NAME)
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM sqlite_master WHERE type=? AND name=?",
            args,
            null
        )
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count > 0*/
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "localdb"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val TABLE_NAME = "accounts"

        // below variable is for our id column.
        private const val ID_COL = "id"

        // below variable is for login column
        private const val LOGIN_COL = "login"

        // below variable is for password column.
        private const val PASSWORD_COL = "password"

    }
}