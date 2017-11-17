package com.mastertechsoftware.etsy.models

import com.squareup.moshi.Json

/**
 * Hold individual Result
 */
data class Result(var listing_id: Int, var title : String, var description : String,
                  var prices : Double, var url : String,
                  @Json(name = "MainImage") var mainImage: MainImage)
