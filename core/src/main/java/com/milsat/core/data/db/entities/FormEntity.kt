package com.milsat.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forms")
data class FormEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
