package com.josyf.improvementtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.josyf.improvementtracker.Adapters.EntryAdapter
import com.josyf.improvementtracker.Services.BaseFragment
import com.josyf.improvementtracker.Services.DataService
import com.josyf.improvementtracker.db.EntryDatabase
import kotlinx.android.synthetic.main.fragment_looping.*
import kotlinx.coroutines.launch

class LoopingFragment : BaseFragment() {
    lateinit var adapter: EntryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // set adapter that we declared but didn't initialize before
        adapter = EntryAdapter(this.requireContext(), DataService.runningEntries)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // takes the XML info and shows it
        var view = inflater.inflate(R.layout.fragment_looping, container, false)

        // get global variable
//        var data = GlobalData()
//        data.listEntries.observe(this,
//            Observer {
//                var menuItems = data.menuItems
//                var listView : ListView = view.entryMenu
//                var listViewAdapter = ArrayAdapter<String>(
//                    requireActivity(),
//                    android.R.layout.simple_list_item_1,
//                    menuItems
//                )
//
//                listView.setAdapter(listViewAdapter)
//            })
//
//
//        // get global variable
//        var menuItems = data.menuItems
//
//        var listView : ListView = view.entryMenu
//
//        var listViewAdapter = ArrayAdapter<String>(
//            requireActivity(),
//            android.R.layout.simple_list_item_1,
//            menuItems
//        )
//
//        listView.setAdapter(listViewAdapter)

//
        return view
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
                //entryRecyclerMenu.adapter = EntryAdapter(entries)
            }
        }




    }

}