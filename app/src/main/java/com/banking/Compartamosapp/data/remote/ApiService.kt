package com.banking.Compartamosapp.data.remote

import com.banking.Compartamosapp.data.model.Account
import com.banking.Compartamosapp.data.model.Transaction
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: Long): Account

    @GET("accounts/{id}/transactions")
    suspend fun getTransactions(@Path("id") id: Long): List<Transaction>

    @POST("transactions")
    suspend fun createTransaction(@Body transaction: Transaction): Transaction

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/api/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}