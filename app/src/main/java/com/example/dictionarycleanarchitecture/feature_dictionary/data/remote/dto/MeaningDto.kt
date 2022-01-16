package com.example.dictionarycleanarchitecture.feature_dictionary.data.remote.dto

import com.example.dictionarycleanarchitecture.feature_dictionary.domain.model.Meaning


// we use dataTransferObjects to be able to ignore info we don't want in our UI

data class MeaningDto(
    val definitions: List<DefinitionDto>,
    val partOfSpeech: String
) {
    fun toMeaning(): Meaning {
        return Meaning(
            definitions = definitions.map { it.toDefinition() },
            partOfSpeech = partOfSpeech
        )
    }
}