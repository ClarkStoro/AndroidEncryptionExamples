package com.clarkstoro.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.clarkstoro.data.local_db.converters.CommonConverters
import com.clarkstoro.data.local_db.daos.ExampleDao
import com.clarkstoro.data.local_db.entities.ExampleDaoModel

@Database(
    entities = [
        // TODO: Add your DAO models.
        ExampleDaoModel::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CommonConverters::class)
abstract class AppDatabase : RoomDatabase() {
    // TODO: Add your DAOs.
    abstract fun exampleDao(): ExampleDao

    companion object { const val DATABASE_NAME = "AppDatabase" }
}