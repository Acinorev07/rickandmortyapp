package com.example.rickandmortyapp.data.source.remote.dto

data class InfoDto(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)