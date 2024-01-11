package com.example.rickandmortyapp.data.source.remote.mappers

import com.example.rickandmortyapp.data.source.remote.dto.ApiResult
import com.example.rickandmortyapp.data.source.remote.dto.CharacterDto
import com.example.rickandmortyapp.domain.model.Characters
import javax.inject.Inject

class CharactersMapper @Inject constructor():Mapper<ApiResult?, Characters> {

    override fun mapToDomain(apiEntity: ApiResult?):Characters{
        return Characters(
            id = apiEntity!!.id,
            name = apiEntity.name,
            specie= apiEntity.species ,
            image = apiEntity.image,

        )
    }

}