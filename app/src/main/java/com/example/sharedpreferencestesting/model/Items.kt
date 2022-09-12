package com.example.sharedpreferencestesting.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    var id: Int?,
    var itemName: String,
    var quantity: Int?,
): Parcelable

