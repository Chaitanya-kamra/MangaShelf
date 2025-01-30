package com.chaitanya.mangashelf.models

data class MangaResponse(
    val id: String,
    val image: String?,
    val score: Double?,
    val popularity:Long?,
    val title: String?,
    val publishedChapterDate: Long?,
    val category: String? 
)
