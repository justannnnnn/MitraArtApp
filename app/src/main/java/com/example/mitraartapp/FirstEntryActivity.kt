package com.example.mitraartapp

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import java.sql.ResultSet
import java.sql.SQLException


class FirstEntryActivity : AppCompatActivity() {

    private final var VK_APP_ID = 51794395
    val VK_SCOPES = arrayOf<String>(
        VKScope.FRIENDS,
        VKScope.MESSAGES,
        VKScope.NOTIFICATIONS,
        VKScope.OFFLINE,
        VKScope.STATUS,
        VKScope.STATS,
        VKScope.PHOTOS,
        VKScope.EMAIL
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_entry)

        VKSdk.customInitialize(this@FirstEntryActivity,VK_APP_ID, "5.52")

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
            VKSdk.login(this, VKScope.FRIENDS,
                VKScope.MESSAGES,
                VKScope.NOTIFICATIONS,
                VKScope.OFFLINE,
                VKScope.STATUS,
                VKScope.STATS,
                VKScope.PHOTOS,
                VKScope.EMAIL)

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
        else{
            VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken>{
                override fun onResult(res: VKAccessToken?) {
                    val request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id, first_name, last_name"))
                    request.secure = false
                    request.useSystemLanguage = false
                    request.executeWithListener(mRequestListener)
                }

                override fun onError(error: VKError?) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    val mRequestListener = object: VKRequest.VKRequestListener(){
        override fun onComplete(response: VKResponse?) {
            super.onComplete(response)
            // перехожу в нестандартный активити для проверки
            val intent = Intent(this@FirstEntryActivity, AskQuestionActivity::class.java)
            startActivity(intent)
        }

        override fun attemptFailed(request: VKRequest?, attemptNumber: Int, totalAttempts: Int) {
            super.attemptFailed(request, attemptNumber, totalAttempts)
            Toast.makeText(this@FirstEntryActivity, "Failed " + totalAttempts, Toast.LENGTH_SHORT).show()

        }

        override fun onError(error: VKError?) {
            super.onError(error)
            Toast.makeText(this@FirstEntryActivity, "Failed " + error.toString(), Toast.LENGTH_SHORT).show()
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
                val dbHandler = DBHandler(this@FirstEntryActivity)
                // TODO: не факт, что хэш код подходит - тестовая версия
                dbHandler.clearTable()
                dbHandler!!.addNewAccount(email, "", account.givenName, account.familyName)
                val flag = dbHandler!!.tableExists()
                val intent = Intent(this@FirstEntryActivity, RegistrationActivity::class.java)
                startActivity(intent)
            }
            else throw Exception()
        } catch (e: Exception) {
            Toast.makeText(this,"signInResult:failed code= " + e.message, Toast.LENGTH_SHORT).show();
        }
    }

    // class for checking is user's info in MS SQL base
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


}