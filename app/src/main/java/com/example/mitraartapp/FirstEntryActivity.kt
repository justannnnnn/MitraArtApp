package com.example.mitraartapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class FirstEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_entry)

        // Close button
        var buttonClose = findViewById<ImageButton>(R.id.close_button)
        buttonClose.setOnClickListener{
            finish()
        }

        // Entry by login button
        var buttonLogin = findViewById<Button>(R.id.enter_by_login_button)
        buttonLogin.setOnClickListener{
            val intent = Intent(this@FirstEntryActivity, LoginEntryActivity::class.java)
            startActivity(intent)
        }

        // Entry by VK button
        var buttonVK = findViewById<Button>(R.id.enter_by_vk_button)
        buttonVK.setOnClickListener{

        }

        // Entry by Google button
        var buttonGoogle = findViewById<Button>(R.id.enter_by_google_button)
        buttonGoogle.setOnClickListener{
            // objects for Google ID API
            // https://developers.google.com/identity/sign-in/android/sign-in#configure_google_sign-in_and_the_googlesigninclient_object
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            val account = GoogleSignIn.getLastSignedInAccount(this)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1)
        }

        // Create account button
        var buttonCreateAccount = findViewById<Button>(R.id.create_account_button)
        buttonCreateAccount.setOnClickListener{
            val intent = Intent(this@FirstEntryActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val email = account.email
            val checkUserObj = checkUser(email, null)
            checkUserObj.execute("")
            var result = checkUserObj.res
            // user's info is in DB
            if (result.equals("REGISTERED")){
                val intent = Intent(this@FirstEntryActivity, RegisteredAccountActivity::class.java)
                startActivity(intent)
            }
            // user's info isn't in DB
            else if (result.equals("NOT REGISTERED")) {
                val intent = Intent(this@FirstEntryActivity, RegistrationActivity::class.java)
                startActivity(intent)
            }
            else throw Exception()
        } catch (e: Exception) {
            Toast.makeText(this,"signInResult:failed code= " + e.message, Toast.LENGTH_SHORT).show();
        }
    }

    // class for checking is user's info in DB
    class checkUser(email: String?, password: String?) : AsyncTask<String, Unit, String>() {
        val e = email
        val p = password
        var res = "nothing"
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute();
            try {
                val connect = ConnectionHelper.CONN();
                var queryStmt = "SELECT Email FROM dbo.Account WHERE Email = " + "'" + e + "'"
                //TODO: разобраться с хэшированием и с логикой входа с паролем
                //if (p != null) queryStmt += "AND PasswordHash = '" + p + "'"
                val preparedStatement = connect?.prepareStatement(queryStmt);
                var resultQuery: ResultSet? = null
                resultQuery = preparedStatement?.executeQuery()
                // если в ResultSet есть строка - пользователь есть в БД
                    if (resultQuery!!.next()){
                        res = "REGISTERED"
                    }
                res = "NOT REGISTERED"
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

}