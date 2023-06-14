package com.bangkit.citisnap.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data,

)

data class Data(

	@field:SerializedName("classification")
	val classification: String
)
