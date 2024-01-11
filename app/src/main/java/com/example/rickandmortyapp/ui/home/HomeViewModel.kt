package com.example.rickandmortyapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.result.Result

import com.example.rickandmortyapp.domain.use_cases.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
        private val getCharactersUseCase: GetCharactersUseCase
        ): ViewModel() {

    var state by mutableStateOf(HomeState(isLoading = true))
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPage = 1

    init {
        getCharacters(increase = false)
    }

    fun getCharacters(increase: Boolean){
        viewModelScope.launch{
            if (increase) currentPage ++ else if (currentPage > 1) currentPage --

            val showPrevious = currentPage > 1
            val showNext = currentPage < 42

            getCharactersUseCase(currentPage).onEach {result->

                when (result){
                    is Result.Success->{
                        state = state.copy(
                            characters = result.data ?: emptyList(),
                            isLoading = false,
                            showPrevious = showPrevious,
                            showNext = showNext
                        )
                    }
                    is Result.Error->{
                        state = state.copy(
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.showSnackBar(
                            result.message ?: "Unknown error"
                        ))
                    }
                    is Result.Loading->{
                        state = state.copy(
                            isLoading = true
                        )
                    }
                    is Result.Error2 -> {
                        // Manejar el caso Error2 aquí
                        // Puedes acceder a la propiedad error de la siguiente manera:
                        // result.error
                    }
                    else -> {
                        // Manejar cualquier otra subclase que pueda aparecer en el futuro
                    }
                }

            }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class showSnackBar(val message: String): UIEvent()
    }
}
