package com.example.mitraartapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BlackListActivity : AppCompatActivity() {
    lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_list)

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // BL of buyers button
        var buttonOfBuyers = findViewById<Button>(R.id.of_buyers_button)
        buttonOfBuyers.setOnClickListener{

        }

        // BL of sellers button
        var buttonOfSellers = findViewById<Button>(R.id.of_sellers_button)
        buttonOfSellers.setOnClickListener{

        }

        // Rate limit button
        var buttonRateLimit = findViewById<Button>(R.id.rate_limit_button)
        buttonRateLimit.setOnClickListener{

        }


        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.account);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    val intent = Intent(this@BlackListActivity, MainActivity::class.java)
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