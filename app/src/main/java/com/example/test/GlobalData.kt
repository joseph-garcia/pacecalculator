package com.example.test

import android.app.Application
import androidx.lifecycle.MutableLiveData


class GlobalData : Application() {
    var menuItems = arrayOf<String>(
        "Do something crazy"
    )

    //livedata stuff
    val listEntries = MutableLiveData<Array<String>>()
}