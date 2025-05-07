package com.example.laboration1.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class ConnectionStatus {
    Available, Lost
}

class ConnectivityObserver(context: Context) {

    private val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // androids api to observe real-time network state changes.
    val connectionStatus: Flow<ConnectionStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            //
            override fun onAvailable(network: Network) {
                trySend(ConnectionStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(ConnectionStatus.Lost)
            }
        }

        // build it in order to be able to use
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // ensures that the callback is registered when the flow is collected and
        // unregistered when the collector goes away!!
        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}
