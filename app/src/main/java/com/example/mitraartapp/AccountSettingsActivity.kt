package com.example.mitraartapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountSettingsActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // Account setting button
        var buttonSettings = findViewById<Button>(R.id.settings_user_account_button)
        buttonSettings.setOnClickListener{
            val intent = Intent(this@AccountSettingsActivity, UserSettingsActivity::class.java)
            startActivity(intent)
        }

        // Vacation mode button
        var buttonVacationMode = findViewById<Button>(R.id.vacation_mode_button)
        buttonVacationMode.setOnClickListener{

        }

        // Watermarks button
        var buttonWatermarks = findViewById<Button>(R.id.watermarks_button)
        buttonWatermarks.setOnClickListener{

        }

        // Standard lot's description button
        var buttonLotDecription = findViewById<Button>(R.id.standart_descript_button)
        buttonLotDecription.setOnClickListener{

        }

        // Downloaded docs button
        var buttonDownloadedDocs = findViewById<Button>(R.id.downloaded_docs_button)
        buttonDownloadedDocs.setOnClickListener{

        }

        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.account);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    val intent = Intent(this@AccountSettingsActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.more -> {
                    loadFragment(MoreFragment())
                    true
                }
                R.id.cart -> {
                    loadFragment(CartFragment())
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                    finish()
                    true
                }


                else -> {true}
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.ll2,fragment)
        transaction.commit()
    }
}