package com.mastertechsoftware.etsy

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.mastertechsoftware.etsy.listings.ListingAdapter
import com.mastertechsoftware.etsy.listings.ListingViewModel
import com.mastertechsoftware.etsy.models.Result
import com.pawegio.kandroid.find
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main (and only) Activity
 */
class MainActivity : AppCompatActivity() {
    val adapter = ListingAdapter()
    var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val progressBar = find<ProgressBar>(R.id.progressBar)
        val recyclerView = find<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.listingViewModel = ListingViewModel()

        // Scroll Listener. Check to see if we have
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.scrollState != SCROLL_STATE_DRAGGING) {
                    return
                }
                // Scrolling down
                if (dy > 0) {
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val nextPage = lastVisiblePosition + ListingViewModel.PAGE_SIZE
                    if (!loading && (nextPage > adapter.listingViewModel.currentOffset)
                            && adapter.listings.size < nextPage) {
                        Log.e("Etsy", "onScrolled: Loading new data")
                        loading = true
                        adapter.listingViewModel.getNextListings()
                                .subscribe { listings ->
                                    Log.d("Etsy", "onBindViewHolder: Found ${listings.size} Results ")
                                    loading = false
                                    adapter.addListings(listings as ArrayList<Result>)
                                }
                    }

                }
            }
        })

        // Search for Keywords. Show Dialog
        fab.setOnClickListener { _ ->
            val builder = AlertDialog.Builder(this)
            val search_view = LayoutInflater.from(this).inflate(R.layout.search_input, null, false)
            builder.setTitle(R.string.search_title).setView(search_view).setNegativeButton(R.string.cancel, { dialog: DialogInterface, which: Int ->
                run {
                    dialog.cancel()
                }
            }).setPositiveButton(R.string.search_button, { dialog: DialogInterface, which: Int ->
                run {
                    val keywords = search_view.find<EditText>(R.id.search_entry).text.toString()
                    if (keywords.isNotEmpty()) {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        adapter.listingViewModel.getListings(keywords, 0).subscribe { listings ->
                            Log.e("Etsy", "MainActivity: Found ${listings.size} Results ")
                            adapter.listings = listings as ArrayList<Result>
                            recyclerView.scrollToPosition(0)
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                        }
                    }
                }
            }).show()

        }
    }

    companion object {
        const val VISIBLE_THRESHOLD = 5
    }
}
