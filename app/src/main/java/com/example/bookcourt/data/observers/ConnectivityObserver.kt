package com.example.bookcourt.data.observers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityObserver(private val context:Context):ConnectivityObserverI {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    override fun observe(): Flow<ConnectivityObserverI.Status> {
        return callbackFlow {
            val callback = object: ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    launch { send(ConnectivityObserverI.Status.Available) }
                    super.onAvailable(network)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    launch { send(ConnectivityObserverI.Status.Losing) }
                    super.onLosing(network, maxMsToLive)
                }

                override fun onLost(network: Network) {
                    launch { send(ConnectivityObserverI.Status.Lost) }
                    super.onLost(network)
                }

                override fun onUnavailable() {
                    launch { send(ConnectivityObserverI.Status.Unavailable) }
                    super.onUnavailable()
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}