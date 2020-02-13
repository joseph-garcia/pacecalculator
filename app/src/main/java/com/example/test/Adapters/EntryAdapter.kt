package com.example.test.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Model.RunningEntry
import com.example.test.R
import com.example.test.Services.DataService.runningEntries

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
    }

    // let's add a viewholder, this is responsible for the data binding--or to prepare the child view to display the data corresponding to its position in the adapter
    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val entryImage = itemView?.findViewById<ImageView>(R.id.entryImage)
        val entryTime = itemView?.findViewById<TextView>(R.id.entryTime)
        val entryDifference = itemView?.findViewById<TextView>(R.id.entryDiff)

        fun bindEntry(entry: RunningEntry, context: Context) {
            val resourceId = context.resources.getIdentifier(entry.image, "drawable", context.packageName)
            entryImage?.setImageResource(resourceId)
            entryTime?.text = entry.timeString
            entryDifference?.text = entry.timeFloat.toString()


        }
    }
}