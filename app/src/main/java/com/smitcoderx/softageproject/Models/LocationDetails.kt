package com.smitcoderx.softageproject.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationDetails")

data class LocationDetails(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "Latitude")
    var lat: String?,
    @ColumnInfo(name = "Longitude")
    var long: String?,
    @ColumnInfo(name = "Date")
    var date: String?,
    @ColumnInfo(name = "Status")
    var status: String?
) {
    constructor() : this(null, null, null, null, null)
}

