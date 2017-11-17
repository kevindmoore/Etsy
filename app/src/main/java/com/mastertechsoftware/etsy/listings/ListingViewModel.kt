package com.mastertechsoftware.etsy.listings

import android.util.Log
import com.mastertechsoftware.etsy.api.EtsyAPI
import com.mastertechsoftware.etsy.models.Result
import com.mastertechsoftware.etsy.models.Results
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Model for Listings. Holds paging info
 */
class ListingViewModel {
    var totalCount = 0
    var currentOffset = 0
    var currentKeyword = ""
    var lastOffsetLoaded = 0

    /**
     * Get the previous page of listings. Currently not used
     * This was to be used if I had just a buffer of data in memory
     */
    fun getPreviousListings() : Single<List<Result>> {
        currentOffset -= PAGE_SIZE
        currentOffset = Math.max(0, currentOffset)
        return EtsyAPI.getListingsByPage(currentKeyword, currentOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{error -> Log.e("Etsy", "Problems getting results ${error}")}
                .onErrorReturn { Results() }
                .map { results -> results.results}

    }

    /**
     * Get the next page of data
     */
    fun getNextListings() : Single<List<Result>> {
        currentOffset += PAGE_SIZE
        currentOffset = Math.min(currentOffset, totalCount-PAGE_SIZE)
        lastOffsetLoaded = currentOffset
        return EtsyAPI.getListingsByPage(currentKeyword, currentOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{error -> Log.e("Etsy", "Problems getting results ${error}")}
                .onErrorReturn { Results() }
                .map { results -> results.results}

    }

    /**
     * Get a specific page of data. Currently just used for the first search
     */
    fun getListings(keywords : String, offset: Int) : Single<List<Result>> {
        currentOffset = offset
        lastOffsetLoaded = currentOffset
        currentKeyword = keywords
        totalCount = 0
        return EtsyAPI.getListingsByPage(keywords, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{error -> Log.e("Etsy", "Problems getting results ${error}")}
                .onErrorReturn { Results() }
                .doOnSuccess { results -> totalCount = results.count; Log.e("Etsy", "Setting totalCount to ${totalCount}") }
                .map { results -> results.results}

    }

    companion object {
        const val PAGE_SIZE = 25
    }
}