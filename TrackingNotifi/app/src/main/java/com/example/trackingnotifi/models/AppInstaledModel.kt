package com.example.trackingnotifi.models

import android.graphics.drawable.Drawable


class AppInstaledModel(
    var id: Long = 0,

    var title: String = "",

    var pack: String = "",

    var icon: Drawable,

    var ischecked: Boolean = false
)