package com.example.mitraartapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import me.bush.translator.Language
import me.bush.translator.Translator
import java.io.File
import java.util.Locale


class RegisteredAccountActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    lateinit var locationManager: LocationManager
    lateinit var locationTextView: TextView
    lateinit var imageView: ShapeableImageView
    var hasPhoto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_account)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager;
        var dbHandler = DBHandler(this@RegisteredAccountActivity)


        // Account image view
        imageView = findViewById<ShapeableImageView>(R.id.account_image)
        imageView.setShapeAppearanceModel(imageView.getShapeAppearanceModel().toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED,120f)
            .setTopRightCorner(CornerFamily.ROUNDED,120f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,120f)
            .setBottomRightCorner(CornerFamily.ROUNDED,120f)
            .build())
        val photoBlob = dbHandler.getImage()
        if (photoBlob != ""){
            hasPhoto = true
            var dbHandler = DBHandler(this@RegisteredAccountActivity)
            var photoFileString = dbHandler.getImage()
            downloadAvatar(photoFileString)
        }
        else {
            imageView.setBackgroundColor(resources.getColor(R.color.grey))
            imageView.setImageBitmap(null)
        }


        // Name text view
        var accountNameTextView = findViewById<TextView>(R.id.account_name)
        accountNameTextView.text = dbHandler.getName() + " " + dbHandler.getSurname()

        // Settings button
        var buttonSettings = findViewById<Button>(R.id.account_settings_button)
        buttonSettings.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        // Location text view
        locationTextView = findViewById<TextView>(R.id.location_textView)


        //My Activity button
        var buttonMyActivity = findViewById<Button>(R.id.my_activity_button)
        buttonMyActivity.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, MyActivityActivity::class.java)
            startActivity(intent)
        }

        // Favourite button
        var buttonFavourite = findViewById<Button>(R.id.favourite_button)
        buttonFavourite.setOnClickListener {

        }

        // My finances button
        var buttonMyFinances = findViewById<Button>(R.id.my_finances_button)
        buttonMyFinances.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, MyFinancesActivity::class.java)
            startActivity(intent)
        }

        // My messages button
        var buttonMyMessages = findViewById<Button>(R.id.my_messages_button)
        buttonMyMessages.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, MyMessagesActivity::class.java)
            startActivity(intent)
        }

        // Delivery button
        var buttonDelivery = findViewById<Button>(R.id.delivery_button)
        buttonDelivery.setOnClickListener {

        }

        // Black list button
        var buttonBlackList = findViewById<Button>(R.id.black_list_button)
        buttonBlackList.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, BlackListActivity::class.java)
            startActivity(intent)
        }

        // Ask question button
        var buttonAsk = findViewById<Button>(R.id.ask_quest_button)
        buttonAsk.setOnClickListener {
            val intent = Intent(this@RegisteredAccountActivity, AskQuestionActivity::class.java)
            startActivity(intent)
        }

        // FAQ button
        var buttonFAQ = findViewById<Button>(R.id.faq_button)
        buttonFAQ.setOnClickListener {

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
    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (10).toLong(), (10).toFloat(), locationListener)
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10 as Long, 10.0, locationListener)
        checkEnabled()
        var dbHandler = DBHandler(this@RegisteredAccountActivity)
        val photoBlob = dbHandler.getImage()
        if (photoBlob != ""){
            hasPhoto = true
            var dbHandler = DBHandler(this@RegisteredAccountActivity)
            var photoFileString = dbHandler.getImage()
            downloadAvatar(photoFileString)
        }
        else {
            imageView.setBackgroundColor(resources.getColor(R.color.grey))
            imageView.setImageBitmap(null)
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val dbHandler = DBHandler(this@RegisteredAccountActivity)
        val photoBlob = dbHandler.getImage()
        if (photoBlob != ""){
            hasPhoto = true
            downloadAvatar(photoBlob)
        }
        else { imageView.setBackgroundColor(resources.getColor(R.color.grey))
                imageView.setImageBitmap(null)}
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            showLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            checkEnabled()
        }

        override fun onProviderEnabled(provider : String) {
            checkEnabled()
            if (ActivityCompat.checkSelfPermission(
                    this@RegisteredAccountActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@RegisteredAccountActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager?.getLastKnownLocation(provider)?.let { showLocation(it) }
        }

        override fun onStatusChanged(provider : String, status : Int, extras : Bundle) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
               // locationTextView.setText("Status: " + String.valueOf(status))
            }
        }
    }

    private fun showLocation(location : Location) {
        if (location == null)
            return
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            locationTextView.setText(formatLocation(location))
        }
    }

    private fun formatLocation(location : Location) : String?{
        if (location == null)
            return ""
        val translator = Translator()
        return getCityName(location.latitude, location.longitude)?.let {
            translator.translateBlocking(
                it, Language.RUSSIAN, Language.ENGLISH).translatedText
        }
    }

    private fun checkEnabled() {
        //locationTextView.setText("Enabled: "
         //       + locationManager
        //.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }


    private fun getCityName(lat: Double,long: Double):String?{
        val cityName: String?
        val geoCoder = Geocoder(this@RegisteredAccountActivity, Locale.getDefault())
        val Address = geoCoder.getFromLocation(lat,long,3)
        cityName = Address?.get(0)?.adminArea
        return cityName
    }

    fun removeUnwantedString(path: String): String {
        //pathUri = "content://com.google.android.apps.photos.contentprovider/-1/2/content://media/external/video/media/5213/ORIGINAL/NONE/2106970034"
        var pathUri = path
        pathUri = pathUri.replace("%3A", ":")
        pathUri = pathUri.replace("%2F", "/")
        val d1 = pathUri.split("content://".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (item1 in d1) {
            if (item1.contains("media/")) {
                val d2 = item1.split("/ORIGINAL/".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                for (item2 in d2) {
                    if (item2.contains("media/")) {
                        pathUri = "content://$item2"
                        break
                    }
                }
                break
            }
        }
        //pathUri = "content://media/external/video/media/5213"
        return pathUri
    }
    fun downloadAvatar(photoFileStr: String){
        var photoFileString = photoFileStr
        // если получили из GooglePhotos
        if ("com.google.android.apps.photos.contentprovider" in photoFileString) {
            photoFileString = removeUnwantedString(photoFileString)
            val resolver = contentResolver
            val cursor = Uri.parse(photoFileString).let { it1 -> resolver.query(it1, null, null, null, null) }
            val dataIndex = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.moveToFirst()
            val imagePath = cursor.getString(dataIndex)
            val myBitmap = BitmapFactory.decodeFile(imagePath)
            cursor.close()
            imageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
        }
        else if ("files" in photoFileString){
            val myBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(File(photoFileString)))
            imageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
        }
    }
}
