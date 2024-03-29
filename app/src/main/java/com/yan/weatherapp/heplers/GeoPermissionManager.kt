package com.yan.weatherapp.heplers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Handles the permission request boilerplate
 */
class GeoPermissionManager {

    private val REQUEST_CODE: Int = 87

    fun isPermissionGranted(activity: Activity): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun requestGeoPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    fun didUserGrantedPermission(requestCode: Int, grantResults: IntArray): Boolean {
        return when (requestCode) {
            REQUEST_CODE -> !(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            else -> false
        }
    }
}