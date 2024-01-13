package com.example.rickandmortyapp.ui.home.searchBar

sealed class SearchEvent {
    data class EnteredCharacter(val value: String): SearchEvent()
}