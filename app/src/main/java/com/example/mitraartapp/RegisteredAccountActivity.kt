package com.example.mitraartapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class RegisteredAccountActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_account)

        // Account image view

        // Name text view
        var accountNameTextView = findViewById<TextView>(R.id.account_name)
        var dbHandler = DBHandler(this@RegisteredAccountActivity)
        accountNameTextView.text = dbHandler.getName() + " " + dbHandler.getSurname()

        // Settings button
        var buttonSettings = findViewById<Button>(R.id.account_settings_button)
        buttonSettings.setOnClickListener{
            val intent = Intent(this@RegisteredAccountActivity, AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        // Location text view
        var locationTextView = findViewById<TextView>(R.id.location_textView)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@RegisteredAccountActivity)
        locationTextView.text = getLocation()


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

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(): String? {
        var res = "NOT DEFINED"
        if (!checkPermissions()) {
            requestPermissions()
        }
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val list: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                res = list?.get(0)?.locality.toString()
                        /*mainBinding.apply {
                            tvLatitude.text = "Latitude\n${list[0].latitude}"
                            tvLongitude.text = "Longitude\n${list[0].longitude}"
                            tvCountryName.text = "Country Name\n${list[0].countryName}"
                            tvLocality.text = "Locality\n${list[0].locality}"
                            tvAddress.text = "Address\n${list[0].getAddressLine(0)}"*/
            }
        }
        return res
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

}