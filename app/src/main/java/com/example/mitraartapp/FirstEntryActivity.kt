package com.example.mitraartapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
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


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)
        // если результат != null -> пользователь уже входил в приложение через google
        // если null -> переводить на страницу с входом


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

            //val intent = Intent(this@FirstEntryActivity, VKAuthActivity::class.java)
            //startActivity(intent)
        }

        // Entry by Google button
        var buttonGoogle = findViewById<Button>(R.id.enter_by_google_button)
        buttonGoogle.setOnClickListener{
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
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
            var email = account.email
            val ch_us = checkUser(email, null)
            ch_us.execute("")
            val result = ch_us.res
            if (result.equals("REGISTERED")){
                val intent = Intent(this@FirstEntryActivity, RegisteredAccountActivity::class.java)
                startActivity(intent)
            }
            else if (result.equals("NOT REGISTERED")) {
                val intent = Intent(this@FirstEntryActivity, RegistrationActivity::class.java)
                startActivity(intent)
            }
            else throw Exception()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }


    class checkUser(email: String?, password: String?) : AsyncTask<String, Unit, String>() {
        val e = email
        val p = password
        lateinit var res : String
        @Deprecated("Deprecated in Java")
        override protected fun onPreExecute() {
            super.onPreExecute();

            /*email = edtEmailAddress.getText().toString();
            password = edtPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);*/
        }

         @Deprecated("Deprecated in Java")
         override protected fun doInBackground(vararg params : String) : String?{

            try {
                //val con = ConnectionHelper;
                val connect = ConnectionHelper.CONN();

                val queryStmt = "SELECT Email FROM dbo.Account WHERE Email = " + "'" + e + "'"
                //+ password
                //+ "','User')";

                val preparedStatement = connect?.prepareStatement(queryStmt);
                var resultQuery: ResultSet? = null

                if (preparedStatement != null) {
                    resultQuery = preparedStatement.executeQuery()
                }
                if (preparedStatement != null) {
                    preparedStatement.close()
                }
                if (resultQuery != null) {
                    if (resultQuery.cursorName != null){
                        return "REGISTERED"
                    }

                }
                return "NOT REGISTERED"

            } catch (e : SQLException) {
                e.printStackTrace();
                return e.toString();
            } catch (e : Exception) {
                return "Exception. Please check your code and database.";
            }
        }

        @Deprecated("Deprecated in Java")
        override protected fun onPostExecute(result : String) : Unit {

            //Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
            //howSnackBar(result);
            //progressBar.setVisibility(View.GONE);
            //btnSignUp.setVisibility(View.VISIBLE);S
            if (!result.equals("Exception. Please check your code and database.")) {
                res = result
            }
            else res = "ERROR"

        }
    }

}