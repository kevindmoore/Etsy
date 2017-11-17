package com.mastertechsoftware.etsy.api

import android.util.Log
import com.mastertechsoftware.etsy.models.Results
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton for getting Listings
 */
object EtsyAPI {
    private val service: EtsyAPIInterface

    // Build the Retrofit service
    init {
        val retrofit = Retrofit.Builder().baseUrl("https://api.etsy.com/v2/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create(EtsyAPIInterface::class.java)
    }

    fun getListings(keywords: String) : Single<Results> {
        return try {
            service.getListings(keywords)
        } catch (e : Exception) {
            Log.e("Etsy", "Problems getting results ${e}")
            Single.just(Results())
        }
    }

    fun getListingsByPage(keywords: String, offset: Int) : Single<Results> {
        return try {
            service.getListingsByPage(keywords, offset)
        } catch (e : Exception) {
            Log.e("Etsy", "Problems getting results ${e}")
            Single.just(Results())
        }
    }
}