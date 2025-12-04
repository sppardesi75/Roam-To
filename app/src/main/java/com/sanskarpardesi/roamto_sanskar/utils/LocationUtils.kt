package com.sanskarpardesi.roamto_sanskar.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.util.*

object LocationUtils {

    fun getCoordinatesFromName(context: Context, locationName: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(locationName, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                Pair(address.latitude, address.longitude)
            } else null
        } catch (e: Exception) {
            Log.e("Geocoder", "Error: ${e.message}")
            null
        }
    }

    fun getAddressFromCoordinates(context: Context, lat: Double, lng: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0)
            } else null
        } catch (e: Exception) {
            Log.e("Geocoder", "Error: ${e.message}")
            null
        }
    }
}
