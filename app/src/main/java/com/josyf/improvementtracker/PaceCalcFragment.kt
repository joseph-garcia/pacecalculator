package com.josyf.improvementtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_calculator.*


class PaceCalcFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        // takes the XML info and shows it
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        return view

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
        setMaxMin(mainHourPicker, 0, 7)
        setMaxMin(mainMinutePicker, 0, 59)
        setMaxMin(mainSecondPicker, 0, 59)
        setMaxMin(mainMilesPicker, 1, 99)
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



        calculateBtn.setOnClickListener {

            val action = PaceCalcFragmentDirections.toCalculation(
                hourSelected,
                minuteSelected,
                secondSelected,
                milesSelected,
                milesTensSelected,
                milesOnesSelected
            )
            Navigation.findNavController(view!!).navigate(action)
        }
    }

    fun setMaxMin(numPicker : NumberPicker, min : Int, max : Int) {
        numPicker.minValue = min
        numPicker.maxValue = max
    }

}