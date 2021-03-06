package com.josyf.improvementtracker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_calculator.*
import java.text.SimpleDateFormat
import java.util.*


class PaceCalcFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Add Entry"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // takes the XML info and shows it
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Create pickers
        val mainHourPicker = numberpicker_main_hours
        val mainMinutePicker = numberpicker_main_minutes
        val mainSecondPicker = numberpicker_main_seconds
        val mainMilesPicker = numberpicker_main_miles
        val mainMilesTensPicker = numberpicker_main_milesTens
        val mainMilesOnesPicker = numberpicker_main_milesOnes

        // Set picker max/min bounds
        setMaxMin(mainHourPicker, 0, 4)
        setMaxMin(mainMinutePicker, 0, 59)
        setMaxMin(mainSecondPicker, 0, 59)
        setMaxMin(mainMilesPicker, 1, 26)
        setMaxMin(mainMilesTensPicker, 0, 9)
        setMaxMin(mainMilesOnesPicker, 0, 9)


        // Change Hour Selected from User
        var hourSelected = 0
        // for clarity: Underscores used to be numberPicker, oldVal
        mainHourPicker.setOnValueChangedListener { _, _, newVal ->
            hourSelected = newVal
        }

        // Change Minute Selected from User
        var minuteSelected = 0
        mainMinutePicker.setOnValueChangedListener { _, _, newVal ->
            minuteSelected = newVal
        }

        // Change Second Selected from User
        var secondSelected = 0
        mainSecondPicker.setOnValueChangedListener { _, _, newVal ->
            secondSelected = newVal
        }

        // Change Miles Selected from User
        var milesSelected = 1
        mainMilesPicker.setOnValueChangedListener { _, _, newVal ->
            milesSelected = newVal
        }

        // Change Miles Tens Selected from User
        var milesTensSelected = 0
        mainMilesTensPicker.setOnValueChangedListener { _, _, newVal ->
            milesTensSelected = newVal
        }

        // Change Miles Ones Selected from User
        var milesOnesSelected = 0
        mainMilesOnesPicker.setOnValueChangedListener { _, _, newVal ->
            milesOnesSelected = newVal
        }

        initDate()

        calculateBtn.setOnClickListener {

            if (inBounds(hourSelected, minuteSelected, secondSelected)) {
                val action = PaceCalcFragmentDirections.toCalculation(
                    hourSelected,
                    minuteSelected,
                    secondSelected,
                    milesSelected,
                    milesTensSelected,
                    milesOnesSelected,
                    dayPickedText.text.toString().toInt(),
                    yearPickedText.text.toString().toInt(),
                    monthPickedText.text.toString()
                )

                activity?.findViewById<NavigationView>(R.id.nav_view)?.setCheckedItem(R.id.ic_journal)
                Navigation.findNavController(view!!).navigate(action)
            } else {
                Toast.makeText(context, "Please select a time greater than three and a half minutes for better accuracy.", Toast.LENGTH_LONG).show()
            }
        }

        changeDateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)

            // get and format month name
            val monthDate = SimpleDateFormat("MMMM", Locale.getDefault())
            val monthName = monthDate.format(c.time)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{ _, mYear, _, mDay ->
                // set to textview
                monthPickedText.text = monthName
                dayPickedText.text = mDay.toString()
                yearPickedText.text = mYear.toString()
            }, year, month, day)
            // show dialog
            dpd.show()
        }
    }

    private fun setMaxMin(numPicker : NumberPicker, min : Int, max : Int) {
        numPicker.minValue = min
        numPicker.maxValue = max
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDate() {
        val cal: Calendar = Calendar.getInstance()
        val monthDate = SimpleDateFormat("MMMM")
        val monthName: String = monthDate.format(cal.time)
        val dayDate = SimpleDateFormat("dd")
        val dayName: String = dayDate.format(cal.time)
        val yearDate = SimpleDateFormat("yyyy")
        val yearName: String = yearDate.format(cal.time)

        monthPickedText.text = monthName
        dayPickedText.text = dayName
        yearPickedText.text = yearName

    }

    private fun inBounds(hourSelected : Int, minuteSelected : Int, secondSelected : Int) : Boolean {
        return if (hourSelected != 0) {
            true
        } else return ((minuteSelected * 60) + secondSelected) > 210
    }
}