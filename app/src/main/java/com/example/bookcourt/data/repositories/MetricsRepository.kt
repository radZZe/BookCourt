package com.example.bookcourt.data.repositories

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.bookcourt.data.BackgroundService
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.models.*
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Hashing
import com.example.bookcourt.utils.MetricType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class MetricsRepository @Inject constructor(
    private val bgService: BackgroundService,
    private val dataStoreRepository: DataStoreRepository,
    private val hashing: Hashing
) : MetricsRepositoryInterface {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendUserData(
        name: String,
        surname: String,
        phoneNumber: String,
        city:String,
        uuid: String,
        context:Context
    ) {
        // В Других метрика UUID брать надо из DataStore

        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val os  = "android"
        val osVersion =  "version: "+Build.VERSION.RELEASE
        val deviceModel = getDeviceModel()
        var GUID = UUID.randomUUID().toString()
        var json = Json.encodeToString(
            serializer = UserDataMetric.serializer(),
            UserDataMetric(name, surname, phoneNumber,city,deviceId,deviceModel,os,osVersion)
        )
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        var metric = Metric(
            Type = USER_DATA_TYPE,
            Data = json,
            Date = date,
            GUID = GUID,
            UUID = uuid
        )
        bgService.sendMetric(metric)
    }


    override suspend fun onClick(clickMetric: ClickMetric){
        val GUID = UUID.randomUUID().toString()
        val uid = dataStoreRepository.getPref(uuid).first()
        val json = Json.encodeToString(serializer = ClickMetric.serializer(), clickMetric)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        val metric = Metric(
            Type = MetricType.CLICK,
            Data = json,
            Date = date, //DateFormat.format("dd-MM-yyyy HH:mm:ss",LocalDate.now()),
            GUID = GUID,
            UUID = uid
        )
        bgService.sendMetric(metric)
        Log.d("Screen", metric.Data.toString())
    }

    override suspend fun onSwipe(book: Book, direction: String) {
        var GUID = UUID.randomUUID().toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        var bookMetric =  book.toBookMetric(direction)
        dataStoreRepository.getPref(uuid).collect {
            var uuid = it
            var json = Json.encodeToString(
                serializer = BookMetric.serializer(),
                bookMetric
            )
            var metric = Metric(
                Type = MetricType.SWIPE,
                Data = json,
                Date = date,
                GUID = GUID,
                UUID = uuid
            )
            bgService.sendMetric(metric)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onAction(action: UserAction) {
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        dataStoreRepository.getPref(uuid).collect {
            var uuid = it
            var json = Json.encodeToString(
                serializer = UserAction.serializer(),
                action
            )
            var metric = Metric(
                Type = USER_DATA_TYPE,
                Data = json,
                Date = date,
                GUID = "TEST!!!",
                UUID = uuid
            )
            bgService.sendMetric(metric)
        }

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

override suspend fun appTime(sessionTime: Int, type: String) {
        val GUID = UUID.randomUUID().toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        var json = Json.encodeToString(serializer = SessionTime.serializer(),
            SessionTime(sessionTime)
        )
        coroutineScope {
            val uid = dataStoreRepository.getPref(uuid).first()
            val metric = Metric(
                Type = type,
                Data = json,
                Date = date,
                GUID = GUID,
                UUID = uid
            )
            bgService.sendMetric(metric)
            Log.d("Screen", metric.Data.toString())
        }
    }


    override suspend fun getDeviceModel(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.lowercase(Locale.getDefault())
                .startsWith(manufacturer.lowercase(Locale.getDefault()))
        ) {
            model
        } else {
            "$manufacturer $model"
        }
    }


    //override suspend fun getOS(): String = "android version: " + Build.VERSION.SDK_INT.toString()
    override suspend fun detectShare() {
        TODO("Not yet implemented")
    }


}

const val USER_DATA_TYPE = "userData"
const val SESSION_LENGTH_TYPE = "sessionLength"
