package com.example.bookcourt.models.metrics

import kotlinx.serialization.Serializable

@Serializable
data class DataUserInfoMetric(
    val name:String,
    val surname:String,
    val phonenumber:String,
    val city:String,
    val deviceId:String,
    val devicemodel:String,
    val os:String,
    val osversion:String
)

