package com.example.dictionarycleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionarycleanarchitecture.feature_dictionary.presentation.WordInfoItem
import com.example.dictionarycleanarchitecture.feature_dictionary.presentation.WordInfoViewModel
import com.example.dictionarycleanarchitecture.ui.theme.DictionaryCleanArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryCleanArchitectureTheme {

                val viewModel: WordInfoViewModel = hiltViewModel()
                // this handles how the screen is shown at the moment with which data
                val state = viewModel.state.value

                //Contains basic screen state, e.g. Drawer configuration,
                // as well as sizes of components after layout has happened
                val scaffoldState = rememberScaffoldState()

                // will launch always but checking the inside condition
                LaunchedEffect(key1 = true) {

                    viewModel.eventFlow.collectLatest { event ->
                        // show snackBar in case of UI message
                        when (event) {
                            is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }

                // to manage the snackBar and the UI stuff
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    // to give a background and a box shape
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                    ) {
                        // from top to bottom
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Search...")
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // the items retrieved in a list form
                            // from top to bottom
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.wordInfoItems.size) { i ->
                                    val wordInfo = state.wordInfoItems[i]

                                    // for every item but not the first
                                    if (i > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    // the composable to show data
                                    WordInfoItem(wordInfo = wordInfo)

                                    // the little gray line to divide the words
                                    // but not to the last one
                                    if (i < state.wordInfoItems.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }

                        // also managed by scaffold
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                    }
                }
            }
        }
    }
}
