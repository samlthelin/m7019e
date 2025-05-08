package com.example.laboration1.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class ConnectionStatus {
    Available
}

class ConnectivityObserver(context: Context) {

    // just take the systems connectinmanager. we use it to register network callback to receive events!
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // we need to create the flow!
    val connectionStatus: Flow<ConnectionStatus> = callbackFlow {
        //when internet becomes available, this emits "ConnectionStatus.Available" to our flow.
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // this emits the value in the flow!!
                trySend(ConnectionStatus.Available)
            }
        }

        // we only care about networks that can access the internet
        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // start observing. also unregister because good coding habit!
        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}