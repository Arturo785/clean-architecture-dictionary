package com.example.dictionarycleanarchitecture.feature_dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dictionarycleanarchitecture.feature_dictionary.data.local.entity.WordInfoEntity


@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class) // to handle the list of objects in the entity class
abstract class WordInfoDatabase : RoomDatabase() {

    abstract val dao: WordInfoDao
}