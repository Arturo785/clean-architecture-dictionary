package com.example.dictionarycleanarchitecture.feature_dictionary.data.remote.dto


// we use dataTransferObjects to be able to ignore info we don't want in our UI

data class PhoneticDto(
    val audio: String,
    val text: String
)