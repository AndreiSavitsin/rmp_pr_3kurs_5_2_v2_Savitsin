package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import androidx.room.Database
import androidx.room.RoomDatabase

// Главный класс базы данных. Связывает таблицу и методы
@Database(entities = [PokemonEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    // Метод для получения доступа к операциям с базой
    abstract fun pokemonDao(): PokemonDao
}