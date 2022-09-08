package com.example.trackingnotifi.screens.DetailFragment

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.Display
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.AppInstaledModel
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList

class DetailViewModel(application: Application): AndroidViewModel(application) {
    val context = application

    fun initDatabase(){
        val daoMode = ModeDatabase.getInstance(context).getNoteDao()
        REPOSITORY = RealizationRepDao(daoMode)
    }

    fun deleteMode(modeModel: ModeModel) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteMode(modeModel)
        }

    fun deleteApp(appModel: AppModel) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteApp(appModel)
        }

    fun deleteModeApp(modeAppModel: Mode_AppModel) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteModeApp(modeAppModel)
        }

    fun getAppsByTitleMode(titleMode: String): LiveData<List<AppModel>> {
        return REPOSITORY.getAppsByTitleMode(titleMode)
    }

    fun getAllModeAppByTitleMode(titleMode: String): LiveData<List<Mode_AppModel>> {
        return REPOSITORY.getAllModeAppByTitleMode(titleMode)
    }

    fun getAllModes(): LiveData<List<ModeModel>> {
        return REPOSITORY.allModes
    }

    @SuppressLint("QueryPermissionsNeeded", "WrongConstant")
    fun getInstaledApps(): List<AppInstaledModel> {
        val pm: PackageManager = context.packageManager
        val listPMInfo: List<ApplicationInfo> = pm.getInstalledApplications(PackageManager.PERMISSION_GRANTED)         //GET_META_DATA сравнить
        var applicationInfo: ApplicationInfo
        val listAppInstaled: ArrayList<AppInstaledModel> = arrayListOf()
        var counter: Long = 0

        for(item in listPMInfo){
            try {
                applicationInfo = pm.getApplicationInfo(item.packageName, 1)

                //create AppInstaledModel, НЕ сохранение в БД
                listAppInstaled.add(
                    AppInstaledModel(
                    counter,
                    pm.getApplicationLabel(applicationInfo) as String,
                    item.packageName,
//                    pm.getApplicationIcon(applicationInfo)
                            pm.getApplicationIcon(applicationInfo)
                )
                )
            }
            catch (e: Exception){
                e.message?.let { Log.e("Error getApps: ", it) }
            }
            counter++
        }
        return listAppInstaled
    }

    fun updateMode(modeModel: ModeModel) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.updateMode(modeModel)
        }

    fun insertApp(appModel: AppModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertApp(appModel)
        }
    }

    fun insertMode(modeModel: ModeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertMode(modeModel)
        }
    }

    fun insertModeApp(modeAppModel: Mode_AppModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertModeApp(modeAppModel)
        }
    }
}