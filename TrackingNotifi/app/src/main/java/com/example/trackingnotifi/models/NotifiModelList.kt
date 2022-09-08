package com.example.trackingnotifi.models

import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import java.io.Serializable

data class NotifiModelList(
    var id: Long = 0,
    var name_app: String = "",
    var icon: Drawable,
    var from: String = "",
    var message: String = "",
    var date: String = "",
    var pack: String = ""
): Serializable
