package com.josyf.improvementtracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_calculator.*

class CalculateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
//            // Pass the minute selected variable to the Calculate class
//            val intent = Intent(activity, Calculate::class.java);
//            intent.putExtra("hourSel", hourSelected)
//            intent.putExtra("minuteSel", minuteSelected)
//            intent.putExtra("secondSel", secondSelected)
//            intent.putExtra("mileSel", milesSelected)
//            intent.putExtra("mileSelTens", milesTensSelected)
//            intent.putExtra("mileSelOnes", milesOnesSelected)
//            intent.putExtra("calcSel", "PaceCalc")
//            startActivity(intent)
            //fragment to
            val action = CalculateFragmentDirections.calcToResult()
            Navigation.findNavController(view!!).navigate(action)
        }
    }

}