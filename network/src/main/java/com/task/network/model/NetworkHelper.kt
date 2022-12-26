package com.task.network.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

internal class NetworkHelper constructor(val context: Context){
    fun isOnline(): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}