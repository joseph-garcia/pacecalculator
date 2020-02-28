package com.josyf.improvementtracker.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.josyf.improvementtracker.Model.RunningEntry
import com.josyf.improvementtracker.R
import com.josyf.improvementtracker.Services.DataService.runningEntries

// The adapter that provides a binding from RunningEntry (from Model package) to the RecyclerView list items (from entry_list_item.xml)
class EntryAdapter(val context: Context, val entries: List<RunningEntry>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {



    // called when new viewholders are needed. kind of similar to inflate XML views--but only when there isn't an existing one to recycle
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.entry_list_item, parent, false)
        return EntryViewHolder(view)
    }

    // just tells the recyclerview how many items are going to be displayed
    override fun getItemCount(): Int {
        return entries.count()
    }

    // function that is called by the recycler view to display the data at the specified location
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder?.bindEntry(runningEntries[position], context)

        //area for updating (navigating? when an entry is clicked on, this happens)
//        holder.view.setOnClickListener{
//            val action = HomeFragmentDirections.actionAddNote()
//            action.note = notes[position]
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    // let's add a viewholder, this is responsible for the data binding--or to prepare the child view to display the data corresponding to its position in the adapter
    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val entryImage = itemView?.findViewById<ImageView>(R.id.entryImage)
        val entryTime = itemView?.findViewById<TextView>(R.id.entryTime)
        val entryDifference = itemView?.findViewById<TextView>(R.id.entryDiff)

        val entryDistance = itemView?.findViewById<TextView>(R.id.entryDistance)
        val entryDate = itemView?.findViewById<TextView>(R.id.entryDate)
        val entryAdjusted = itemView?.findViewById<TextView>(R.id.entryAdjusted)
        val entryPace = itemView?.findViewById<TextView>(R.id.entryPace)

        // actual binding shia leboeuf
        fun bindEntry(entry: RunningEntry, context: Context) {
            val resourceId = context.resources.getIdentifier(entry.image, "drawable", context.packageName)
            entryImage?.setImageResource(resourceId)
            entryTime?.text = entry.timeString

            println("time difference is ${entry.timeDifference}")
            if ((entry.timeDifference).contains("-")) {
                entryDifference?.setTextColor(Color.GREEN)
            } else {
                entryDifference?.setTextColor(Color.RED)
            }

            entryDifference?.text = entry.timeDifference

            entryDistance?.text = entry.distanceString
            entryDate?.text = entry.dateString
            entryAdjusted?.text = entry.adjustedTime
            entryPace?.text = entry.paceString


        }
    }
}