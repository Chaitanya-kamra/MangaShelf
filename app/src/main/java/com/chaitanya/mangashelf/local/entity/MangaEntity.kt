package com.chaitanya.mangashelf.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mangas")
data class MangaEntity (
    @PrimaryKey val id: String,
    val image: String?,
    val score: Double?,
    val popularity: Long?,
    val title: String?,
    val publishedChapterDate: Long?,
    val category: String?,
    val isFavorite:Boolean = false,
    val isRead: Boolean = false
):Parcelable