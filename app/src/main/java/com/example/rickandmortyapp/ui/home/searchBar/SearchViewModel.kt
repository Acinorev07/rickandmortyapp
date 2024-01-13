package com.example.rickandmortyapp.ui.home.searchBar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.data.result.ErrorEntity
import com.example.rickandmortyapp.domain.model.info.Info
import com.example.rickandmortyapp.domain.use_cases.FindGetCharacters
import com.example.rickandmortyapp.domain.use_cases.GetMoreCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.util.RUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val findGetCharactersUseCase: FindGetCharacters,
    private val getMoreCharacters: GetMoreCharacters
): ViewModel() {

    companion object {
        const val UI_PAGE_SIZE = Info.DEFAULT_PAGE_SIZE
    }

    var state by mutableStateOf(SearchState())
        private set

    private val _eventFlow = MutableSharedFlow<RUIEvent>()
     val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null
    private var totalPages = 1

    fun onEvent(event: SearchEvent){
        when (event){
            is SearchEvent.EnteredCharacter-> state = state.copy(input = event.value)
        }
    }

    fun findGetCharacters(input:String, press:Boolean = false){
        searchJob?.cancel()
        if (press){
            resetSearchState()
            findGetCharacters(input)
        }else{
            if((totalPages > 1) && (state.page < totalPages)){
                state = state.copy(page = state.page + 1)
                findGetCharacters(input)
            }
        }
    }

    private fun findGetCharacters(input: String){
        searchJob = viewModelScope.launch{
            delay(1000L)
            state = state.copy(isLoading = true)
            getMoreCharacters(state.page, input).also{result ->

                when(result){
                    is Result.Error2->{
                        when(result.error){
                            ErrorEntity.ApiError.NotFound->{
                                _eventFlow.emit(RUIEvent.ShowSnackBar(
                                    message = RUiText.StringResource(R.string.io_exception_error)
                                ))
                                findGetCharactersUseCase(input, UI_PAGE_SIZE, (UI_PAGE_SIZE*(state.page - 1))).collect { result ->
                                    when (result) {
                                        is Result.Success -> {
                                            val currentCharacters = result.data ?: emptyList()
                                            state = state.copy(characters = Result.Success(currentCharacters), isLoading = false)
                                        }
                                        is Result.Error2 -> {
                                            // Manejar errores según sea necesario
                                        }
                                        is Result.Loading ->{}
                                        is Result.Error->{}
                                    }
                                }
                            }
                            ErrorEntity.ApiError.UnKnown ->{
                                _eventFlow.emit(RUIEvent.ShowSnackBar(
                                    message = RUiText.StringResource(R.string.unknown_exception_error)
                                ))
                                findGetCharactersUseCase(input, UI_PAGE_SIZE, (UI_PAGE_SIZE*(state.page - 1))).collect {
                                    state = state.copy(
                                        characters = it,
                                        isLoading = false
                                    )
                                }
                            }
                            ErrorEntity.InputError.NameError -> {
                                _eventFlow.emit(RUIEvent.ShowSnackBar(
                                    message = RUiText.StringResource(R.string.character_error,2)
                                ))
                            }
                        }
                    }
                    is Result.Success -> {
                        totalPages = result.data?.pages ?: 41
                        findGetCharactersUseCase(input, UI_PAGE_SIZE, (UI_PAGE_SIZE * (state.page - 1))).collect { innerResult ->
                            when (innerResult) {
                                is Result.Success -> {
                                    val currentCharacters = innerResult.data ?: emptyList()
                                    appendCharacters(Result.Success(currentCharacters), state.characters)
                                }
                                is Result.Error2 -> {
                                    // Manejar errores según sea necesario
                                }
                                is Result.Loading -> {}
                                is Result.Error -> {}
                            }
                        }
                    }
                    is Result.Error->{}
                    is Result.Loading->{}
                }

            }
        }
    }

    fun onActiveChange(newActive: Boolean) {
        //Log.d("onActiveChange1","onActiveChange: ${state.active}")
        state = state.copy(
            isLoading = newActive
        )
        //Log.d("onActiveChange2","onActiveChange: ${state.active}")
    }
private fun appendCharacters(newCharactersResult: Result<List<Characters>>, oldCharactersResult: Result<List<Characters>>) {
    val current = when {
        newCharactersResult is Result.Success && oldCharactersResult is Result.Success -> {
            val combinedList = mutableListOf<Characters>()

            if (oldCharactersResult is Result.Success && oldCharactersResult.data != null) {
                combinedList.addAll(oldCharactersResult.data)
            }

            if (newCharactersResult is Result.Success && newCharactersResult.data != null) {
                combinedList.addAll(newCharactersResult.data)
            }
            Result.Success(combinedList)
        }
        newCharactersResult is Result.Error2 -> {
            // Manejar el error según sea necesario para el nuevo conjunto de caracteres
            // Puedes propagar el error al estado o tomar alguna otra acción
            oldCharactersResult
        }
        else -> {
            // Manejar otros casos según sea necesario
            // Puedes propagar el error al estado o tomar alguna otra acción
            oldCharactersResult
        }
    }

    state = state.copy(characters = current as Result<List<Characters>>, isLoading = false)

}
    private fun resetSearchState() {
        state = state.copy(characters = Result.Success(ArrayList()), page = 1)
        totalPages = 1
    }

    sealed class RUIEvent{
        data class ShowSnackBar(val message: RUiText): RUIEvent()
    }
}












