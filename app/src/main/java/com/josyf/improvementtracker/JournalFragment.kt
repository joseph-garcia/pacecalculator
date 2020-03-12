package com.josyf.improvementtracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.josyf.improvementtracker.adapters.EntryAdapter
import com.josyf.improvementtracker.services.BaseFragment
import com.josyf.improvementtracker.db.EntryDatabase
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
        showImageFromDb()
    }


    }


    fun showImageFromDb() {
        GlobalScope.launch {
            context.let {
                val imageList = ImageURIDatabase(it!!).ImageDAO().getAllEntries()
                if (imageList.isNotEmpty()) {
                    val imageItem = imageList[0]
//                    Handler(Looper.getMainLooper()).post(Runnable {
//                        image_view.setImageURI(imageItem.imageAddress.toUri())
//                    })
                    Handler(Looper.getMainLooper()).post(Runnable {
                        println("please dont be null: $image_view")
                    })
                    println("image_view is: $image_view")
                    println("image address is ${imageItem.imageAddress}")
                    println("image id is ${imageItem.id}")

                } else {
                    println("lol empty")
                }
            }

        }
    }



}

