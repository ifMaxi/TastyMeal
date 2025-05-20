package com.maxidev.tastymeal.utils

import androidx.room.TypeConverter

class ConvertersUtils {

    @TypeConverter
    fun fromStringToList(value: String): List<String> {

        return value.split(",")
    }

    @TypeConverter
    fun fromListToString(value: List<String>): String {

        return value.joinToString()
    }
}