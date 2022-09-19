package com.example.trackingnotifi.screens.ListOfModes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
}