package com.example.bookcourt.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import com.example.bookcourt.data.BackgroundService
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.models.AppSessionLength
import com.example.bookcourt.models.Metric
import com.example.bookcourt.models.UserDataMetric
import com.example.bookcourt.utils.Hashing
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class MetricsRepository @Inject constructor(
    private val bgService: BackgroundService,
    private val dataStoreRepository: DataStoreRepository,
    private val hashing:Hashing
    ):MetricsRepositoryInterface {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendUserData(name:String, surname:String, phoneNumber:String,uuid: String){
        // В Других метрика UUID брать надо из DataStore
        var json = Json.encodeToString(serializer = UserDataMetric.serializer(),
            UserDataMetric(name,surname,phoneNumber)
        )
        var metric = Metric(type = USER_DATA_TYPE, data = json, date = LocalDate.now().toString(), GUID = "TEST!!!", UUID = uuid )
        bgService.addToStack(metric)
    }

    override suspend fun onClick(objectName:String){

    }

    override suspend fun onSwipe(direсtion:String) {
        TODO("Not yet implemented")
    }

    override suspend fun onStartScreen() {
        TODO("Not yet implemented")
    }

    override suspend fun onStopScreen() {
        TODO("Not yet implemented")
    }

    override suspend fun onStartApp() {
        TODO("Not yet implemented")
    }

    override suspend fun onStopApp() {
        TODO("Not yet implemented")
    }

    override suspend fun screenTime() {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun appTime(sessionTime: Int) {
        var json = Json.encodeToString(serializer = AppSessionLength.serializer(),
            AppSessionLength(sessionTime)
        )
        coroutineScope {
            val UUID = dataStoreRepository.getPref(uuid).collect().toString()
            var metric = Metric(type = SESSION_LENGTH_TYPE, data = json, date = LocalDate.now().toString(), GUID = "TEST!!!", UUID = UUID)
            bgService.addToStack(metric)
        }
    }

}

const val USER_DATA_TYPE ="userData"
const val SESSION_LENGTH_TYPE ="sessionLength"
