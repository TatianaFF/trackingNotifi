package com.example.trackingnotifi.screens.ListOfModes

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.Display
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

class ModesViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

    fun initDatabase(){
        val daoMode = ModeDatabase.getInstance(context).getNoteDao()
        REPOSITORY = RealizationRepDao(daoMode)
    }

    fun getAllModes(): LiveData<List<ModeModel>> {
        return REPOSITORY.allModes
    }

    fun getAppsByTitleMode(titleMode: String): LiveData<List<AppModel>>{
        return REPOSITORY.getAppsByTitleMode(titleMode)
    }

    fun updateMode(modeModel: ModeModel) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.updateMode(modeModel)
        }

    @SuppressLint("QueryPermissionsNeeded", "WrongConstant")
    fun getInstaledApps(): List<AppInstaledModel> {
        val pm: PackageManager = context.packageManager
        val listPMInfo: MutableList<ApplicationInfo> = pm.getInstalledApplications(PackageManager.GET_META_DATA)         //GET_META_DATA сравнить
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
}