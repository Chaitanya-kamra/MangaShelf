package com.chaitanya.mangashelf.ui

import com.chaitanya.mangashelf.api.ApiService
import com.chaitanya.mangashelf.local.MangaDao
import com.chaitanya.mangashelf.local.entity.MangaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val api: ApiService,
    private val dao: MangaDao
) {

    suspend fun fetchAndCacheMangas() {
        withContext(Dispatchers.IO) {
                val mangaList = api.getMangas()
                val existingMangas = dao.getAllMangas().associateBy { it.id }
                dao.insertMangas(mangaList.map {
                    val existingManga = existingMangas[it.id]
                    MangaEntity(
                        id = it.id,
                        image = it.image,
                        score = it.score,
                        popularity = it.popularity,
                        title = it.title,
                        publishedChapterDate = it.publishedChapterDate,
                        category = it.category,
                        isFavorite = existingManga?.isFavorite ?: false,
                        isRead = existingManga?.isRead ?: false
                    )
                })
        }
    }

    fun getAllMangasFlow(): Flow<List<MangaEntity>> = dao.getAllMangasFlow()
    suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }
    suspend fun updateReadStatus(id: String) {
        dao.updateReadStatus(id)
    }

}

