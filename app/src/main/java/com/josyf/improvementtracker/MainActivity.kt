package com.josyf.improvementtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // create a member variable for the draw layout itself
    //val drawer:DrawerLayout = TODO()
    //val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = NavHostFragment.findNavController(LoopingFragment())

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //we need a reference to our navigation view
        // NavigationView navigationView = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(
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
                LoopingFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.ic_row)
        }


    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.ic_row -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    CalculateFragment()).commit()
            }
            R.id.ic_loop -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    LoopingFragment()).commit()
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

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(
//            Navigation.findNavController(this, R.id.fragment),
//            null
//        )
//    }


}
