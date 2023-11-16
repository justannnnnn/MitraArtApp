package com.example.mitraartapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyActivityActivity : AppCompatActivity() {
    lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_activity)

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // My lots button
        var buttonMyLots = findViewById<Button>(R.id.my_lots_button)
        buttonMyLots.setOnClickListener{

        }

        // Lots with my bids button
        var buttonLotsMyBids = findViewById<Button>(R.id.lots_with_my_bids_button)
        buttonLotsMyBids.setOnClickListener{

        }

        // My sells button
        var buttonMySells = findViewById<Button>(R.id.my_sells_button)
        buttonMySells.setOnClickListener{

        }

        // My shops button
        var buttonMyShops = findViewById<Button>(R.id.my_shops_button)
        buttonMyShops.setOnClickListener{

        }


        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.account);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    val intent = Intent(this@MyActivityActivity, MainActivity::class.java)
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


                else -> {
                    true
                }
            }
        }
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.ll2,fragment)
        transaction.commit()
    }
}