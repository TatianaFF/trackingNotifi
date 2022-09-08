package com.example.trackingnotifi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

//@Entity(tableName = "notifi_table", foreignKeys = arrayOf(
//    ForeignKey(
//        entity = AppModel::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id_app"),
//        onDelete = ForeignKey.NO_ACTION
//    )
//))
@Entity(tableName = "notifi_table")
data class NotifiModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

//    @ColumnInfo
//    var id_app: Int = 0,

    @ColumnInfo
    var pack: String = "",

    @ColumnInfo
    var from: String = "",

    @ColumnInfo
    var date: String = "",

    @ColumnInfo
    var message: String = ""
) : Serializable