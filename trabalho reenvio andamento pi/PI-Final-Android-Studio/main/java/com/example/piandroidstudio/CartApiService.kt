package com.example.echoviagens


import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface CartApiService {
    @GET("/getCartItems")
    fun getCartItems(@Query("userId") userId: Int): Call<List<Produtos>>

    @DELETE("/deleteCartItem")
    fun deleteCartItem(@Query("produtoId") produtoId: Int, @Query("userId") userId: Int): Call<Void>
}
