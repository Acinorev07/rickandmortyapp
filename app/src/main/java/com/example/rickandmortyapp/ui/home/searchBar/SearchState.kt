package com.example.rickandmortyapp.ui.home.searchBar

import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.data.result.Result

data class SearchState(
    val characters: Result<List<Characters>> = Result.Success(emptyList()),
    val input: String = "",
    val isLoading:Boolean = false,
    val page: Int = 1
)
