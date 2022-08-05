package com.example.trackingnotifi.db.repository

import androidx.lifecycle.LiveData
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel
import com.example.trackingnotifi.models.NotifiModel

interface IRepository {

    //Mode

    val allModes: LiveData<List<ModeModel>>
    suspend fun insertMode(modeModel: ModeModel)
    suspend fun deleteMode(modeModel: ModeModel)
    suspend fun updateMode(modeModel: ModeModel)

    //App

    val allApps: LiveData<List<AppModel>>
    suspend fun insertApp(appModel: AppModel)
    suspend fun deleteApp(appModel: AppModel)
    fun getAppsByTitleMode(titleMode: String): LiveData<List<AppModel>>

    //Mode_AppModel

    val allModeApp: LiveData<List<Mode_AppModel>>
    suspend fun insertModeApp(modeAppModel: Mode_AppModel)
    suspend fun deleteModeApp(modeAppModel: Mode_AppModel)
    fun getAllModeAppByTitleMode(titleMode: String): LiveData<List<Mode_AppModel>>

    //NotifiModel

    val allNotifi: LiveData<List<NotifiModel>>
    suspend fun insertNotifi(notifi: NotifiModel)
    suspend fun deleteNotifi(notifi: NotifiModel)
}


//, onSuccess:() -> Unit