package com.josyf.improvementtracker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.db.EntryDatabase
import com.josyf.improvementtracker.services.BaseFragment
import kotlinx.android.synthetic.main.activity_calculate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.roundToInt

// The logic behind the main Pace Calculator xml view.
class CalcResultsFragment : BaseFragment() {

    // Each variable corresponds to differing numerical values from the view.
    // It's separated like this, because it's separated in the view as well.
    private var monthSelected : String = ""
    private var daySelected : Int = 0
    private var yearSelected : Int = 0

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

    private val args: CalcResultsFragmentArgs by navArgs()

    //this happens second (after onCreate)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_calculate, container, false)
    }

    //this happens 3rd
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // initialize the variables carried through from MainActivity
        getArgs()

        // Display the values passed from PaceCalcFragment
        displayValues()

        // calculates pace and binds pace text to view
        val totalSeconds = 3600*(hourSelected) + 60*(minuteSelected) + secondSelected
        val paceText = getPaceStringFromSeconds(totalSeconds)
        paceStringText.text = "$paceText/mi"


        // SEND TO JOURNAL BUTTON
        sendButton.setOnClickListener {view ->

            val timeString = timeStringify(hourSelected, minuteSelected, secondSelected)
            val distanceString = distanceStringify(milesSelected, milesTensSelected, milesOnesSelected)
            val dateString = dateStringify(monthSelected, daySelected, yearSelected)
            val adjustedTimeInSeconds: Int = adjustedPaceInSeconds(hourSelected, minuteSelected, secondSelected, milesSelected, milesTensSelected, milesOnesSelected, goalDistance)
            val adjustedTime: String = getPaceFromSeconds(adjustedTimeInSeconds)


            launch {
                context?.let{

                    val testEntry = Entry(timeString, distanceString,
                        paceText, dateString, adjustedTimeInSeconds,adjustedTime,"n/a")
                    getTimeDifference(testEntry)
                    val dbAccess = EntryDatabase(it).entryDao()
                    dbAccess.addEntry(testEntry)
                    val toast = Toast.makeText(context, "New entry added!", Toast.LENGTH_LONG)
                    toast.show()

                    // navigate to the entry log
                    val action = CalcResultsFragmentDirections.toEntryLog()
                    Navigation.findNavController(view).navigate(action)

                }
            }
        }

    }


    // test new timeDiff func
    private suspend fun getTimeDifference(entry: Entry) {
        // initialize timeDiff string and differenceValue int
        var timeDiffString : String
        var differenceValue : Int
        // if isEmpty, keep timeDiffString as ""
        withContext(Dispatchers.Default) {
            context?.let {
                val entryDB = EntryDatabase(it).entryDao()
                val entryList = entryDB.getAllEntries()
                // if isEmpty, keep timeDiffString as ""
                if (entryList.isEmpty()) {
                    entry.timeDifference = "--"
                } else { //if not, check the timeDiff value of the first item in the list
                    val lastItem = entryList[0]
                    // calculate the timeDiff
                    differenceValue = entry.adjustedTimeInSeconds - lastItem.adjustedTimeInSeconds
                    timeDiffString = formatDifferenceValue(differenceValue)
                    entry.timeDifference = timeDiffString
                }
            }
        }
        EntryDatabase(context!!).entryDao().updateEntry(entry)
    }

    private fun dateStringify(month: String, day: Int, year: Int) : String {
        return "$month $day, $year"
    }

    private fun timeStringify(hour:Int, minute:Int, second:Int) : String {
        if (hour != 0) {
            return "${hour}h:${minute}m:${second}s"
        }
        return "${minute}m:${second}s"
    }

    private fun timeNumify(hour:Int, minute:Int, second:Int) : String {
        var minuteString : String = minute.toString()
        var secondString : String = second.toString()
        if (minute < 10) {
            minuteString = "0$minuteString"
        }
        if (second < 10) {
            secondString = "0$secondString"
        }
        if (hour != 0) {
            return "$hour:$minuteString:$secondString"
        }
        return "$minute:$second"
    }

    private fun paceStringify(minutePace:Double, secondPace:Double) : String {
        return if (secondPace < 10) {
            "${minutePace.toInt()}:0${secondPace.toInt()}"
        } else "${minutePace.toInt()}:${secondPace.toInt()}"
    }

    private fun distanceStringify(miles:Int, milesTens:Int, milesOnes:Int) : String {
        return "$miles.$milesTens$milesOnes miles"
    }

    private fun getPaceFromSeconds(seconds:Int) : String {
        // Get raw decimal value (ie 90 seconds >> 1.5)
        val minuteDouble = (seconds/60.0)
        // Get floor of the minute value to return (ie 1.5 >> 1)
        val minuteValue = minuteDouble.toInt()
        // Get the remainder (the decimal we need to convert to seconds, ie 1.5 - 1 = 0.5)
        val secondDouble = minuteDouble - minuteValue
        // Then actually convert to seconds
        val secondValue = secondDouble * 60
        return paceStringify(minuteValue.toDouble(), secondValue)
    }

    private fun adjustedPaceInSeconds(hours: Int, minutes:Int, seconds:Int, miles:Int, milesTens:Int, milesOnes: Int, goalMiles:Double) : Int {
        //Race time predictor algorithm from Pete Riegel
        //T2 = T1 x ((D2/D1)^1.06) where T1 is the given time, D1 is the given distance, D2 is the distance to predict a time for, and T2 is the calculated time for D2.
        // adjustedPace = runDurationInMinutes * ((goalMiles/milesRan)^1.06)
        val milesRan:Double = miles + (milesTens * 0.10) + (milesOnes * 0.01)
        val runDurationInMinutes:Double = (hours * 60) + minutes + (seconds/60.0)
        val adjustedPace:Double = runDurationInMinutes * ((goalMiles/milesRan).pow(1.06))
        val minuteValue = adjustedPace.toInt()
        val secondsValue = ((adjustedPace - minuteValue) * 60).roundToInt()
        return (minuteValue * 60) + secondsValue
    }


    private fun displayValues() {

        dateText.text = "$monthSelected $daySelected, $yearSelected"
        totalTimeText.text = timeNumify(hourSelected, minuteSelected, secondSelected)
        adjMileTime.text = getPaceFromSeconds(adjustedPaceInSeconds(hourSelected, minuteSelected, secondSelected, milesSelected, milesTensSelected, milesOnesSelected, goalDistance))

        // Tack on the decimals
        mileSelectedDecimals = milesSelected.toDouble() + (milesTensSelected * 0.10) + (milesOnesSelected * 0.01)
        // and Display the miles value
        milesNumText.text = "%.2f".format(mileSelectedDecimals).toDouble().toString()
    }


    private fun getPaceStringFromSeconds(totalSeconds: Int) : String {
        //divide seconds by miles
        val paceBySeconds = totalSeconds / mileSelectedDecimals
        // get that number and divide by 60 to get minute pace
        minutePace = paceBySeconds / 60.0
        // use the remainder to get seconds
        //var remainderSeconds = minutePace.toInt() - (paceBySeconds / 60)
        remainderSeconds = (paceBySeconds / 60) - minutePace.toInt()
        // convert seconds in decimal form to seconds in human-readable form
        remainderSeconds = (remainderSeconds * 60)


        println("minutePace is: $minutePace")
        println("remainderSeconds is: $remainderSeconds")
        // return the minute and seconds as a string
        return paceStringify(minutePace, remainderSeconds)
    }

    private fun getArgs() {
        hourSelected = args.hour
        minuteSelected = args.minute
        secondSelected = args.second
        milesSelected = args.miles
        milesTensSelected = args.milesTens
        milesOnesSelected = args.milesOnes
        monthSelected = args.month
        daySelected = args.day
        yearSelected = args.year
    }

}