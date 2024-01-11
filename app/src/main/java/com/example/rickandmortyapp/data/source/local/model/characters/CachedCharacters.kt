package com.example.rickandmortyapp.data.source.local.model.characters

import androidx.room.*
import com.example.rickandmortyapp.domain.model.Characters

@Entity(tableName = "characters")
data class CachedCharacters(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val specie: String,
    val image: String,

){
    companion object{
        fun fronDomain(domainModel: Characters): CachedCharacters{

            return CachedCharacters(
                id = domainModel.id,
                name = domainModel.name,
                specie = domainModel.specie,
                image = domainModel.image

            )
        }
    }

    fun toDomain(): Characters {
        return Characters(
            id = id,
            name = name,
            specie = specie,
            image = image,
        )
    }
}
