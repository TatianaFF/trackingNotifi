package com.example.trackingnotifi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "mode_table")
class ModeModel (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo
    var title: String = "",

    @ColumnInfo
    var status: Boolean = false

) : Serializable