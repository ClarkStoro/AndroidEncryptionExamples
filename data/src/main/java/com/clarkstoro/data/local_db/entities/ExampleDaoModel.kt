package com.clarkstoro.data.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.clarkstoro.data.local_db.entities.ExampleDaoModel.Companion.EXAMPLE_TABLE_NAME
import java.util.Date

@Entity(tableName = EXAMPLE_TABLE_NAME)
data class ExampleDaoModel (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = TEST_COLUMN_NAME) var test: String? = "Test",
    @ColumnInfo(name = TEST_COLUMN_NAME2) var test2: Date? = Date(),
) {
    companion object {
        const val EXAMPLE_TABLE_NAME = "ExampleTable"
        const val TEST_COLUMN_NAME = "test_column"
        const val TEST_COLUMN_NAME2 = "test_column2"
    }
}