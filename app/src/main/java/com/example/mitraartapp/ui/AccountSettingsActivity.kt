package com.example.mitraartapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mitraartapp.AccountFragment
import com.example.mitraartapp.CartFragment
import com.example.mitraartapp.HomeFragment
import com.example.mitraartapp.MainActivity
import com.example.mitraartapp.MoreFragment
import com.example.mitraartapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountSettingsActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
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