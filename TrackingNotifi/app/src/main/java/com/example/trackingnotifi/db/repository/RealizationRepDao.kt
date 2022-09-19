package com.example.trackingnotifi.db.repository

import androidx.lifecycle.LiveData
import com.example.trackingnotifi.db.dao.IDao
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel
import com.example.trackingnotifi.models.NotifiModel

// репозиторий IModeRepository нужен для имплементации всех его методов,
// Dao описывает(реализует) что нужно делать БД при вызове методов
// теперь эти методы можно вызывать и передавать им данные
class RealizationRepDao(private val iDao:IDao): IRepository {

    //Mode
    override val allModes: LiveData<List<ModeModel>>
        get() = iDao.getAllModes()

    override suspend fun insertMode(modeModel: ModeModel) {
        iDao.insertMode(modeModel)
    }

    override suspend fun deleteMode(modeModel: ModeModel) {
        iDao.deleteMode(modeModel)
    }

    override suspend fun updateMode(modeModel: ModeModel) {
        iDao.updateMode(modeModel)
    }

    //App
    override val allApps: LiveData<List<AppModel>>
        get() = iDao.getAllApps()

    override suspend fun insertApp(appModel: AppModel) {
        iDao.insertApp(appModel)
    }

    override suspend fun deleteApp(appModel: AppModel) {
        iDao.deleteApp(appModel)
    }

    override fun getAppsByTitleMode(titleMode: String): LiveData<List<AppModel>> {
        return iDao.getAppsByTitleMode(titleMode)
    }

    //Mode_App
    override val allModeApp: LiveData<List<Mode_AppModel>>
        get() = iDao.getAllModeApp()

    override suspend fun insertModeApp(modeAppModel: Mode_AppModel) {
        iDao.insertModeApp(modeAppModel)
    }

    override suspend fun deleteModeApp(modeAppModel: Mode_AppModel) {
        iDao.deleteModeApp(modeAppModel)
    }

    override fun getAllModeAppByTitleMode(titleMode: String): LiveData<List<Mode_AppModel>> {
        return iDao.getAllModeAppByTitleMode(titleMode)
    }

    //NotifiModel
    override val allNotifi: LiveData<List<NotifiModel>>
        get() = iDao.getAllNotifi()

    override suspend fun insertNotifi(notifi: NotifiModel) {
        iDao.insertNotifi(notifi)
    }

    override suspend fun deleteNotifi(notifi: NotifiModel) {
        iDao.deleteNotifi(notifi)
    }

}