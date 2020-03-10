package com.josyf.improvementtracker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
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



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                JournalFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.ic_journal)
        }


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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            GlobalScope.launch {
                data?.let {
                    val imageData = data.data
                    val imageURI = ImageURI(imageData!!.toString())
                    val imageDB = ImageURIDatabase(baseContext).ImageDAO()
                    imageDB.addEntry(imageURI)
                    Handler(Looper.getMainLooper()).post(Runnable {
                        image_view.setImageURI(imageURI.imageAddress.toUri())
                    })
                }
            }
        }

    }


}
