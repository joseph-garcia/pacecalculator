package com.example.test

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calculate.*

class Calculate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        // initialize the variables carried through from MainActivity
        val hourSelected = intent.getIntExtra("hourSel", 0)
        val minuteSelected = intent.getIntExtra("minuteSel", 0)
        val secondSelected = intent.getIntExtra("secondSel", 0)
        val milesSelected = intent.getIntExtra("mileSel", 1)
        val milesTensSelected = intent.getIntExtra("mileSelTens", 0)
        val milesOnesSelected = intent.getIntExtra("mileSelOnes", 0)
        val calcSelected = intent.getStringExtra("calcSel")

        // Display the default values (not actually visible until Calculate Pace is pressed)
        hoursNumText.text = hourSelected.toString()
        minutesNumText.text = minuteSelected.toString()
        secondsNumText.text = secondSelected.toString()
        milesNumText.text = milesSelected.toString()

        // Tack on the decimals
        val mileSelectedDecimals = milesSelected.toDouble() + (milesTensSelected * 0.10) + (milesOnesSelected * 0.01)
        // and Display the miles value
        milesNumText.text = mileSelectedDecimals.toString()

        // Back button
        backBtn.setOnClickListener {
            onBackPressed()
            d("mileSelected", "$mileSelectedDecimals")
        }

        // get the time, convert to seconds, then divide seconds by miles
        val totalSeconds = 3600*(hourSelected) + 60*(minuteSelected) + secondSelected
        val paceBySeconds = totalSeconds / mileSelectedDecimals
        // get that number and divide by 60 to get minute pace
        val minutePace = paceBySeconds / 60.0
        // use the remainder to get seconds
        //var remainderSeconds = minutePace.toInt() - (paceBySeconds / 60)
        var remainderSeconds = (paceBySeconds / 60) - minutePace.toInt()
        // convert seconds in decimal form to seconds in human-readable form
        remainderSeconds = (remainderSeconds * 60)


        // print the minute and seconds
        minutePaceText.text = minutePace.toInt().toString()
        secondPaceText.text = remainderSeconds.toInt().toString()
    }


}