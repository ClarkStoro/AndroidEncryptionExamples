package com.clarkstoro.data.local_db.converters

import androidx.room.TypeConverter
import java.util.Date


class CommonConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}