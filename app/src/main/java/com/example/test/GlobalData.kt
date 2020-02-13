package com.example.test

import android.app.Application
import androidx.lifecycle.MutableLiveData


class GlobalData : Application() {
    var menuItems = arrayOf<String>(
        "Do something crazy",
        "Biggity",
        "Goodity"
    )

    //livedata stuff
    val listEntries = MutableLiveData<Array<String>>()
}