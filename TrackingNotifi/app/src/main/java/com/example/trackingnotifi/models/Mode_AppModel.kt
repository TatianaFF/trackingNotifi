package com.example.trackingnotifi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "mode_app_table"
//    , foreignKeys = arrayOf(
//    ForeignKey(
//        entity = ModeModel::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id_mode"),
//        onDelete = ForeignKey.CASCADE
//    ),
//    ForeignKey(
//        entity = AppModel::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id_app"),
//        onDelete = ForeignKey.CASCADE
//    )
//)
)
class Mode_AppModel (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo
    var title_mode: String = "",

    @ColumnInfo
    var pack_app: String = ""

) : Serializable

//    var id_mode: Long = 0,
//    var id_app: Long = 0