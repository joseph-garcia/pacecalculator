package com.example.test.Services

import com.example.test.Model.RunningEntry


// this is our singleton. in here, we want all of our arrays that hold our data for our running entries
object DataService {
    val runningEntries = mutableListOf(
        RunningEntry("1h:30m:0s", "12 miles", "8:30/mi", "Feb 17, 2020", "-2s", "7m:20s", "entrytemplate"),
        RunningEntry("1h:30m:0s", "14 miles", "8:45/mi", "Feb 15, 2020", "-10s", "6m:30s","entrytemplate")
    )
}