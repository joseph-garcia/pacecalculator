package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_calculate.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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


        // Change Minute Selected from User
        var minuteSelected = 35
        mainMinutePicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            minuteSelected = newVal
            d("minuteSel", "$minuteSelected")

        }




        calculateBtn.setOnClickListener {
            // Pass the minute selected variable to the Calculate class
            val intent = Intent(this@MainActivity, Calculate::class.java);
            intent.putExtra("minuteSel", minuteSelected)
            intent.putExtra("calcSel", "PaceCalc")
            startActivity(intent)
            //startActivity(Intent(this, Calculate::class.java))
            //d("numOut", "$test1.text")
        }


    }


}
