package com.example.mitraartapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog


class UserSettingsActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)


        // Back button
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // Photo button
        val photoButton = findViewById<ImageButton>(R.id.photoImageButton)
        photoButton.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.button_sheet_dialog, null)

            val makePhotoButton = view.findViewById<Button>(R.id.make_photo_button)
            val addPhotoButton = view.findViewById<Button>(R.id.add_photo_button)
            val deletePhotoButton = view.findViewById<Button>(R.id.delete_photo_button)

            makePhotoButton.setOnClickListener{

            }
            addPhotoButton.setOnClickListener{

            }
            deletePhotoButton.setOnClickListener{

            }
            // below line is use to set cancelable to avoid
            // closing of dialog box when clicking on the screen.
            dialog.setCancelable(true)

            // on below line we are setting
            // content view to our view.
            dialog.setContentView(view)

            // on below line we are calling
            // a show method to display a dialog.
            dialog.show()
        }


        // Spinners
        val mSpinnerType = findViewById<Spinner>(R.id.typeSpinner)
        val mSpinnerECP = findViewById<Spinner>(R.id.ecpSpinner)
        // Create an adapter as shown below
        val mArrayAdapterType = ArrayAdapter.createFromResource(this, R.array.type, R.layout.spinner_list)
        val mArrayAdapterECP = ArrayAdapter.createFromResource(this, R.array.ecp, R.layout.spinner_list)
        mArrayAdapterType.setDropDownViewResource(R.layout.spinner_list)
        mArrayAdapterECP.setDropDownViewResource(R.layout.spinner_list)
        // Set the adapter to the Spinner
        mSpinnerType.adapter = mArrayAdapterType
        mSpinnerECP.adapter = mArrayAdapterECP
        mSpinnerType.setSelection(3)
        mSpinnerECP.setSelection(1)

        mSpinnerType.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, itemSelected: View, selectedItemPosition: Int, selectedId: Long) {
                when(selectedItemPosition){
                    0 -> {
                        val intent = Intent(this@UserSettingsActivity, PhysicalTypeActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this@UserSettingsActivity, LegalTypeActivity::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@UserSettingsActivity, IPTypeActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.account);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    val intent = Intent(this@UserSettingsActivity, MainActivity::class.java)
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
}