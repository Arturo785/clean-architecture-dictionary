package com.example.dictionarycleanarchitecture.feature_dictionary.presentation

import com.example.dictionarycleanarchitecture.feature_dictionary.domain.model.WordInfo


// represents the state or how a screen of the app would contain
data class WordInfoState(
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false
)