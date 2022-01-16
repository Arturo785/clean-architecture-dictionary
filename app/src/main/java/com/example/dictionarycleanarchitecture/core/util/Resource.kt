package com.example.dictionarycleanarchitecture.core.util


// as the name implies is just an alias to be called with that name
typealias SimpleResource = Resource<Unit>


sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}