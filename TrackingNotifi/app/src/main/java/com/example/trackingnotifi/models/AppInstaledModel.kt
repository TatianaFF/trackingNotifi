package com.example.trackingnotifi.models

import android.graphics.drawable.Drawable
import java.io.Serializable


class AppInstaledModel(
    var id: Long = 0,

    var title: String = "",

    var pack: String = "",

    var icon: Drawable,

    var ischecked: Boolean = false
) : Serializable