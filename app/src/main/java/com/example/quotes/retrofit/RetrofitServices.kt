package com.example.quotes.retrofit

import com.example.quotes.apimodels.ItemsResponse
import com.example.quotes.apimodels.Quotes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitServices {

    @GET
    fun quotesItems(@Url url:String,
                    @Query("page") page : Int) : Call<ItemsResponse>

    @GET
    suspend fun pagerItems(@Url url:String,
                           @Query("page") page : Int) : Response<ItemsResponse>

}