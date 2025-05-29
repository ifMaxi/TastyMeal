package com.maxidev.tastymeal.utils

import android.net.Uri
import androidx.core.net.toUri
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

    @TypeConverter
    fun fromString(value: String?): Uri? {
        return value?.toUri()
    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun fromStrings(value: String?): List<Uri?> {
        return value?.toUri().toString().split("||").map { it.toUri() }
    }

    @TypeConverter
    fun fromUriList(uriList: List<Uri?>): String? {
        return uriList.joinToString("||")
    }
}