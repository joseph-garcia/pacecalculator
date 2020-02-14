package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapters.EntryAdapter
import com.example.test.Services.DataService
import kotlinx.android.synthetic.main.fragment_looping.*
import kotlinx.android.synthetic.main.fragment_looping.view.*
import kotlinx.android.synthetic.main.activity_main.*

class LoopingFragment : Fragment() {
    lateinit var adapter: EntryAdapter
    //lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // set adapter that we declared but didn't initialize before
        adapter = EntryAdapter(this.requireContext(), DataService.runningEntries)
//        val layoutManager = LinearLayoutManager(this.requireContext())
//        entryRecyclerMenu.layoutManager = layoutManager
//        entryRecyclerMenu.setHasFixedSize(true)


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
        val layoutManager = LinearLayoutManager(this.requireContext())
        entryRecyclerMenu.layoutManager = layoutManager
        entryRecyclerMenu.adapter = adapter
        entryRecyclerMenu.setHasFixedSize(true)




    }

}