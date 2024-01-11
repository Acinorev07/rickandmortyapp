package com.example.rickandmortyapp.data.source.remote.dto

import com.example.rickandmortyapp.domain.model.Characters
import com.google.gson.annotations.SerializedName

data class CharactersDto(
    val info: InfoDto,
    @SerializedName("results") val results: List<ApiResult>
)

fun CharactersDto.toListCharacters():List<Characters>{
    val resultEntries = results.mapIndexed{_ ,entries->
        Characters(
            id = entries.id,
            name = entries.name,
            specie = entries.species,
            image = entries.image
        )
    }
    return resultEntries
}