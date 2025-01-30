package com.chaitanya.mangashelf.api

import com.chaitanya.mangashelf.models.MangaResponse
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("b/KEJO")
    suspend fun getMangas():List<MangaResponse>



}
