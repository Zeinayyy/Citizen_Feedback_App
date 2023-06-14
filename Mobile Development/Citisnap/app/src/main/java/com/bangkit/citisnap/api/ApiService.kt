package com.bangkit.citisnap.api

import com.bangkit.citisnap.model.PostModel
import com.bangkit.citisnap.response.Data
import com.bangkit.citisnap.response.PredictResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("predict")
    fun predict(
        @Body postModel: PostModel
    ): Call<PredictResponse>


}