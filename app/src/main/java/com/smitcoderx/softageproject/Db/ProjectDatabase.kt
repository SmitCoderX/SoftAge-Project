package com.smitcoderx.softageproject.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smitcoderx.softageproject.Models.LocationDetails

@Database(
    entities = [LocationDetails::class],
    version = 1
)

abstract class ProjectDatabase : RoomDatabase() {

    abstract fun getProjectDao(): ProjectDao

    companion object {

        @Volatile
        private var instance: ProjectDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProjectDatabase::class.java,
                "locations_db.db"
            ).fallbackToDestructiveMigration().build()

    }

}