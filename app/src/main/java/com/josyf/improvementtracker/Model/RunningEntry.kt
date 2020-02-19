package com.josyf.improvementtracker.Model

// the class RunningEntry itself. This is the blueprint for each entry object
class RunningEntry(val timeString: String,
                   val distanceString: String,
                   val paceString: String,
                   val dateString: String,
                   val adjustedTime: String,
                   val adjustedTimeInSeconds: Int,
                   var timeDifference: String,
                   val image: String)