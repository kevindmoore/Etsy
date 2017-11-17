package com.mastertechsoftware.etsy.api

import com.mastertechsoftware.etsy.models.Results
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for Retrofit API calls
 */
interface EtsyAPIInterface {
    @GET("listings/active?api_key=ob5cp17p7q8hvw37wv4nopuf&includes=MainImage")
    fun getListings(@Query("keywords") keywords: String): Single<Results>
    @GET("listings/active?api_key=ob5cp17p7q8hvw37wv4nopuf&includes=MainImage&limit=25")
    fun getListingsByPage(@Query("keywords") keywords: String, @Query("offset") offset: Int): Single<Results>
}