package com.smitcoderx.softageproject.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smitcoderx.softageproject.Models.LocationDetails

@Dao
interface ProjectDao {

    @Query("SELECT * FROM locationDetails")
    fun getAllLocations(): LiveData<List<LocationDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details: LocationDetails)

}
