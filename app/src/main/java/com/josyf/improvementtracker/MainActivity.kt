package com.josyf.improvementtracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.josyf.improvementtracker.db.ImageURI
import com.josyf.improvementtracker.db.ImageURIDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController:NavController
    private lateinit var navigationView : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //we need a reference to our navigation view
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // take draw variable, and pass toggle variable in
        drawer.addDrawerListener(toggle)
        // takes care of rotating hamburger icon
        toggle.syncState()

        val navHeader = navigationView.getHeaderView(0)
        val imageButtonFromNavHeader = navHeader.image_view
        showImageFromDb(imageButtonFromNavHeader)


        // show / listen for name and name changes
        val pref = getPreferences(Context.MODE_PRIVATE)
        val name = pref.getString("NAME", "")
        navHeader.editNameText.setText(name)


        navHeader.editNameText.onSubmit {
            if (editNameText.text.length > 16) {
                Toast.makeText(applicationContext, "16 character limit! Please try again.", Toast.LENGTH_SHORT).show()
                navHeader.editNameText.setText(name)
                return@onSubmit
            } else {
                submit()
            }
        }



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                JournalFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.ic_journal)
        }

        println("on create: $image_view")

    }



    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.ic_pace -> {
                navController.navigate(R.id.paceCalcFragment)
            }
            R.id.ic_journal -> {
                navController.navigate(R.id.loopingFragment)
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    // when we press the back btn when drawer is open, we dont want to leave the activity--instead we want to close nav drawer
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun clickImage(view: View) {
        // if OS >= marshmallow, request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                // show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission granted
                pickImageFromGallery()
            }
        } else {
            // OS is < marshmallow
            pickImageFromGallery()
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun pickImageFromGallery() {

        // Intent to pick image
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun showImageFromDb(imgButton: ImageButton) {
        GlobalScope.launch {
            baseContext.let {
                val imageList = ImageURIDatabase(applicationContext).ImageDAO().getAllEntries()
                if (imageList.isNotEmpty()) {
                    val imageItem = imageList[0]

                    Handler(Looper.getMainLooper()).post {
                        try {
                            imgButton.setImageURI(imageItem.imageAddress.toUri())
                        } catch (e: SecurityException) {
                            imgButton.setImageResource(R.drawable.duck)
                        }

                    }

                    println(imageItem.imageAddress)
                } else {
                    imgButton.setImageResource(R.drawable.duck)
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup granted
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if user selects a photo, launch the async task
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            GlobalScope.launch {
                data?.let {

                    // bind the image data to a URI string
                    val imageData = data.data
                    val imageURI = ImageURI(imageData!!.toString())

                    val takeFlags : Int = data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    this@MainActivity.contentResolver!!.takePersistableUriPermission(imageData, takeFlags)

                    // get syntax for calling dao functions
                    val imageDB = ImageURIDatabase(applicationContext).ImageDAO()

                    // make var for imageURI list
                    val imageList = imageDB.getAllEntries()

                    // if an element already exists in the list,
                    if (imageList.isNotEmpty()) {
                        // replace first element in list with our new imageURI
                        imageList[0].imageAddress = imageURI.imageAddress

                        // update the list
                        imageDB.updateEntry(imageList[0])
                    } else {
                        //add to the list
                        imageDB.addEntry(imageURI)
                    }
                    Handler(Looper.getMainLooper()).post {
                        image_view.setImageURI(imageURI.imageAddress.toUri())
                    }
                }
            }
        }
    }

    private fun EditText.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                func()
            }
            true
        }
    }

    private fun submit() {
        // create shared preferences
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()

        // save name
        editor.putString("NAME", editNameText.text.toString())

        // commit changes
        editor.apply()

        Toast.makeText(applicationContext, "Saved ${editNameText.text}", Toast.LENGTH_SHORT).show()
        editNameText.clearFocus()
        nav_view.hideKeyboard()


    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}


fun formatDifferenceValue(diffValue:Int) : String {
    var timeDiffString = diffValue.toString()
    if (diffValue > 0) {
        timeDiffString = "+$timeDiffString"
    }
    timeDiffString = "${timeDiffString}s"
    return timeDiffString
}


