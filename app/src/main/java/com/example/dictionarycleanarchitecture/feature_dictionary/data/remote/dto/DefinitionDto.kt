package com.example.dictionarycleanarchitecture.feature_dictionary.data.remote.dto

import com.example.dictionarycleanarchitecture.feature_dictionary.domain.model.Definition

// we use dataTransferObjects to be able to ignore info we don't want in our UI

data class DefinitionDto(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
) {
    fun toDefinition(): Definition {
        return Definition(
            antonyms = antonyms,
            definition = definition,
            example = example,
            synonyms = synonyms
        )
    }
}