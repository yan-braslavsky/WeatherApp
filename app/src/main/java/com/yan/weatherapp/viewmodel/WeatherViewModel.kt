package com.yan.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yan.weatherapp.api.repositories.WeatherRepository
import com.yan.weatherapp.heplers.GeoProvider
import com.yan.weatherapp.models.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Handles the state of the weather view
 */
class WeatherViewModel(
        private val weatherRepository: WeatherRepository,
        private val geoProvider: GeoProvider) : ViewModel(), GeoProvider.Listener {

    private val disposablesBag: CompositeDisposable = CompositeDisposable()
    private val weather: MutableLiveData<Weather> = MutableLiveData()
    private val loadError: MutableLiveData<String> = MutableLiveData()
    val permissionError: MutableLiveData<String> = MutableLiveData()

    fun getWeather(): LiveData<Weather> {
        return weather
    }

    fun getLoadError(): LiveData<String> {
        return loadError
    }

    fun getPermissionError(): LiveData<String> {
        return permissionError
    }

    override fun onCleared() {
        super.onCleared()
        disposablesBag.dispose()
    }

    fun refreshData() {
        geoProvider.updateLocation(this)
    }

    override fun onLocationReady(longitude: Double, latitude: Double) {
        disposablesBag.add(
            weatherRepository.getWeather(longitude, latitude)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            weather.value = result
                        },
                        { error ->
                            loadError.value = error.message
                        }
                    )
        )
    }

}