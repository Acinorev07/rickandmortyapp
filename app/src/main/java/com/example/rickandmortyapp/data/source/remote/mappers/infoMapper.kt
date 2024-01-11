package com.example.rickandmortyapp.data.source.remote.mappers

import com.example.rickandmortyapp.data.source.remote.dto.InfoDto
import com.example.rickandmortyapp.domain.model.info.Info
import javax.inject.Inject

class InfoMapper @Inject constructor(): Mapper<InfoDto?, Info> {
    override fun mapToDomain(apiEntity: InfoDto?): Info {
        return Info(pages = apiEntity?.pages ?: 0)
    }
}