package com.example.rickandmortyapp.ui.detail

import com.example.rickandmortyapp.domain.model.Character

data class DetailState(
    val character: Character? = null,
    val isLoading: Boolean = false
)
