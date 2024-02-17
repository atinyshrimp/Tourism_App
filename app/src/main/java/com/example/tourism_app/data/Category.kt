package com.example.tourism_app.data

import androidx.annotation.DrawableRes

data class Category (
    val name : String ?= null,

    // have to change to the actual way to get an image from Firebase
    @DrawableRes val imageId : Int ?= null
)