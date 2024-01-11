package com.example.rickandmortyapp.domain.model

import com.example.rickandmortyapp.domain.model.info.Info
import com.example.rickandmortyapp.data.source.remote.dto.ApiResult
data class PaginatedCharacters(
    val info: Info,
    val results: List<Characters>

    )
