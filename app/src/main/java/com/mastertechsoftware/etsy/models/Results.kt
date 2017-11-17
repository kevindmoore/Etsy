package com.mastertechsoftware.etsy.models

/**
 * Main entry point for JSON
 */
data class Results(var count : Int = 0, var results : List<Result> = ArrayList<Result>())