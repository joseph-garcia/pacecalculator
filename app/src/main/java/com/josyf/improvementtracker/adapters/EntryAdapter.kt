package com.josyf.improvementtracker.adapters

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
import com.josyf.improvementtracker.R
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.db.EntryDatabase
import com.josyf.improvementtracker.formatDifferenceValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


// The adapter that provides a binding from entry database to the RecyclerView list items (from entry_list_item.xml)


class EntryAdapter(private val context: Context, private val entries: MutableList<Entry>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {


    // called when new viewHolders are needed. kind of similar to inflate XML views--but only when there isn't an existing one to recycle
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
                true
            }
        }

    }

    private suspend fun removeEntryFromView(position: Int) {

        // We need to account for if there is only one entry left in the list
        // If so, return isOneLeft true so we can move on to deletion and nothing else
        CoroutineScope(IO).launch {
            val isOneLeft : Boolean = async {
                if (entries.count() == 1) {
                    println("this is firing off")
                    return@async true
                } else return@async false
            }.await()
            // If last entry was deleted: Set its upper entry to -- as the new baseline
            val mainValueChanged = async {
                if (entries.count() == (position+1) && !isOneLeft) {
                    val previousPosition = position-1
                    entries[previousPosition].timeDifference = "--"
                    EntryDatabase(context).entryDao().updateEntry(entries[previousPosition])
                }
                // if this is the FIRST entry (at the top), skip on ahead and just delete as usual.
                // else, set a new timeDifference value for the upper entry (position-1)
                else if (0 != position && !isOneLeft) {
                    val previousPosition = position-1
                    val nextPosition = position+1
                    val newTimeDiff: Int = (entries[previousPosition].adjustedTimeInSeconds - entries[nextPosition].adjustedTimeInSeconds)
                    entries[previousPosition].timeDifference = formatDifferenceValue(newTimeDiff)
                    EntryDatabase(context).entryDao().updateEntry(entries[previousPosition])
                }
            }.await()
            // Now that all logic is handled, delete the entry and update recyclerview
            val deleteFromDb = async {
                // deletes itself from the Database
                EntryDatabase(context).entryDao().deleteEntry(entries[position])
                // this removes the above particular entry from the EntryAdapter list
                entries.removeAt(position)
                println("this always second")
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    notifyDataSetChanged()
                }
            }
        }
    }

    // here, we add the ViewHolder, this is responsible for the data binding--or to prepare the child view to display the data corresponding to its position in the adapter
    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        private val entryImage = itemView.findViewById<ImageView>(R.id.entryImage)
        private val entryTime = itemView.findViewById<TextView>(R.id.entryTime)
        private val entryDifference: TextView = itemView.findViewById<TextView>(R.id.entryDiff)
        private val entryDistance = itemView.findViewById<TextView>(R.id.entryDistance)
        private val entryDate = itemView.findViewById<TextView>(R.id.entryDate)
        private val entryAdjusted = itemView.findViewById<TextView>(R.id.entryAdjusted)
        private val entryPace = itemView.findViewById<TextView>(R.id.entryPace)


        // actual binding
        fun bindEntry(entry: Entry) {

            entryTime?.text = entry.timeString

            if ((entry.timeDifference).contains("-") && (entry.timeDifference != "--")) {
                entryDifference.setTextColor(Color.parseColor("#64dd17"))
            } else if (entry.timeDifference == "--"){
                entryDifference.setTextColor(Color.BLACK)
            } else {
                entryDifference.setTextColor(Color.RED)
            }
            assignTextViews(entry)
        }

        private fun assignTextViews(entry: Entry) {
            entryDifference.text = entry.timeDifference

            entryDistance?.text = entry.distanceString
            entryDate?.text = entry.dateString
            entryAdjusted?.text = entry.adjustedTime
            entryPace?.text = entry.paceString
        }
    }
}