package com.kotlinweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.snackbar.Snackbar
import com.kotlinweather.databinding.ActivityNavigationBinding
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.OpenWeather
import com.kotlinweather.presentation.ScreenMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityNavigation : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private var currentScreenMode: ScreenMode = ScreenMode.FORECAST
    private lateinit var searchTextWatcher: TextWatcher
    private lateinit var forecastBadge: BadgeDrawable

    private val weatherApi = OpenWeather(null)

    private fun showMsg(textMsg: String) {
        Snackbar.make(
            binding.root,
            textMsg,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("ok") {}
            .show()
    }

    private fun initElements() {
        forecastBadge = binding.bottomNavBar.getOrCreateBadge(R.id.page_forecast)
        forecastBadge.isVisible = true

        // Здесь прописываем поведение при изменении текста в поисковой строке
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

        }

        binding.lblSearchResult.text = "Some txt"

    }

    private fun setUiListeners() {
        binding.bottomNavBar.setOnItemSelectedListener {
            changeScreenMode(it.itemId)
        }

        // Слушатель для текстового поля поиска городов
        binding.txtSearchCity.addTextChangedListener(this.searchTextWatcher)

        binding.txtSearchCity.setOnEditorActionListener { textView, i, keyEvent ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyBoard()
                    true
                }
                else -> false
            }
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

    private fun hideKeyBoard() {
        val view: View? = this.currentFocus
        // on below line checking if view is not null.
        if (view != null) {
            // on below line we are creating a variable
            // for input manager and initializing it.
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // on below line hiding our keyboard.
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun findCity() {
        val call = weatherApi.getCity("Moscow")
        call.enqueue(object : Callback<List<CityInfo>> {
            override fun onResponse(
                call: Call<List<CityInfo>>,
                response: Response<List<CityInfo>>
            ) {
                if (response.code() == 200) {
                    showMsg("200")
                    //if (response.body() != null) listener?.showFoundCities(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<CityInfo>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initElements()
        setUiListeners()
        findCity()

    }
}