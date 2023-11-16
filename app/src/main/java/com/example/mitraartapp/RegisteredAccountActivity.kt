package com.example.mitraartapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RegisteredAccountActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_account)

        // Settings button
        var buttonSettings = findViewById<Button>(R.id.account_settings_button)
        buttonSettings.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        //My Activity button
        var buttonMyActivity = findViewById<Button>(R.id.my_activity_button)
        buttonMyActivity.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, MyActivityActivity::class.java)
            startActivity(intent)
        }

        // Favourite button
        var buttonFavourite = findViewById<Button>(R.id.favourite_button)
        buttonFavourite.setOnClickListener{

        }

        // My finances button
        var buttonMyFinances = findViewById<Button>(R.id.my_finances_button)
        buttonMyFinances.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, MyFinancesActivity::class.java)
            startActivity(intent)
        }

        // My messages button
        var buttonMyMessages = findViewById<Button>(R.id.my_messages_button)
        buttonMyMessages.setOnClickListener{

        }

        // Delivery button
        var buttonDelivery = findViewById<Button>(R.id.delivery_button)
        buttonDelivery.setOnClickListener{

        }

        // Black list button
        var buttonBlackList = findViewById<Button>(R.id.black_list_button)
        buttonBlackList.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, BlackListActivity::class.java)
            startActivity(intent)
        }

        // Ask question button
        var buttonAsk = findViewById<Button>(R.id.ask_quest_button)
        buttonAsk.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, AskQuestionActivity::class.java)
            startActivity(intent)
        }

        // FAQ button
        var buttonFAQ = findViewById<Button>(R.id.faq_button)
        buttonFAQ.setOnClickListener{

        }

        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.account);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    val intent = Intent(this@RegisteredAccountActivity, MainActivity::class.java)
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