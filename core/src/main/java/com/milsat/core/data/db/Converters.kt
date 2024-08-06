package com.milsat.core.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.UIType

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }
}


class UITypeConverter {
    @TypeConverter
    fun fromUIType(value: UIType): String {
        return value.name.lowercase()
    }

    @TypeConverter
    fun toUIType(value: String): UIType {
        return UIType.valueOf(value.uppercase())
    }
}

class ColumnTypeConverter {
    @TypeConverter
    fun fromColumnType(value: ColumnType): String {
        return value.name
    }

    @TypeConverter
    fun toColumnType(value: String): ColumnType {
        return ColumnType.valueOf(value)
    }
}