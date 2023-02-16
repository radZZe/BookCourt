package com.example.bookcourt.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bookcourt.data.BackgroundService
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.models.*
import com.example.bookcourt.utils.Hashing
import com.example.bookcourt.utils.MetricType
import com.example.bookcourt.utils.MetricType.SKIP_BOOK
import com.example.bookcourt.utils.MetricType.DISLIKE_BOOK
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import java.time.LocalDate
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
        uuid: String
    ) {
        // В Других метрика UUID брать надо из DataStore
        var GUID = UUID.randomUUID().toString()
        var json = Json.encodeToString(
            serializer = UserDataMetric.serializer(),
            UserDataMetric(name, surname, phoneNumber)
        )
        var metric = Metric(
            type = USER_DATA_TYPE,
            data = json,
            date = LocalDate.now().toString(),
            GUID = GUID,
            UUID = uuid
        )
        bgService.sendMetric(metric)
    }

    override suspend fun onClick(objectName: String) {

    }

    override suspend fun onSwipe(book: Book, direction: String) {
        var GUID = UUID.randomUUID().toString()
        var date = LocalDate.now().toString()
        var bookMetric =  book.toBookMetric()
        dataStoreRepository.getPref(uuid).collect {
            var uuid = it
            var json = Json.encodeToString(
                serializer = BookMetric.serializer(),
                bookMetric
            )
            // Сделать более лаконично
            when (direction) {
                MetricType.WANT_TO_READ_BOOK -> {
                    var metric = Metric(
                        type = MetricType.WANT_TO_READ_BOOK,
                        data = json,
                        date = date,
                        GUID = GUID,
                        UUID = uuid
                    )
                    bgService.sendMetric(metric)
                }
                DISLIKE_BOOK -> {
                    var metric = Metric(
                        type = DISLIKE_BOOK,
                        data = json,
                        date = date,
                        GUID = GUID,
                        UUID = uuid
                    )
                    bgService.sendMetric(metric)
                }
                MetricType.LIKE_BOOK -> {
                    var metric = Metric(
                        type = MetricType.LIKE_BOOK,
                        data = json,
                        date = date,
                        GUID = GUID,
                        UUID = uuid
                    )
                    bgService.sendMetric(metric)
                }
                SKIP_BOOK -> {
                    var metric = Metric(
                        type = SKIP_BOOK,
                        data = json,
                        date = date,
                        GUID = GUID,
                        UUID = uuid
                    )
                    bgService.sendMetric(metric)
                }
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onAction(action: UserAction) {
        dataStoreRepository.getPref(uuid).collect {
            var uuid = it
            var json = Json.encodeToString(
                serializer = UserAction.serializer(),
                action
            )
            var metric = Metric(
                type = USER_DATA_TYPE,
                data = json,
                date = LocalDate.now().toString(),
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun appTime(sessionTime: Int) {
        var json = Json.encodeToString(
            serializer = AppSessionLength.serializer(),
            AppSessionLength(sessionTime)
        )
        coroutineScope {
            val UUID = dataStoreRepository.getPref(uuid).collect().toString()
            var metric = Metric(
                type = SESSION_LENGTH_TYPE,
                data = json,
                date = LocalDate.now().toString(),
                GUID = "TEST!!!",
                UUID = UUID
            )
            bgService.sendMetric(metric)
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

    override suspend fun getOS(): String = "android version: " + Build.VERSION.SDK_INT.toString()
    override suspend fun detectShare() {
        TODO("Not yet implemented")
    }
}

const val USER_DATA_TYPE = "userData"
const val SESSION_LENGTH_TYPE = "sessionLength"
