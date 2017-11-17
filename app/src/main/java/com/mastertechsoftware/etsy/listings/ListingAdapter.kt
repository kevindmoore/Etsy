package com.mastertechsoftware.etsy.listings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mastertechsoftware.etsy.R
import com.mastertechsoftware.etsy.models.Result
import com.pawegio.kandroid.find

/**
 * Adapter for Listings. Takes into account multiple pages
 */
class ListingAdapter : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {
    private var layoutInflator: LayoutInflater? = null
    lateinit var listingViewModel : ListingViewModel
    var listings = ArrayList<Result>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    // Add new listings to our existing list
    fun addListings(newListings : ArrayList<Result>) {
        notifyItemRangeChanged(listings.size, (listings.size + newListings.size - 1))
        listings.addAll(newListings)

    }

    /**
     * Create the viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        if (layoutInflator == null) {
            layoutInflator = LayoutInflater.from(parent.context)
        }
        return ListingViewHolder(layoutInflator?.inflate(R.layout.listing_item, parent, false)!!)
    }

    override fun getItemCount(): Int = listingViewModel.totalCount

    // Set the title & image
    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = listings[position]
        holder.titleView.text = listing.title
        Glide.with(holder.titleView.context)
                .load(listing.mainImage.url_75x75)
                .into(holder.imageView)
    }

    /**
     * Hold the 2 fields
     */
    class ListingViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView
        val titleView : TextView

        init {
            imageView = itemView.find(R.id.image)
            titleView = itemView.find(R.id.title)
        }
    }
}