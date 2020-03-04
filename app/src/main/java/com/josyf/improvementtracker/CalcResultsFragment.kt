package com.josyf.improvementtracker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.josyf.improvementtracker.Services.BaseFragment
import kotlinx.android.synthetic.main.activity_calculate.*
import com.josyf.improvementtracker.Services.DataService.runningEntries
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.db.EntryDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt

// The logic behind the main Pace Calculator xml view.
class CalcResultsFragment : BaseFragment() {

    // Each variable corresponds to differing numerical values from the view.
    // It's separated like this, because it's separated in the view as well.
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
    private var adjustedTimeInSeconds: Int = 0

    private val args: CalcResultsFragmentArgs by navArgs()

    // this happens first
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_calculate)


    }


    //this happens second
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

        //val model: CalculationViewModel by viewModels()


        println("the hour of the args is ${args.hour} wow ezpz")



        // initialize the variables carried through from MainActivity
        getArgs()

        // Display the values passed from Pace Calculator (not actually visible until Calculate Pace is pressed)
        displayValues()


        // calculates pace and binds pace text to view
        val totalSeconds = 3600*(hourSelected) + 60*(minuteSelected) + secondSelected
        val paceText = getPaceStringFromSeconds(totalSeconds) + "/mi"
        paceStringText.text = paceText


