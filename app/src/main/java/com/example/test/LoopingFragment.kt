package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_looping.*

class LoopingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_looping, container, false)

        var menuItems = arrayOf<String>(
            "Do something",
            "Do something else",
            "Do yet another thing"
            )

        var listView : ListView = entryMenu

        var listViewAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            menuItems
        )

        listView.setAdapter(listViewAdapter)

        return view
    }
}