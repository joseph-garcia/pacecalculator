package com.josyf.improvementtracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.RoomDatabase
import com.josyf.improvementtracker.adapters.EntryAdapter
import com.josyf.improvementtracker.db.Entry
import com.josyf.improvementtracker.services.BaseFragment
import com.josyf.improvementtracker.db.EntryDatabase
import com.josyf.improvementtracker.db.ImageURI
import com.josyf.improvementtracker.db.ImageURIDatabase
import kotlinx.android.synthetic.main.entry_list_item.*
import kotlinx.android.synthetic.main.entry_list_view.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JournalFragment : BaseFragment() {
    lateinit var adapter: EntryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Journal"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // takes the XML info and shows it
        return inflater.inflate(R.layout.entry_list_view, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        entryRecyclerMenu.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this.requireContext())

        entryRecyclerMenu.layoutManager = layoutManager

        launch {
            context?.let{
                val entries = EntryDatabase(it).entryDao().getAllEntries()
                entryRecyclerMenu.adapter = EntryAdapter(it, entries as MutableList<Entry>)
                if (entries.isEmpty()) {
                    println("Is empty. Printing thingy")
                    emptyText.visibility = View.VISIBLE
                    hamburgerImage.visibility = View.VISIBLE
                    emptyText2.visibility = View.VISIBLE
                } else {
                    emptyText.visibility = View.INVISIBLE
                    hamburgerImage.visibility = View.INVISIBLE
                    emptyText2.visibility = View.INVISIBLE
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }








}

