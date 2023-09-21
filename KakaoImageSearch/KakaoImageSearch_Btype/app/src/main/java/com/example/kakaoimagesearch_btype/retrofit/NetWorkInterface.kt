package com.example.kakaoimagesearch_btype.retrofit

import com.example.kakaoimagesearch_btype.ImageData
import com.example.kakaoimagesearch_btype.VideoData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface NetWorkInterface {
    @GET("/v2/search/image")
    suspend fun getImage(
        @Header("Authorization") token : String?,
        @QueryMap param: HashMap<String, String>): ImageData

    @GET("/v2/search/vclip")
    suspend fun getVideo(
        @Header("Authorization") token : String?,
        @QueryMap param: HashMap<String, String>): VideoData
}