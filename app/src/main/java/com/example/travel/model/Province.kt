package com.example.travel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ProvinceResult(
    val error:Int = 0,
    val error_text:String = "",
    val data_name:String = "",
    val data:List<Province> = emptyList()
)

data class Province(
    val id:String,
    val name:String,
    val name_en:String,
    val full_name:String,
    val full_name_en:String,
    val latitude:Double,
    val longitude:Double
)