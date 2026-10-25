package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
// Интерфейс с SQL-запросами для работы с таблицей покемонов
@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemons")
    fun getAll(): List<PokemonEntity>

    @Insert
    fun insert(pokemon: PokemonEntity)

    @Update
    fun update(pokemon: PokemonEntity)

    @Delete
    fun delete(pokemon: PokemonEntity)
}