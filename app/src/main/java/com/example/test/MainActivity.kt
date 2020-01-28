package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_calculate.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // create a member variable for the draw layout itself
    private var drawer: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // take draw variable, and pass toggle variable in
        drawer_layout.addDrawerListener(toggle)
        // takes care of rotating hamburger icon
        toggle.syncState()


        // Create Hour Picker
        var mainHourPicker = numberpicker_main_hours
        mainHourPicker.setMaxValue(7);
        mainHourPicker.setMinValue(0);

        // Create Minute Picker
        var mainMinutePicker = numberpicker_main_minutes
        mainMinutePicker.setMaxValue(59)
        mainMinutePicker.setMinValue(0)

        // Create Second Picker
        var mainSecondPicker = numberpicker_main_seconds
        mainSecondPicker.setMaxValue(59)
        mainSecondPicker.setMinValue(0)

        // Create Miles Picker
        var mainMilesPicker = numberpicker_main_miles
        mainMilesPicker.setMaxValue(99)
        mainMilesPicker.setMinValue(1)

        // Create Miles Decimal (Tens) Picker
        var mainMilesTensPicker = numberpicker_main_milesTens
        mainMilesTensPicker.setMaxValue(9)
        mainMilesTensPicker.setMinValue(0)

        // Create Miles Decimal (Ones) Picker
        var mainMilesOnesPicker = numberpicker_main_milesOnes
        mainMilesOnesPicker.setMaxValue(9)
        mainMilesOnesPicker.setMinValue(0)


        // Change Hour Selected from User
        var hourSelected = 0
        mainHourPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            hourSelected = newVal
        }

        // Change Minute Selected from User
        var minuteSelected = 0
        mainMinutePicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            minuteSelected = newVal
            d("minuteSel", "$minuteSelected")

        }

        // Change Second Selected from User
        var secondSelected = 0
        mainSecondPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            secondSelected = newVal
        }

        // Change Miles Selected from User
        var milesSelected = 1
        mainMilesPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            milesSelected = newVal
        }

        // Change Miles Tens Selected from User
        var milesTensSelected = 0
        mainMilesTensPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            milesTensSelected = newVal
        }

        // Change Miles Ones Selected from User
        var milesOnesSelected = 0
        mainMilesOnesPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            milesOnesSelected = newVal
        }





        calculateBtn.setOnClickListener {
            // Pass the minute selected variable to the Calculate class
            val intent = Intent(this@MainActivity, Calculate::class.java);
            intent.putExtra("hourSel", hourSelected)
            intent.putExtra("minuteSel", minuteSelected)
            intent.putExtra("secondSel", secondSelected)
            intent.putExtra("mileSel", milesSelected)
            intent.putExtra("mileSelTens", milesTensSelected)
            intent.putExtra("mileSelOnes", milesOnesSelected)
            intent.putExtra("calcSel", "PaceCalc")
            startActivity(intent)
            //startActivity(Intent(this, Calculate::class.java))
            //d("numOut", "$test1.text")
        }


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




}
