package com.josyf.improvementtracker.Services

import com.josyf.improvementtracker.Model.RunningEntry


// Our mutable list of RunningEntry class objects are encapsulated in a DataService singleton for simplicity. Our object
// list has to live somewhere, I suppose. This part may be interchangeable with Firebase
object DataService {
    val runningEntries = mutableListOf<RunningEntry>(
//        RunningEntry("1h:30m:0s", "12 miles", "8:30/mi", "Feb 17, 2020", "7m:20s", 440, "+2s", "entrytemplate"),
//        RunningEntry("1h:30m:0s", "14 miles", "8:45/mi", "Feb 15, 2020", "6m:30s", 390, "-1s","entrytemplate")
    )
}