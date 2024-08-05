package com.milsat.core.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.milsat.core.data.db.entities.FieldsEntity


@Database(entities = [FieldsEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CapstoneDatabase : RoomDatabase() {

    abstract fun capstoneDao(): CapstoneDao
}