//        // Back button
//        backBtn.setOnClickListener {
//            onBackPressed()
//
//        }

        // SEND TO JOURNAL BUTTON
        sendButton.setOnClickListener {view ->
            //val toast = Toast.makeText(applicationContext, "Ouch!", Toast.LENGTH_SHORT)
            //toast.show()



            val timeString = timeStringify(hourSelected, minuteSelected, secondSelected)
            val distanceString = distanceStringify(milesSelected, milesTensSelected, milesOnesSelected)
            val paceString = paceText
//            //val paceString = "pace strting here"
            val dateString = "Feb 18, 2020"
            val adjustedTimeInSeconds: Int = adjustedPaceInSeconds(hourSelected, minuteSelected, secondSelected, milesSelected, milesTensSelected, milesOnesSelected, goalDistance)
            val adjustedTime: String = getPaceFromSeconds(adjustedTimeInSeconds)
//            DataService.runningEntries.add(RunningEntry(
//                timeString,
//                distanceString,
//                paceString,
//                dateString,
//                adjustedTime,
//                adjustedTimeInSeconds,
//                "",
//                "entrytemplate"))
//            val toast = Toast.makeText(context, "New entry added!", Toast.LENGTH_LONG)
//            toast.show()
//            val timeDifference = getTimeDifference()
//            runningEntries[runningEntries.lastIndex].timeDifference = timeDifference
//            println(adjustedTimeInSeconds)
//            println("list length is: ${runningEntries.size}")

            launch {
                context?.let{
                    println("adjustedTime: $adjustedTime")
                    println("adjustedTimeInSeconds: $adjustedTimeInSeconds")
                    val testEntry = Entry(timeString, distanceString, paceString, dateString, adjustedTimeInSeconds,adjustedTime,"n/a", "entrytemplate")
                    getTimeDifference2(testEntry)
                    val dbAccess = EntryDatabase(it).entryDao()
                    //EntryDatabase(it).entryDao().addEntry(testEntry)
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


    // test new timediff func
    suspend fun getTimeDifference2(entry: Entry) {
        // initialize timeDiff string and differencevalue int
        var timeDiffString:String = ""
        var differenceValue:Int = 0
        // check if any entries exist in the list already
        async {
            context?.let {
                // if isEmpty, keep timeDiffString as ""

                if (EntryDatabase(it).entryDao().getAllEntries().isEmpty()) {
                    entry.timeDifference = ""
                } else { //if not, check the timeDiff value of the last item in the list
                    val entryList = EntryDatabase(it).entryDao().getAllEntries()
                    val lastItem = entryList[0]
                    // calculate the timeDiff
                    differenceValue = entry.adjustedTimeInSeconds - lastItem.adjustedTimeInSeconds
                    timeDiffString = differenceValue.toString()

                    if (differenceValue > 0) {
                        //make value red and append a + in front
                        timeDiffString = "+$timeDiffString"
                    } else if (differenceValue < 0) {
                        //     make value green and append a - in front
                        timeDiffString = "$timeDiffString"
                    }
                    // append an s after the string
                    timeDiffString = "${timeDiffString}s"
                    entry.timeDifference = timeDiffString
                }
                val ListToPrint = (EntryDatabase(it).entryDao().getAllEntries())
                println("the size of the list is ${ListToPrint.size}")

            }
        }.await()
        EntryDatabase(context!!).entryDao().updateEntry(entry)
        println("now it should be updated... ${entry.timeDifference}")
    }

    suspend fun getTimeDifference() : String {
        var differenceValue:Int = 0
        var timeDiffString:String = ""
        async {
            context?.let {
                // if the list of entries > 1:
                if (EntryDatabase(context!!).entryDao().getAllEntries().size > 1) {
                    //     get the value of current adjustedTimeInSeconds
                    val lastIndex = runningEntries.lastIndex
                    //     get the value of previous adjustedTimeInSeconds, and subtract current - previous
                    differenceValue =
                        runningEntries[lastIndex].adjustedTimeInSeconds - runningEntries[lastIndex - 1].adjustedTimeInSeconds
                    timeDiffString = differenceValue.toString()
                }

                if (differenceValue > 0) {
                    //make value red and append a + in front
                    timeDiffString = "+$timeDiffString"
                } else if (differenceValue < 0) {
                    //     make value green and append a - in front
                    timeDiffString = "$timeDiffString"
                }
                // append an s after the string
                timeDiffString = "${timeDiffString}s"
                return@async timeDiffString
            }
            println("balwijbalwieb $timeDiffString")
        }.await()
        return timeDiffString

    }



    fun timeStringify(hour:Int, minute:Int, second:Int) : String {
        if (hour != 0) {
            return "${hour}h:${minute}m:${second}s"
        }
        return "${minute}m:${second}s"
    }

    fun paceStringify(minutePace:Double, secondPace:Double) : String {
        if (secondPace < 10) {
            return "${minutePace.toInt()}:0${secondPace.toInt()}"
        } else return "${minutePace.toInt()}:${secondPace.toInt()}"
    }

    fun distanceStringify(miles:Int, milesTens:Int, milesOnes:Int) : String {
        return "$miles.$milesTens$milesOnes miles"
    }

    fun getPaceFromSeconds(seconds:Int) : String {
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

    fun adjustedPaceInSeconds(hours: Int, minutes:Int, seconds:Int, miles:Int, milesTens:Int, milesOnes: Int, goalMiles:Double) : Int {
        //Race time predictor algorithm from Pete Riegel
        //T2 = T1 x ((D2/D1)^1.06) where T1 is the given time, D1 is the given distance, D2 is the distance to predict a time for, and T2 is the calculated time for D2.
        // adjustedPace = runDurationInMinutes * ((goalMiles/milesRan)^1.06)
        val milesRan:Double = miles + (milesTens * 0.10) + (milesOnes * 0.01)
        val runDurationInMinutes:Double = (hours * 60) + minutes + (seconds/60.0)
        val adjustedPace:Double = runDurationInMinutes * ((goalMiles/milesRan).pow(1.06))
        val minuteValue = adjustedPace.toInt()
        val secondsValue = ((adjustedPace - minuteValue) * 60).roundToInt()
        val adjustedTimeInSeconds = (minuteValue * 60) + secondsValue
        return adjustedTimeInSeconds
        //return (getMinutesFromSeconds(adjustedTimeInSeconds))

    }


    fun displayValues() {

        hoursNumText.text = hourSelected.toString()
        minutesNumText.text = minuteSelected.toString()
        secondsNumText.text = secondSelected.toString()
        //milesNumText.text = milesSelected.toString()

        // Tack on the decimals
        mileSelectedDecimals = milesSelected.toDouble() + (milesTensSelected * 0.10) + (milesOnesSelected * 0.01)
        // and Display the miles value
        milesNumText.text = mileSelectedDecimals.toString()
    }


    fun getPaceStringFromSeconds(totalSeconds: Int) : String {
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

    fun getArgs() {
        hourSelected = args.hour
        minuteSelected = args.minute
        secondSelected = args.second
        milesSelected = args.miles
        milesTensSelected = args.milesTens
        milesOnesSelected = args.milesOnes
        println("bleh")
    }

}