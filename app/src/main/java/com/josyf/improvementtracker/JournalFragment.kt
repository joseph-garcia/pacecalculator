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
import com.josyf.improvementtracker.services.BaseFragment
import com.josyf.improvementtracker.db.EntryDatabase
import com.josyf.improvementtracker.db.ImageURI
import com.josyf.improvementtracker.db.ImageURIDatabase
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
                //val entries = EntryDatabase(it).entryDAO().getAllEntries()
                val entries = EntryDatabase(it).entryDao().getAllEntries()
                println("is empty: ${entries.isEmpty()}")
                entryRecyclerMenu.adapter = EntryAdapter(it,entries)
            }
        }

    refreshPic.setOnClickListener{view ->
        launch {
            context?.let{
               ImageURIDatabase(it).ImageDAO().deleteAll(ImageURIDatabase(it).ImageDAO().getAllEntries())
            }
        }
    }


    }






}

