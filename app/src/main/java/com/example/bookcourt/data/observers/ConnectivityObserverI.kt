package com.example.bookcourt.data.observers

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserverI {

    fun observe(): Flow<Status>
    enum class Status{
        Available,Unavailable,Losing,Lost
    }
}