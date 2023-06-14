package com.bangkit.citisnap.api

import android.util.Log
import androidx.datastore.core.DataStore
import com.bangkit.citisnap.preferences.Preferences
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object{
        private lateinit var preferences: Preferences

        fun getClient(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>) : ApiService{
            val gson = GsonBuilder().setLenient().create()

            preferences = Preferences.getInstance(dataStore)

            val token = runBlocking { preferences.getToken().first() }

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api-model-4yjg6o4w3q-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}