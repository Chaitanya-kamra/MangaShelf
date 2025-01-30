package com.chaitanya.mangashelf.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chaitanya.mangashelf.local.entity.MangaEntity

@Database(entities = [MangaEntity::class],version = 1)
abstract class MangaShelfDatabase:RoomDatabase() {
    abstract fun mangaDao(): MangaDao
}