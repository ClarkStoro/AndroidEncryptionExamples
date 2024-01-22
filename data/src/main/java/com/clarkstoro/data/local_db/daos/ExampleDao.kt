package com.clarkstoro.data.local_db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clarkstoro.data.local_db.entities.ExampleDaoModel
import com.clarkstoro.data.local_db.entities.ExampleDaoModel.Companion.EXAMPLE_TABLE_NAME

@Dao
interface ExampleDao {
    // Use constants to refer to table and columns names.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExampleDaoModel(exampleToInsert: ExampleDaoModel)

    @Query("SELECT * FROM $EXAMPLE_TABLE_NAME")
    fun retrieveAll(): List<ExampleDaoModel>
}