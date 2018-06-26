package com.claritusconsulting.postman.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import java.util.*


object PostmanTypeConverters {
    private val gson = Gson()
    @TypeConverter
    @JvmStatic
    fun stringToSomeObjectList(data: String?): Map<String, String> {
        if (data == "null") {
            return Collections.emptyMap()
        }

        val listType = object : TypeToken<Map<String,String>>() {}.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun someObjectListToString(someObjects: Map<String,String>): String {
        return gson.toJson(someObjects)
    }
}