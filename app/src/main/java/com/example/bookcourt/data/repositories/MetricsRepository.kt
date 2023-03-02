package com.example.bookcourt.data.repositories

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.workers.SendMetricsWorker
import com.example.bookcourt.models.*
import com.example.bookcourt.utils.AppVersion.appVersion
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
    private val dataStoreRepository: DataStoreRepository,
) : MetricsRepositoryInterface {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendUserData(
        name: String,
        surname: String,
        phoneNumber: String,
        city: String,
        uuid: String,
        context: Context
    ) {

        val deviceId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val os = "android"
        val osVersion = "version: " + Build.VERSION.RELEASE
        val deviceModel = getDeviceModel()
        var GUID = UUID.randomUUID().toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        var metric = UserDataMetricRemote(
            Type = USER_DATA_TYPE,
            Data = UserDataMetric(
                name,
                surname,
                phoneNumber,
                city,
                deviceId,
                deviceModel,
                os,
                osVersion
            ),
            Date = date,
            GUID = GUID,
            UUID = uuid
        )
        var json = Json.encodeToString(
            serializer = UserDataMetricRemote.serializer(),
            metric
        )
        sendMetric(json)
    }


    override suspend fun onClick(clickMetric: ClickMetric) {
        val GUID = UUID.randomUUID().toString()
        val uid = dataStoreRepository.getPref(uuid).first()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        val metric = ClickMetricRemote(
            Type = MetricType.CLICK,
            Data = clickMetric,
            Date = date, //DateFormat.format("dd-MM-yyyy HH:mm:ss",LocalDate.now()),
            GUID = GUID,
            UUID = uid
        )
        val json = Json.encodeToString(serializer = ClickMetricRemote.serializer(), metric)
        sendMetric(json)
    }

    override suspend fun onSwipe(book: Book, direction: String) {
        var GUID = UUID.randomUUID().toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)
        var bookMetric = book.toBookMetric(direction)
        dataStoreRepository.getPref(uuid).collect {
            var uuid = it
            var metric = BookMetricRemote(
                Type = MetricType.SWIPE,
                Data = bookMetric,
                Date = date,
                GUID = GUID,
                UUID = uuid
            )
            var json = Json.encodeToString(
                serializer = BookMetricRemote.serializer(),
                metric
            )
            sendMetric(json)
        }
    }


    override suspend fun appTime(sessionTime: Int, type: String, screen: String) {
        val GUID = UUID.randomUUID().toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_METRIC_FORMAT)
        var date = LocalDateTime.now().format(formatter)

        coroutineScope {
            val uid = dataStoreRepository.getPref(uuid).first()
            val metric = SessionTimeMetricRemote(
                Type = type,
                Data = SessionTime(
                    (sessionTime / 1000).toString(),
                    screen,
                    appversion = appVersion
                ),
                Date = date,
                GUID = GUID,
                UUID = uid
            )
            var json = Json.encodeToString(
                serializer = SessionTimeMetricRemote.serializer(),
                metric
            )
            sendMetric(json)
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

    private fun sendMetric(json: String) {
        val data = Data.Builder()
            .putString("metricJson", json)
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = OneTimeWorkRequestBuilder<SendMetricsWorker>().setConstraints(constraints)
            .setInputData(data).build()
        WorkManager.getInstance().enqueue(workRequest)
    }


}

const val USER_DATA_TYPE = "userData"
const val SESSION_LENGTH_TYPE = "sessionLength"
