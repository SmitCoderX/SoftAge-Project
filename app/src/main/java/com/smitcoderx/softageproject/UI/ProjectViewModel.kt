package com.smitcoderx.softageproject.UI

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.softageproject.Models.LocationDetails
import com.smitcoderx.softageproject.Repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(app: Application, val repository: ProjectRepository) :
    AndroidViewModel(app) {

    fun insert(details: LocationDetails) = viewModelScope.launch {
        repository.insertLocation(details)
    }

    fun allLocations() = repository.allLocations()
}