package com.example.trackingnotifi.models

import android.content.pm.ChangedPackages
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "app_table")
data class AppModel (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    //убрать?
//    @ColumnInfo
//    var title: String = "",

    @ColumnInfo
    var pack: String = ""
) : Serializable