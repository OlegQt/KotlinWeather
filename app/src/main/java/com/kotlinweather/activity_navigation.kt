package com.kotlinweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kotlinweather.databinding.ActivityNavigationBinding
import com.kotlinweather.presentation.ScreenMode

class ActivityNavigation : AppCompatActivity() {
    lateinit var binding: ActivityNavigationBinding
    private var currentScreenMode: ScreenMode = ScreenMode.FORECAST

    private fun setUiListeners() {
        binding.bottomNavBar.setOnItemSelectedListener {
            changeScreenMode(it.itemId)
        }
    }

    private fun changeScreenMode(itemId: Int): Boolean {
        return when (itemId) {
            R.id.page_search -> {
                // Изменяем представление на поиск городов
                currentScreenMode = ScreenMode.SEARCH
                binding.groupSearch.visibility = View.VISIBLE
                binding.groupForecast.visibility = View.GONE
                true
            }
            R.id.page_forecast -> {
                // Изменяем представление на просмотр прогнозов
                currentScreenMode = ScreenMode.FORECAST
                binding.groupSearch.visibility = View.GONE
                binding.groupForecast.visibility = View.VISIBLE
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setUiListeners()
    }
}