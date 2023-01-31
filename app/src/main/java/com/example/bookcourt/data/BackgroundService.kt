package com.example.bookcourt.data

import android.util.Log
import com.example.bookcourt.models.Metric
import com.example.bookcourt.models.UserDataMetric
import com.example.bookcourt.models.UserRemote
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class BackgroundService {

    var stack = listOf<Metric>()

    fun deleteFromStack(){
        TODO()
    }

    fun addToStack(metric: Metric){
        var body = Json.encodeToString(serializer = Metric.serializer(),
            metric
        )
        Log.d("MetricUserData", body)
    }

    fun job(){
        TODO()
    }
}