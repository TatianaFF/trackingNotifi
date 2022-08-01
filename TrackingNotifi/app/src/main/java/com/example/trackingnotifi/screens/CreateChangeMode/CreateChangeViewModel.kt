package com.example.trackingnotifi.screens.CreateChangeMode

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.ModeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.trackingnotifi.models.AppInstaledModel
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.Mode_AppModel
import kotlinx.coroutines.delay
import java.lang.Exception
import java.util.ArrayList


class CreateChangeViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    fun initDatabase(){
        val daoMode = ModeDatabase.getInstance(context).getNoteDao()
        REPOSITORY = RealizationRepDao(daoMode)
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

    fun getAllModes(): LiveData<List<ModeModel>> {
        return REPOSITORY.allModes
    }

    @SuppressLint("QueryPermissionsNeeded", "WrongConstant")
    fun getInstaledApps(): List<AppInstaledModel> {
        val pm: PackageManager = context.packageManager
        val listPMInfo: MutableList<ApplicationInfo> = pm.getInstalledApplications(GET_META_DATA)         //GET_META_DATA сравнить
        var applicationInfo: ApplicationInfo
        val listAppInstaled: ArrayList<AppInstaledModel> = arrayListOf()
        var counter: Long = 0

        for(item in listPMInfo){
            try {
                applicationInfo = pm.getApplicationInfo(item.packageName, 1)

                //create AppInstaledModel, НЕ сохранение в БД
                listAppInstaled.add(AppInstaledModel(
                    counter,
                    pm.getApplicationLabel(applicationInfo) as String,
                    item.packageName,
                    pm.getApplicationIcon(applicationInfo)
                ))
            }
            catch (e: Exception){
                e.message?.let { Log.e("Error getApps: ", it) }
            }
            counter++
        }
        return listAppInstaled
    }
}