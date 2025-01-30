package com.chaitanya.mangashelf.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chaitanya.mangashelf.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(mangas: List<MangaEntity>)

    @Query("SELECT * FROM mangas")
    suspend fun getAllMangas(): List<MangaEntity>

    @Query("SELECT * FROM mangas")
    fun getAllMangasFlow(): Flow<List<MangaEntity>>

    @Query("UPDATE mangas SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE mangas SET isRead = 1 WHERE id = :id")
    suspend fun updateReadStatus(id: String)
}