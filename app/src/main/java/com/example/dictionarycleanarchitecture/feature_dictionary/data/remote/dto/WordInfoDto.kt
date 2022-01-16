package com.example.dictionarycleanarchitecture.feature_dictionary.data.remote.dto

import com.example.dictionarycleanarchitecture.feature_dictionary.data.local.entity.WordInfoEntity

// we use dataTransferObjects to be able to ignore info we don't want in our UI

data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<PhoneticDto>,
    val word: String
) {
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            origin = origin,
            phonetic = phonetic,
            word = word
        )
    }
}