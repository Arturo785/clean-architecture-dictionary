package com.example.dictionarycleanarchitecture.feature_dictionary.data.util

import java.lang.reflect.Type


// is an interface because we may use different libraries to parse the Json
// but the behaviour should stay the same
interface JsonParser {

    fun <T> fromJson(json: String, type: Type): T?

    fun <T> toJson(obj: T, type: Type): String?
}