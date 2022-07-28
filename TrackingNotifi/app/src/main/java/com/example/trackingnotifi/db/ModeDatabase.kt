package com.example.trackingnotifi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trackingnotifi.db.dao.IDao
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel

@Database(entities = [ModeModel::class, AppModel::class, Mode_AppModel::class], version = 1)
abstract class ModeDatabase: RoomDatabase() {
    abstract fun getNoteDao(): IDao

    companion object{
        private var database: ModeDatabase ?= null

        @Synchronized
        fun getInstance(context: Context): ModeDatabase{
            return if (database == null){
                database = Room.databaseBuilder(context, ModeDatabase::class.java, "db").build()
                database as ModeDatabase
            } else{
                database as ModeDatabase
            }
        }
    }
}