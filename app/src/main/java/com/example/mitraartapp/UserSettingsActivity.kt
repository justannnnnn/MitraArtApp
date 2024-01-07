package com.example.mitraartapp

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import java.io.ByteArrayOutputStream


class UserSettingsActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    lateinit var photoImageView: ShapeableImageView
    var hasPhoto = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        var dbHandler = DBHandler(this@UserSettingsActivity)
        hasPhoto = dbHandler.getImage().size != 0

        // Back button
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        // Аватарка(форма ImageView)
        photoImageView = findViewById<ShapeableImageView>(R.id.photoImageView)
        photoImageView.setShapeAppearanceModel(photoImageView.getShapeAppearanceModel().toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED,120f)
            .setTopRightCorner(CornerFamily.ROUNDED,120f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,120f)
            .setBottomRightCorner(CornerFamily.ROUNDED,120f)
            .build())
        val photoLayout = findViewById<LinearLayout>(R.id.ll1)
        if (hasPhoto) {
            photoLayout.visibility = View.INVISIBLE
            val photoBlob = dbHandler.getImage()
            val photo = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size)
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(photo, 150, 150, false))
        }

        // Photo button
        val photoButton = findViewById<ImageButton>(R.id.photoImageButton)
        photoButton.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.button_sheet_dialog, null)

            val makePhotoButton = view.findViewById<Button>(R.id.make_photo_button)
            val addPhotoButton = view.findViewById<Button>(R.id.add_photo_button)
            val deletePhotoButton = view.findViewById<Button>(R.id.delete_photo_button)
            deletePhotoButton.visibility = View.INVISIBLE

            makePhotoButton.setOnClickListener{
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, pic_id)
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
        bottomNav.setSelectedItemId(R.id.account)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            val photo = data!!.extras!!["data"] as Bitmap
            photo
            // Set the image in imageview for display
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(photo, 150, 150, false))
            //photoImageView.setImageBitmap(photo)
            hasPhoto = true
            val stream = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 0, stream)
            var dbHandler = DBHandler(this@UserSettingsActivity)
            dbHandler.setImage(stream.toByteArray())
        }
    }



    override fun onResume() {
        super.onResume()
        val photoLayout = findViewById<LinearLayout>(R.id.ll1)
        if (hasPhoto) {
            photoLayout.visibility = View.INVISIBLE
            var dbHandler = DBHandler(this@UserSettingsActivity)
            val photoBlob = dbHandler.getImage()//Convert blob to bytearray
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size - 1, options)
            val photo = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size - 1)
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, false))
        }
    }

    companion object {
        private const val pic_id = 123
    }
}