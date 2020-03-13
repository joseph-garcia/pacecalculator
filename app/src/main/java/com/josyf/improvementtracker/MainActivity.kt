package com.josyf.improvementtracker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
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
        //val navView = findViewById<NavigationView>(R.id.nav_view)



        // Check if we running android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // call some material design APIs here
        } else { // for below api 21
            // implement this feature without material design
        }



        navController = Navigation.findNavController(this, R.id.fragment)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //we need a reference to our navigation view
        // NavigationView navigationView = findViewById(R.id.nav_view)
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val navHeaderView = findViewById<ImageButton>(R.id.image_view)

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

        //println("nav view is hopefully here:  ${navigationView.nav_view}")
//        println("image view is hopefully here: ${navigationView.getHeaderView(0).image_view}")
//        val imageView = navigationView.getHeaderView(0).image_view

        val navHeader = navigationView.getHeaderView(0)
        val imageButtonFromNavHeader = navHeader.image_view
        showImageFromDb(imageButtonFromNavHeader)










        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                JournalFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.ic_journal)
        }

        println("on create: $image_view")

    }


    override fun onStart() {
        super.onStart()


//        val img = findViewById<ImageButton>(R.id.image_view)
//        //img.setImageResource(R.drawable.duck)
//        img.setImageResource(R.drawable.duck)



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
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    fun clickImage(view : View) {
        // if OS >= marshmallow, request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
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

    fun pickImageFromGallery() {

        // Intent to pick image
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun showImageFromDb(imgButton: ImageButton) {
        GlobalScope.launch {
            baseContext.let {
                val imageList = ImageURIDatabase(applicationContext).ImageDAO().getAllEntries()



                if (imageList.isNotEmpty()) {
                    val imageItem = imageList[0]

                    Handler(Looper.getMainLooper()).post(Runnable {
                        imgButton.setImageURI(imageItem.imageAddress.toUri())
                    })

                    println(imageItem.imageAddress)
                    println("not empty")
                    println("image list is : $imageList")
                } else {
                    imgButton.setImageResource(R.drawable.duck)
                }
            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    Handler(Looper.getMainLooper()).post(Runnable {
                        image_view.setImageURI(imageURI.imageAddress.toUri())
                    })

                    val uri : Uri = imageURI.imageAddress.toUri()
                    /////////////////////////////////////////////////////////////////////////////////

                }

            }

        }


    }


}
