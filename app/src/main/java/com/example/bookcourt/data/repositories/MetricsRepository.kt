package com.example.bookcourt.data.repositories

import com.example.bookcourt.data.BackgroundService

class MetricsRepository(bgService: BackgroundService):MetricsRepositoryInterface {
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

    override suspend fun appTime() {
        TODO("Not yet implemented")
    }
}
