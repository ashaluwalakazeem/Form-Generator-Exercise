package com.milsat.core.data.db.entities

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.milsat.core.domain.model.UIType

@Entity(tableName = "fields")
@Stable
data class FieldsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val formId: Int,
    val columnName: String,
    val pageName: String,
    val columnTitle: String,
    val columnType: String,
    val columnValue: String,
    val required: Boolean,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val showOnList: Boolean = false,
    val uiType: UIType,
    val values: List<String>?,
)
