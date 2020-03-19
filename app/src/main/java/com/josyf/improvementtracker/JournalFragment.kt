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
                println("is empty: ${entries.isEmpty()}")
                entryRecyclerMenu.adapter = EntryAdapter(it, entries as MutableList<Entry>)

            }
        }

        refreshPic.setOnClickListener{view ->
            launch {
                context?.let{
                        val dbAccess = EntryDatabase(it).entryDao()
                        dbAccess.addEntry(Entry("2h:15m:0s", "13.10 miles", "10:18/mi", "March 1, 2020", 530, "8:50", "--" ))
                        dbAccess.addEntry(Entry("2h:10m:0s", "13.10 miles", "9:55/mi", "March 3, 2020", 510, "8:30", "-20s" ))
                        dbAccess.addEntry(Entry("2h:7m:0s", "13.10 miles", "9:41/mi", "March 5, 2020", 498, "8:18", "-12s" ))
                        dbAccess.addEntry(Entry("2h:14m:0s", "13.10 miles", "10:13/mi", "March 7, 2020", 526, "8:46", "+28s" ))
                        dbAccess.addEntry(Entry("2h:5m:0s", "13.10 miles", "9:32/mi", "March 11, 2020", 491, "8:11", "-35s" ))
                        dbAccess.addEntry(Entry("2h:0m:0s", "13.10 miles", "9:09/mi", "March 15, 2020", 470, "7:50", "-20s" ))
                        dbAccess.addEntry(Entry("1h:56m:0s", "13.10 miles", "8:51/mi", "March 19, 2020", 454, "7:34", "-16s" ))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }






}

