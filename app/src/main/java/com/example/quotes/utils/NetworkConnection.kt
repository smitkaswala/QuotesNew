package com.example.quotes.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData
import com.airbnb.lottie.animation.content.Content

class NetworkConnection(private val context : Context) : LiveData<Boolean>() {

    private var connectivityManger : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkCallBack : ConnectivityManager.NetworkCallback

    @SuppressLint("ObsoleteSdkInt")
    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {

                connectivityManger.registerDefaultNetworkCallback(connectivityManagerCallBack())

            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {

                lollipopNetworkRequest()

            }else -> {

                context.registerReceiver(networkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }

        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onInactive() {
        super.onInactive()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)  {

            connectivityManger.unregisterNetworkCallback(connectivityManagerCallBack())

        }else{

            context.unregisterReceiver(networkReceiver)
        }

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkRequest(){
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectivityManger.registerNetworkCallback(
            requestBuilder.build(),
            connectivityManagerCallBack()
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun connectivityManagerCallBack() : ConnectivityManager.NetworkCallback{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            networkCallBack = object : ConnectivityManager.NetworkCallback(){

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)

                }
            }
            return networkCallBack
        }else{
            throw IllegalAccessError("Error")
        }
    }

    private val networkReceiver = object : BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {
            updateConnection()
        }

    }
    private fun updateConnection(){
        val actionNetwork : NetworkInfo? = connectivityManger.activeNetworkInfo
        postValue(actionNetwork?.isConnected == true)
    }

}