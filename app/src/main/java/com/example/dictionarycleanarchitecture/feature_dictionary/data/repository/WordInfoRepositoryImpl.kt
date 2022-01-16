package com.example.dictionarycleanarchitecture.feature_dictionary.data.repository

import com.example.dictionarycleanarchitecture.core.util.Resource
import com.example.dictionarycleanarchitecture.feature_dictionary.data.local.WordInfoDao
import com.example.dictionarycleanarchitecture.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionarycleanarchitecture.feature_dictionary.domain.model.WordInfo
import com.example.dictionarycleanarchitecture.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
) : WordInfoRepository {

    // the work of the repository is to take all the data sources and combine them in order to
    // have one single source of data or single source of truth

    // db access is quicker than network calls
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        // emmit the already saved data to the loading state
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        // In case of catching we never show directly the data from the API, we retrieve the data,
        // store it in the DB and after that we show the saved data in the UI
        try {
            val remoteWordInfos = api.getWordInfo(word)

            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })

            // in case of errors make sure the user knows it
            // already has the cached information
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos
                )
            )
        }
        // happy path

        // send the data from the DB to the UI
        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}