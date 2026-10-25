package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблица в базе данных
@Entity("pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var height: Int = 0,
    var weight: Int = 0
)