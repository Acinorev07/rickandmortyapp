package com.example.rickandmortyapp.data.source.remote.dto

import com.example.rickandmortyapp.data.source.remote.dto.InfoDto

import com.google.gson.annotations.SerializedName

data class ApiPaginatedCharacters(
    val info: InfoDto?,
    @SerializedName("results") val characters: List<CharacterDto>?
)
