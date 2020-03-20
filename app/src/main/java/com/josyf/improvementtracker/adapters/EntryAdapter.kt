package com.josyf.improvementtracker.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.josyf.improvementtracker.MainActivity
import com.josyf.improvementtracker.R
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.db.EntryDatabase
import com.josyf.improvementtracker.formatDifferenceValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// The adapter that provides a binding from RunningEntry (from Model package) to the RecyclerView list items (from entry_list_item.xml)


class EntryAdapter(private val context: Context, private val entries: MutableList<Entry>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {


    // called when new viewholders are needed. kind of similar to inflate XML views--but only when there isn't an existing one to recycle
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false)
        return EntryViewHolder(view)
    }

    // just tells the recyclerview how many items are going to be displayed
    override fun getItemCount(): Int {
        return entries.count()
    }


    // function that is called by the recycler view to display the data at the specified location
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {

        holder.bindEntry(entries[position])


        // adds the Delete context menu to itself
        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("Delete").setOnMenuItemClickListener {
                GlobalScope.launch {
                    removeEntryFromView(position)
                }

                Toast.makeText(context, "Entry removed", Toast.LENGTH_SHORT).show()
                //println("The list is of length: ${entries.count()}")
                //recalculateDifferenceValues(entries, position)
                //notifyDataSetChanged()
                true
            }
        }

    }

    private fun sortEntries(entries: MutableList<Entry>) {

    }

    private suspend fun removeEntryFromView(position: Int) {
        //Toast.makeText(context, "Entry removed", Toast.LENGTH_SHORT).show()


        // deletes itself from the Database
        EntryDatabase(context).entryDao().deleteEntry(entries[position])

        // this removes the above particular entry from the EntryAdapter list
        entries.removeAt(position)

        println("Entries length is: ${entries.count()}")
        println("Entries position is: $position")

        var isOneLeft : Boolean = false
        if (entries.count() == 0) {
            isOneLeft = true
            println("this is firing off")
        }


        // ### Since the entry is already removed, here we handle recalculation of the entry that took its place ###
        // if the position index (the deleted entry) was the LAST entry, set the timeDifference of
        // the second-to-last entry to --, as this is now the new baseline entry
        // NOTE: Also we need to account for if there is only one entry left in the list
        if (entries.count() == position && !isOneLeft) {
            entries[position - 1].timeDifference = "--"
            notifyItemChanged(position-1)
            EntryDatabase(context).entryDao().updateEntry(entries[position - 1])
            notifyDataSetChanged()
            println("HELLO! I WAS HIDING!")
        }

        // if this is the FIRST entry (at the top), skip on ahead and just delete as usual.
        // else, set a new timeDifference value for the upper entry (position-1)
        else if (0 != position && !isOneLeft) {
            val newTimeDiff: Int =
                (entries[position - 1].adjustedTimeInSeconds - entries[position].adjustedTimeInSeconds)
            entries[position - 1].timeDifference = formatDifferenceValue(newTimeDiff)
            notifyItemChanged(position - 1)
            EntryDatabase(context).entryDao().updateEntry(entries[position - 1])
            notifyDataSetChanged()
        }

        println("Position is $position")

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            notifyDataSetChanged()
        }



        }


    private fun recalculateDifferenceValues(entries : List<Entry>, position: Int) {
        // list[position - 1]  should be the entry that's taken its place--the one that needs its diffValue recalculated
        // set newTimeDiff value = list[position].adjustedPaceInSeconds value - list[position-1].adjustedPaceInSeconds value
        // if the item IS the first element in the list:
            // assign the list[i] timeDiff value to "--"
        // else: reassign the list[i] timeDiff value to this new Value
        // update the list[i] value
        // notifyDataSetChanged
        //println("entries[position] is ${entries[position-1]}")
        if (0 != position) {
            val newTimeDiff : Int = (entries[position-1].adjustedTimeInSeconds - entries[position].adjustedTimeInSeconds)
            entries[position-1].timeDifference = formatDifferenceValue(newTimeDiff)
            notifyItemChanged(position-1)
            GlobalScope.launch {
                context.let {
                    EntryDatabase(it).entryDao().updateEntry(entries[position-1])
                }
            }
            println("Position is $position")
        }




//        removeItem(position)
//        notifyDataSetChanged()



    }





    private fun stringToNum(string: String) : Int {
        val strippedString = string.replace("[^0-9]".toRegex(), "")
        return strippedString.toInt()
    }

    // let's add a ViewHolder, this is responsible for the data binding--or to prepare the child view to display the data corresponding to its position in the adapter
    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        private val entryImage = itemView.findViewById<ImageView>(R.id.entryImage)
        private val entryTime = itemView.findViewById<TextView>(R.id.entryTime)
        private val entryDifference: TextView = itemView.findViewById<TextView>(R.id.entryDiff)
        private val entryDistance = itemView.findViewById<TextView>(R.id.entryDistance)
        private val entryDate = itemView.findViewById<TextView>(R.id.entryDate)
        private val entryAdjusted = itemView.findViewById<TextView>(R.id.entryAdjusted)
        private val entryPace = itemView.findViewById<TextView>(R.id.entryPace)

        // actual binding shia leboeuf
        fun bindEntry(entry: Entry) {
            //val resourceId = context.resources.getIdentifier(entry.image, "drawable", context.packageName)
            //entryImage?.setImageResource(resourceId)

            entryTime?.text = entry.timeString

            println("In bindEntry - timeDiff - ${entry.timeDifference}")
            if ((entry.timeDifference).contains("-") && (entry.timeDifference != "--")) {
                entryDifference.setTextColor(Color.parseColor("#64dd17"))
            } else if (entry.timeDifference == "--"){
                entryDifference.setTextColor(Color.BLACK)
            } else {
                entryDifference.setTextColor(Color.RED)
            }

//            entryDifference.text = entry.timeDifference
//
//            entryDistance?.text = entry.distanceString
//            entryDate?.text = entry.dateString
//            entryAdjusted?.text = entry.adjustedTime
//            entryPace?.text = entry.paceString
            assignTextViews(entry)


        }

        fun assignTextViews(entry: Entry) {
            entryDifference.text = entry.timeDifference

            entryDistance?.text = entry.distanceString
            entryDate?.text = entry.dateString
            entryAdjusted?.text = entry.adjustedTime
            entryPace?.text = entry.paceString
        }
    }
}