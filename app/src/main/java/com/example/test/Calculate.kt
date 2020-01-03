package com.example.test

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calculate.*

class Calculate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        val minuteSelected = intent.getIntExtra("minuteSel", 20)
        val calcSelected = intent.getStringExtra("calcSel")

        d("um", "$minuteSelected")

        test1.text = minuteSelected.toString()
        d("calc", "$calcSelected")

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }


}