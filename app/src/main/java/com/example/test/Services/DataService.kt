package com.example.test.Services

import com.example.test.Model.RunningEntry


// this is our singleton. in here, we want all of our arrays that hold our data for our running entries
object DataService {
    val runningEntries = mutableListOf(
        RunningEntry("1h:30m:0s", "+2.5s", "entrytemplate"),
        RunningEntry("1h:30m:0s", "+2.5s", "entrytemplate")
    )
}