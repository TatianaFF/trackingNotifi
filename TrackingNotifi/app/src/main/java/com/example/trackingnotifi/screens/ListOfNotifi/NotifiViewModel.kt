package com.example.trackingnotifi.screens.ListOfNotifi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.NotifiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotifiViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

    fun initDatabase(){
        val daoMode = ModeDatabase.getInstance(context).getNoteDao()
        REPOSITORY = RealizationRepDao(daoMode)
    }

    fun getAllNotifi(): LiveData<List<NotifiModel>> {
        return REPOSITORY.allNotifi
    }

    fun insertNotifi(notifi: NotifiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertNotifi(notifi)
        }
    }

    fun deleteNotifi(notifi: NotifiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteNotifi(notifi)
        }
    }


}