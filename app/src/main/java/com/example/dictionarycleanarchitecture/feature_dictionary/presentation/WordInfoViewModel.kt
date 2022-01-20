package com.example.dictionarycleanarchitecture.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarycleanarchitecture.core.util.Resource
import com.example.dictionarycleanarchitecture.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    // public and private accessors as in liveData

    // these are compose states
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // this handles how the screen is shown at the moment with which data
    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state


    // this one handles the UI events and because should be one time event uses
    // sharedFlows
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    // handles already made searches
    private var searchJob: Job? = null

    fun onSearch(query: String) {
        // updates the mutable state and cancels previous searches
        _searchQuery.value = query
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            // to avoid a lot of searches
            delay(500L)

            // this is the use case and returns a flow
            getWordInfo(query)
                .onEach { result -> // handles every emmition of the flow
                    when (result) {
                        is Resource.Success -> {
                            // sets the result to the state of what to display in the UI
                            // and updates with the new data
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            // updates with the cached data if available
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )

                            // emmit a one time flow about the error
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                    result.message ?: "Unknown error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            // updates with the cached data if available
                            // and loading status
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this) // gets launched on the scope is inside
            // in this case viewModelScope
        }
    }


    // this is used to send one time events to the UI
    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }


}