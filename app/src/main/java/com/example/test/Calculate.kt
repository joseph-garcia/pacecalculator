package com.example.test

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calculate.*

class Calculate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        val hourSelected = intent.getIntExtra("hourSel", 0)
        val minuteSelected = intent.getIntExtra("minuteSel", 0)
        val secondSelected = intent.getIntExtra("secondSel", 0)
        val milesSelected = intent.getIntExtra("mileSel", 0)
        val calcSelected = intent.getStringExtra("calcSel")

        d("um", "$minuteSelected")

        hoursNumText.text = hourSelected.toString()
        minutesNumText.text = minuteSelected.toString()
        secondsNumText.text = secondSelected.toString()
        milesNumText.text = milesSelected.toString()

        d("calc", "$calcSelected")

        backBtn.setOnClickListener {
            onBackPressed()
        }

        // get the time, convert to seconds, then divide seconds by miles
        val totalSeconds = 3600*(hourSelected) + 60*(minuteSelected) + secondSelected
        val paceBySeconds = totalSeconds / milesSelected
        // get that number and divide by 60 to get minute pace
        val minutePace = paceBySeconds / 60.0
        // use the remainder to get seconds
        val remainderSeconds = minutePace - (paceBySeconds / 60)
        // convert seconds in decimal form to seconds in human-readable form

        // print the minute
        minutePaceText.text = minutePace.toInt().toString()
    }


}