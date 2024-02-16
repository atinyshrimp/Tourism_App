package com.example.tourism_app

// class representing the "Lieu" object in our Firebase database
data class Activity(
    val name : String ?= null,
    var address: String? = null,
    var description: String? = null,
    var transport: List<List<String>> ?= null,
    var hours: String? = null,
    var category: String ?= null
)