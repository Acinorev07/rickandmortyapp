package com.example.rickandmortyapp.data.source.remote.mappers

interface Mapper<E, D> {
    fun mapToDomain(apiEntity: E): D
}