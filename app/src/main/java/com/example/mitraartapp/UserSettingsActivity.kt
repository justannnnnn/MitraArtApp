package com.example.mitraartapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UserSettingsActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    lateinit var photoImageView: ShapeableImageView
    lateinit var deletePhotoButton: Button
    lateinit var view: View


    var hasPhoto = false
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 12
    private lateinit var mCurrentPhotoPath: String
    private val IMAGE_DIRECTORY_NAME = "MITRA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        var dbHandler = DBHandler(this@UserSettingsActivity)
        hasPhoto = dbHandler.getImage() != ""

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
            photoImageView.visibility = View.VISIBLE
            var dbHandler = DBHandler(this@UserSettingsActivity)
            var photoFileString = dbHandler.getImage()
            downloadAvatar(photoFileString)
        }
        else photoLayout.visibility = View.VISIBLE
        photoImageView.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            view = layoutInflater.inflate(R.layout.button_sheet_dialog, null)

            val makePhotoButton = view.findViewById<Button>(R.id.make_photo_button)
            val addPhotoButton = view.findViewById<Button>(R.id.add_photo_button)
            deletePhotoButton = view.findViewById<Button>(R.id.delete_photo_button)
            if (!hasPhoto) deletePhotoButton.visibility = View.INVISIBLE

            makePhotoButton.setOnClickListener{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage()
                } else {
                    captureImage2()
                }
            }
            addPhotoButton.setOnClickListener{
                val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                changeImage.launch(pickImg)
            }
            deletePhotoButton.setOnClickListener{
                dbHandler.setImage("")
                hasPhoto = false
                this.onResume()

            }
            dialog.setCancelable(true) // avoid closing of dialog box when clicking on the screen
            dialog.setContentView(view) // setting content view to our view
            dialog.show()
        }

        // Photo button
        val photoButton = findViewById<ImageButton>(R.id.photoImageButton)
        photoButton.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            view = layoutInflater.inflate(R.layout.button_sheet_dialog, null)

            val makePhotoButton = view.findViewById<Button>(R.id.make_photo_button)
            val addPhotoButton = view.findViewById<Button>(R.id.add_photo_button)
            deletePhotoButton = view.findViewById<Button>(R.id.delete_photo_button)
            if (!hasPhoto) deletePhotoButton.visibility = View.INVISIBLE

            makePhotoButton.setOnClickListener{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage()
                } else {
                    captureImage2()
                }
            }
            addPhotoButton.setOnClickListener{
                val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                changeImage.launch(pickImg)
            }
            deletePhotoButton.setOnClickListener{
                dbHandler.setImage("")
                hasPhoto = false
                this.onResume()
            }
            dialog.setCancelable(true) // avoid closing of dialog box when clicking on the screen
            dialog.setContentView(view) // setting content view to our view
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
        // получили первый раз с камеры
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.getAbsolutePath())
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
            var dbHandler = DBHandler(this@UserSettingsActivity)
            dbHandler.setImage(photoFile!!.absolutePath)
            hasPhoto = true
            this.onResume()
        } else {
            //displayMessage(baseContext, "Request cancelled or something went wrong.")
        }
    }


    override fun onResume() {
        super.onResume()
        val photoLayout = findViewById<LinearLayout>(R.id.ll1)
        if (hasPhoto) {
            photoLayout.visibility = View.INVISIBLE
            photoImageView.visibility = View.VISIBLE
            var dbHandler = DBHandler(this@UserSettingsActivity)
            var photoFileString = dbHandler.getImage()
            downloadAvatar(photoFileString)
        }
        else {
            photoLayout.visibility = View.VISIBLE
            photoImageView.visibility = View.INVISIBLE
            val view = layoutInflater.inflate(R.layout.button_sheet_dialog, null)
            val deletePhotoButton = view.findViewById<Button>(R.id.delete_photo_button)
            deletePhotoButton.visibility = View.INVISIBLE
        }
    }


    companion object {
        private const val pic_id = 123
    }

    // начало функций для камеры
    private fun captureImage2() {

        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = createImageFile4()
            if (photoFile != null) {
                //displayMessage(baseContext, photoFile!!.getAbsolutePath())
                Log.i("Mayank", photoFile!!.getAbsolutePath())
                val photoURI = Uri.fromFile(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST)
            }
        } catch (e: Exception) {
            displayMessage(baseContext, "Camera is not available." + e.toString())
        }

    }

    private fun captureImage() {

        if (ContextCompat.checkSelfPermission(this@UserSettingsActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@UserSettingsActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //if (takePictureIntent.resolveActivity(packageManager) != null) {
            if (getApplicationContext().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)){
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile()
                    //displayMessage(baseContext, photoFile!!.getAbsolutePath())
                    Log.i("Mayank", photoFile!!.getAbsolutePath())

                    // Continue only if the File was successfully created
                    if (photoFile != null) {


                        var photoURI = FileProvider.getUriForFile(this,
                            "com.example.mitraartapp.fileprovider",
                            photoFile!!
                        )

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)

                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext,"Capture Image Bug: "  + ex.message.toString())
                }


            } else {
                displayMessage(baseContext, "Nullll")
            }
        }
    }

    private fun createImageFile4(): File? {
        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY_NAME)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(baseContext, "Unable to create directory.")
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(Date())

        return File(mediaStorageDir.path + File.separator
                + "IMG_" + timeStamp + ".jpg")

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    // конец функций для камеры

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            }
        }

    }

    // onActivityResult для выбора из галереи
    val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                var dbHandler = DBHandler(this@UserSettingsActivity)
                dbHandler.setImage(imgUri.toString())
                val photoLayout = findViewById<LinearLayout>(R.id.ll1)
                photoLayout.visibility = View.INVISIBLE

                val resolver = contentResolver
                val cursor = imgUri?.let { it1 -> resolver.query(it1, null, null, null, null) }
                val dataIndex = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.moveToFirst()
                val imagePath = cursor.getString(dataIndex)
                val myBitmap = BitmapFactory.decodeFile(imagePath)
                cursor.close()

                photoImageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
            }
        }

    // функция для обработки пути из GooglePhotos
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
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
        }
        else if ("files" in photoFileString){
            val myBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(File(photoFileString)))
            photoImageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, false))
        }
    }
}
