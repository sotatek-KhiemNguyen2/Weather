package com.sotatek.weather.app.forecast

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sotatek.weather.R
import com.sotatek.weather.app.home.ForecastAdapter
import com.sotatek.weather.base.BaseFragment
import com.sotatek.weather.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by khiemnt
 */

@AndroidEntryPoint
class ForecastFragment : BaseFragment<FragmentForecastBinding, ForecastViewModel>(FragmentForecastBinding::inflate) {

    override val viewModel: ForecastViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun initializeViews() {
        val city = ForecastFragmentArgs.fromBundle(requireArguments()).city
        val forecastAdapter = ForecastAdapter()
        binding.apply {
            showTemperature(viewModel.showTempC)

            rcvWeathers.apply {
                adapter = forecastAdapter
                val lm = LinearLayoutManager(requireContext())
                layoutManager = lm
                val dividerDecoration = DividerItemDecoration(
                    context,
                    lm.orientation
                )
                val divider = ContextCompat.getDrawable(requireActivity(), R.drawable.horizontal_divider_with_paddings)
                divider?.let {
                    dividerDecoration.setDrawable(divider)
                    addItemDecoration(dividerDecoration)
                }
            }
            textTempC.setOnClickListener {
                if (viewModel.showTempC) return@setOnClickListener
                viewModel.showTempC = true
                showTemperature(true)
                forecastAdapter.showTempC = true
                forecastAdapter.notifyDataSetChanged()
            }
            textTempF.setOnClickListener {
                if (!viewModel.showTempC) return@setOnClickListener
                viewModel.showTempC = false
                showTemperature(false)
                forecastAdapter.showTempC = false
                forecastAdapter.notifyDataSetChanged()
            }
            imgBack.setOnClickListener { findNavController().popBackStack() }
        }

        viewModel.getForecast(city)
        viewModel.forecastResultDto
            .onEach { forecastResultDto ->
                forecastAdapter.city = city
                forecastAdapter.submitList(viewModel.getWholeSaleForecast(forecastResultDto.weatherResultDtoList))
            }
            .launchIn(lifecycleScope)
    }

    private fun showTemperature(showTempC: Boolean) {
        binding.apply {
            val activeTempC = if (showTempC) R.color.black else R.color.gray
            val activeTempF = if (!showTempC) R.color.black else R.color.gray
            textTempC.setTextColor(ContextCompat.getColor(requireContext(), activeTempC))
            textTempF.setTextColor(ContextCompat.getColor(requireContext(), activeTempF))
        }
    }
}