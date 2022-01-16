package com.example.dictionarycleanarchitecture.feature_dictionary.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.dictionarycleanarchitecture.feature_dictionary.data.util.JsonParser
import com.example.dictionarycleanarchitecture.feature_dictionary.domain.model.Meaning
import com.google.gson.reflect.TypeToken


// means we created it and to room to know that we did it
@ProvidedTypeConverter
class Converters(
    // uses our interface and passed in the injection
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: "[]"
    }
}