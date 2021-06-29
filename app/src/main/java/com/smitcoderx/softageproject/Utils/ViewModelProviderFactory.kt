package com.smitcoderx.softageproject.Utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smitcoderx.softageproject.Repository.ProjectRepository
import com.smitcoderx.softageproject.UI.ProjectViewModel

class ViewModelProviderFactory(
    val app: Application,
    val repository: ProjectRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectViewModel(app, repository) as T
    }
}