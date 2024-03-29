package com.yan.weatherapp.usecases

import com.yan.weatherapp.WeatherActivity
import com.yan.weatherapp.heplers.GeoPermissionManager
import com.yan.weatherapp.viewmodel.WeatherViewModel

/**
 * Handles the flow of geo location permissions request and location fetch
 */
class PermissionRequestUseCase(
        private val
        weatherActivity: WeatherActivity,
        private val viewModel: WeatherViewModel,
        private val geoPermissionManager: GeoPermissionManager = GeoPermissionManager()) {

    fun execute() {
        handleGeolocationPermission()
    }

    private fun handleGeolocationPermission() {
        if (!geoPermissionManager.isPermissionGranted(weatherActivity)) {
            geoPermissionManager.requestGeoPermission(weatherActivity)
        } else {
            viewModel.refreshData()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (geoPermissionManager.didUserGrantedPermission(requestCode, grantResults)) {
            viewModel.refreshData()
        } else {
            viewModel.permissionError.value = "Permission has been denied by user"
        }

    }
}