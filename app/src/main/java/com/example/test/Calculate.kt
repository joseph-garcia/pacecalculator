package com.example.test


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calculate.*
import com.example.test.Model.RunningEntry
import com.example.test.Services.DataService


class Calculate : AppCompatActivity() {

    private var hourSelected : Int = 0
    private var minuteSelected : Int = 0
    private var secondSelected : Int = 0
    private var milesSelected : Int = 0
    private var milesTensSelected : Int = 0
    private var milesOnesSelected : Int = 0
    private var mileSelectedDecimals : Double = 0.0
    private var minutePace : Double = 0.0
    private var remainderSeconds : Double = 0.0
    private var goalDistance: Double = 1.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        // initialize the variables carried through from MainActivity
        intentInit()

        // Display the values passed from Pace Calculator (not actually visible until Calculate Pace is pressed)
        displayValues()


        // calculates pace and binds pace text to view
        calculatePace()



        // Back button
        backBtn.setOnClickListener {
            onBackPressed()

        }

        // SEND TO JOURNAL BUTTON
        sendButton.setOnClickListener {
            //val toast = Toast.makeText(applicationContext, "Ouch!", Toast.LENGTH_SHORT)
            //toast.show()
            val timeString = timeStringify(hourSelected, minuteSelected, secondSelected)
            val paceString = paceStringify(minutePace, remainderSeconds)
            DataService.runningEntries.add(RunningEntry(
                timeString,
                "$milesSelected miles",
                paceString,
                "Feb 17, 2020",
                "-2s",
                "7m:15s",
                "entrytemplate"))
            val toast = Toast.makeText(applicationContext, "New entry added!", Toast.LENGTH_LONG)
            toast.show()


        }
    }

    fun timeStringify(hour:Int, minute:Int, second:Int) : String {
        if (hour != 0) {
            return "${hour}h:${minute}m:${second}s"
        }
        return "${minute}m:${second}s"
    }

    fun paceStringify(minutePace:Double, secondPace:Double) : String {
        return "${minutePace.toInt()}:${secondPace.toInt()}/mi"
    }

    fun adjustedPace() {
        //Race time predictor algorithm from Pete Riegel
        //T2 = T1 x ((D2/D1)^1.06) where T1 is the given time, D1 is the given distance, D2 is the distance to predict a time for, and T2 is the calculated time for D2.
        // adjustedPace = runDurationInMinutes * ((goalMiles/milesRan)^1.06)

    }


    fun displayValues() {
        hoursNumText.text = hourSelected.toString()
        minutesNumText.text = minuteSelected.toString()
        secondsNumText.text = secondSelected.toString()
        milesNumText.text = milesSelected.toString()

        // Tack on the decimals
        mileSelectedDecimals = milesSelected.toDouble() + (milesTensSelected * 0.10) + (milesOnesSelected * 0.01)
        // and Display the miles value
        milesNumText.text = mileSelectedDecimals.toString()
    }

    fun calculatePace() {
        // get the time, convert to seconds, then divide seconds by miles
        val totalSeconds = 3600*(hourSelected) + 60*(minuteSelected) + secondSelected
        val paceBySeconds = totalSeconds / mileSelectedDecimals
        // get that number and divide by 60 to get minute pace
        minutePace = paceBySeconds / 60.0
        // use the remainder to get seconds
        //var remainderSeconds = minutePace.toInt() - (paceBySeconds / 60)
        remainderSeconds = (paceBySeconds / 60) - minutePace.toInt()
        // convert seconds in decimal form to seconds in human-readable form
        remainderSeconds = (remainderSeconds * 60)


        // print the minute and seconds
        minutePaceText.text = minutePace.toInt().toString()
        secondPaceText.text = remainderSeconds.toInt().toString()
    }

    fun intentInit() {
        hourSelected = intent.getIntExtra("hourSel", 0)
        minuteSelected = intent.getIntExtra("minuteSel", 0)
        secondSelected = intent.getIntExtra("secondSel", 0)
        milesSelected = intent.getIntExtra("mileSel", 1)
        milesTensSelected = intent.getIntExtra("mileSelTens", 0)
        milesOnesSelected = intent.getIntExtra("mileSelOnes", 0)
        val calcSelected = intent.getStringExtra("calcSel")
    }

}