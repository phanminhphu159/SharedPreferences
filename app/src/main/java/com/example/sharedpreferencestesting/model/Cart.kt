package com.example.sharedpreferencestesting.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    var uid: Int,
    var cartName: String,
    var listItem: MutableList<Items>?
) : Parcelable

