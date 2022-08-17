package com.mohit.weatherapp.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mohit.weatherapp.R
import com.mohit.weatherapp.databinding.FragmentWeatherInfoBinding
import com.mohit.weathersdk.network.db.Location
import com.mohit.weatherapp.interfaces.IFragmentCallbacks
import com.mohit.weathersdk.network.models.LocationWeatherResponse
import com.mohit.weathersdk.util.Status
import com.mohit.weatherapp.viewmodel.WeatherViewModel
import com.mohit.weathersdk.network.models.LocationForecastResponse


class WeatherInfoFragment : BaseFragment() {

    companion object {
        const val TAG = "WeatherInfoFragment"
    }

    private val weatherViewModel by viewModels<WeatherViewModel> { viewModelProviderFactory }

    lateinit var binding: FragmentWeatherInfoBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentCallbacks) {
            iFragmentCallbacks = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weather_info, container, false
        )
        binding.viewModel = weatherViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iFragmentCallbacks.showBackArrow()
        val location = requireArguments().getParcelable<Location>(LocationsFragment.LOCATION)
        location?.cityName?.let {
            iFragmentCallbacks.updateTitle(it)
            observeLocationWeatherByCityName(it)
        }

        location?.let {
            if (it.lat != null && it.lon != null) {
                observeLocationForecastByLatLng(it.lat!!, it.lon!!)
            }
        }
    }

    /**
     * Observes whether location weather request is successful
     * @param cityName Name of the city.
     */
    private fun observeLocationWeatherByCityName(cityName: String) {
        weatherViewModel
            .getLocationWeatherByCityName(cityName)
            .observe(viewLifecycleOwner, Observer { (status, locationWeatherResponse, errString) ->
                when (status) {
                    Status.LOADING -> {
                        binding.tvWeatherInfo.text = getString(R.string.msg_loading)
                    }
                    Status.SUCCESS -> {
                        showWeatherInfo(locationWeatherResponse)
                    }
                    Status.ERROR -> {
                        binding.tvWeatherInfo.text = getString(R.string.msg_failed_to_get_weather_info)
                    }
                }
            })
    }

    /**
     * Observes whether location forecast request is successful
     * @param lat LATITUDE of the city.
     * @param lon LONGITUDE of the city.
     */
    private fun observeLocationForecastByLatLng(lat: Double, lon: Double) {
        weatherViewModel
            .getLocationForecast(lat, lon)
            .observe(viewLifecycleOwner, Observer { (status, locationForecastResponse, errString) ->
                when (status) {
                    Status.LOADING -> {
                        binding.tvForecastInfo.text = getString(R.string.msg_loading)
                    }
                    Status.SUCCESS -> {
                        showForecastInfo(locationForecastResponse)
                    }
                    Status.ERROR -> {
                        binding.tvForecastInfo.text = getString(R.string.msg_failed_to_get_weather_info)
                    }
                }
            })
    }

    private fun showWeatherInfo(locationWeatherResponse: LocationWeatherResponse?) {
        val weatherInfo = SpannableStringBuilder()
        weatherInfo.append("Weather: ")
        var start = weatherInfo.length
        weatherInfo.append("${locationWeatherResponse?.name} (${locationWeatherResponse?.weather?.get(0)?.description})")
        weatherInfo.setSpan(
            RelativeSizeSpan(1.2f), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.setSpan(
            StyleSpan(Typeface.BOLD), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.append("\nTemperature: ")
        start = weatherInfo.length
        weatherInfo.append("${locationWeatherResponse?.main?.temp} F")
        weatherInfo.setSpan(
            RelativeSizeSpan(1.2f), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.setSpan(
            StyleSpan(Typeface.BOLD), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.append("\nHumidity: ")
        start = weatherInfo.length
        weatherInfo.append("${locationWeatherResponse?.main?.humidity}")
        weatherInfo.setSpan(
            RelativeSizeSpan(1.2f), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.setSpan(
            StyleSpan(Typeface.BOLD), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.append("\nWind: ")
        start = weatherInfo.length
        weatherInfo.append("${locationWeatherResponse?.wind?.speed} mph")
        weatherInfo.setSpan(
            RelativeSizeSpan(1.2f), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        weatherInfo.setSpan(
            StyleSpan(Typeface.BOLD), start, weatherInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvWeatherInfo.text = weatherInfo
    }

    private fun showForecastInfo(locationForecastResponse: LocationForecastResponse?) {
        val forecastInfo = SpannableStringBuilder()
        forecastInfo.append("Forecast:\n")
        var start = forecastInfo.length
        locationForecastResponse?.list?.forEach {
            forecastInfo.append("\n${it.dtTxt} [T: ${it.main.temp} F]")
            start = forecastInfo.length
            forecastInfo.append(
                "\n[${it.weather[0].description}]" + "\n" + "Humidity: ${it.main.humidity}, Wind: ${it.wind.speed} mph"
            )
            forecastInfo.setSpan(
                RelativeSizeSpan(1.2f), start, forecastInfo.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            forecastInfo.setSpan(
                StyleSpan(Typeface.BOLD), start, forecastInfo.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            forecastInfo.append("\n")
        }
        binding.tvForecastInfo.text = forecastInfo
        binding.tvForecastInfo.movementMethod = ScrollingMovementMethod()
    }

}