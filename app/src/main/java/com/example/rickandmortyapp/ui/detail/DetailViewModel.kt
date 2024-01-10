package com.example.rickandmortyapp.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.domain.use_cases.GetCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.rickandmortyapp.data.result.Result

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var state by mutableStateOf(DetailState())
        private set
    init{
        getCharacter()
    }

    private fun getCharacter(){
        savedStateHandle.get<Int>("id")?.let{characterId->
            viewModelScope.launch{
                getCharacterUseCase(characterId).also{result->

                    when (result){
                        is Result.Success->{
                            state = state.copy(
                                character = result.data,
                                isLoading = false
                            )
                        }
                        is Result.Error->{
                            state = state.copy(
                                isLoading = false
                            )
                        }
                        is Result.Loading->{
                            state= state.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }
}