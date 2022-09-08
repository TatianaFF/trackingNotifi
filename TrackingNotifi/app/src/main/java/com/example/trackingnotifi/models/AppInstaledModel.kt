package com.example.trackingnotifi.models

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable


data class AppInstaledModel(
    var id: Long = 0,

    var title: String = "",

    var pack: String = "",

    //ссылка на картинку
    var icon: Drawable?,

    var ischecked: Boolean = false
) : Serializable
