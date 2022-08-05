package com.example.trackingnotifi.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel
import com.example.trackingnotifi.models.NotifiModel

@Dao
interface IDao {

    //Mode

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMode(modeModel: ModeModel)

    @Delete
    suspend fun deleteMode(modeModel: ModeModel)

    @Update
    suspend fun updateMode(modeModel: ModeModel)

    @Query("select * from mode_table")
    fun getAllModes(): LiveData<List<ModeModel>>

    //App

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertApp(appModel: AppModel)

    @Delete
    suspend fun deleteApp(appModel: AppModel)

    @Query("select * from app_table")
    fun getAllApps(): LiveData<List<AppModel>>

    @Query("select * from app_table where app_table.pack in (select mode_app_table.pack_app from mode_app_table where mode_app_table.title_mode = :titleMode)")
    fun getAppsByTitleMode(titleMode: String): LiveData<List<AppModel>>

    //Mode_App

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertModeApp(modeAppModel: Mode_AppModel)

    @Delete
    suspend fun deleteModeApp(modeAppModel: Mode_AppModel)

    @Query("select * from mode_app_table")
    fun getAllModeApp(): LiveData<List<Mode_AppModel>>

    @Query("select * from mode_app_table where mode_app_table.title_mode = :titleMode")
    fun getAllModeAppByTitleMode(titleMode: String): LiveData<List<Mode_AppModel>>

    //Notifi

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotifi(notifi: NotifiModel)

    @Delete
    suspend fun deleteNotifi(notifi: NotifiModel)

    @Query("select * from notifi_table")
    fun getAllNotifi(): LiveData<List<NotifiModel>>
}