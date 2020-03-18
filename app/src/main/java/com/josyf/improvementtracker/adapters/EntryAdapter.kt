package com.josyf.improvementtracker.adapters

import android.content.Context
import android.graphics.Color
import android.os.Debug
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.josyf.improvementtracker.R
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.db.EntryDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// The adapter that provides a binding from RunningEntry (from Model package) to the RecyclerView list items (from entry_list_item.xml)
class EntryAdapter(private val context: Context, private val entries: MutableList<Entry>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {



    // called when new viewholders are needed. kind of similar to inflate XML views--but only when there isn't an existing one to recycle
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false)
        return EntryViewHolder(view)
    }

    // just tells the recyclerview how many items are going to be displayed
    override fun getItemCount(): Int {
        return entries.count()
    }

    private fun removeItem(position: Int) {
        entries.removeAt(position)
        notifyDataSetChanged()

        GlobalScope.launch {
            context.let {
                val entries = EntryDatabase(it).entryDao().getAllEntries()
                println("entries is: $entries")
                EntryDatabase(it).entryDao().deleteEntry(entries[position])
            }
        }

    }




    // function that is called by the recycler view to display the data at the specified location
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bindEntry(entries[position])




        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("Delete").setOnMenuItemClickListener {
                removeEntryFromView(position)
                Toast.makeText(context, "Entry removed", Toast.LENGTH_SHORT).show()
                true
            }
        }

    }

    private fun sortEntries(entries : MutableList<Entry>) {

    }

    private fun removeEntryFromView(position: Int) {
        Toast.makeText(context, "Entry removed", Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            context.let {
                val entries = EntryDatabase(it).entryDao().getAllEntries()
                EntryDatabase(it).entryDao().deleteEntry(entries[position])

                recalculateDifferenceValues(entries)

            }
        }

        removeItem(position)
        notifyDataSetChanged()
    }

    private suspend fun recalculateDifferenceValues(list: List<Entry>) {
        println("List is length: ${list.count()}")
        var count = 0
        while (count <= list.count() - 1) {
            if (list.count() == 1) {
                count += 1
                println("I'm in the first if statement")
                return
            }
            // if count is at the end of the list, assign the last "--"
            if ((count == list.count() - 1)) {
                list[count].timeDifference = "--"
                count += 1
                println("I'm in the second if statement")
                break
            }
            // extract numbers from the timeDifference string and subtract them to get diffNum
            //val firstStringOriginal = list[count].timeDifference
            //val firstStringNew = stringToNum(firstStringOriginal)
            //if (list[count+1].timeDifference != "") {
            //val secondStringOriginal = list[count+1].timeDifference
            //val secondStringNew = stringToNum(secondStringOriginal)
            else {
                val firstNum = list[count].adjustedTimeInSeconds
                val nextNum = list[count+1].adjustedTimeInSeconds
                val differenceNum : Int = firstNum - nextNum
                list[count].timeDifference = differenceNum.toString()
            }
            //val differenceNum : Int = firstNum - nextNum
            //} else {
            //    val differenceNum : Int = firstStringNew -
            //}




            //list[count].timeDifference = differenceNum.toString()
            EntryDatabase(context).entryDao().updateEntry(list[count])
            count += 1
            println("Outside the if statements. Incrementing count")
        }


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

            entryDifference.text = entry.timeDifference

            entryDistance?.text = entry.distanceString
            entryDate?.text = entry.dateString
            entryAdjusted?.text = entry.adjustedTime
            entryPace?.text = entry.paceString


        }
    }
}