package com.smitcoderx.softageproject.Repository

import com.smitcoderx.softageproject.Db.ProjectDatabase
import com.smitcoderx.softageproject.Models.LocationDetails

class ProjectRepository(
    private val db: ProjectDatabase
) {

    suspend fun insertLocation(details: LocationDetails) = db.getProjectDao().insert(details)

    fun allLocations() = db.getProjectDao().getAllLocations()

}