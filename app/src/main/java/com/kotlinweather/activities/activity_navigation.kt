package com.kotlinweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.snackbar.Snackbar
import com.kotlinweather.R
import com.kotlinweather.adapters.AutoCompleteAdapter
import com.kotlinweather.databinding.ActivityNavigationBinding
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.OpenWeather
import com.kotlinweather.models.ScreenMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityNavigation : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private var currentScreenMode: ScreenMode = ScreenMode.FORECAST
    private lateinit var searchTextWatcher: TextWatcher
    private lateinit var forecastBadge: BadgeDrawable

    // listCityNames содержит названия городов, которые когда-либо были найдены через приложение
    // При вводе названия города в поисковой строке, ищется match из списка всех названий городов
    // и заносится в listAutocompleteCityNames, на основе которого строится autocompleteAdapter
    // и RecyclerView, которые вместе реализуют интерфейс Autocomplete Text Input
    private val listCityNames = mutableSetOf<String>()
    private val listAutocompleteCityNames = mutableListOf<String>()
    private val autocompleteAdapter = AutoCompleteAdapter(listAutocompleteCityNames)

    // Полный список городов, добавленных на экран прогнозов
    private val listOfCities:MutableSet<CityInfo> = mutableSetOf()

    // Элемент класса доступа к погодному интерфейсу
    private val weatherApi = OpenWeather()

    private fun showMsg(textMsg: String) {
        Snackbar.make(
            binding.root,
            textMsg,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("ok") {}
            .setTextMaxLines(20)
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

            override fun onTextChanged(charS: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
                binding.rclAutocompleteCities.visibility=View.VISIBLE
                if (!charS.isNullOrEmpty()) {
                    listAutocompleteCityNames.clear()
                    val str = listCityNames.filterIndexed { index, s ->
                        s.contains(charS,ignoreCase = true)
                    }
                    if(str.isEmpty()) binding.rclAutocompleteCities.visibility = View.GONE
                    listAutocompleteCityNames.addAll(str)
                    autocompleteAdapter.notifyItemRangeChanged(0,listAutocompleteCityNames.size)
                    binding.txtSearchCity.setSelection(charS.length)
                }
                else {
                    listAutocompleteCityNames.clear()
                    listAutocompleteCityNames.addAll(listCityNames)
                    autocompleteAdapter.notifyItemRangeChanged(0,listAutocompleteCityNames.size)
                }

            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

        }

        // Устанавливаем и настраиваем поведение RecyclerView
        // Для отображения подсказки при наборе городов в поисковой строке
        with(binding.rclAutocompleteCities) {
            layoutManager = LinearLayoutManager(this@ActivityNavigation)
            adapter = autocompleteAdapter
            itemAnimator = null
            // Добавляем обработчик нажатий по элемету RecycleView
            autocompleteAdapter.setOnCardClickListener { str ->
                // Устанавливаем текс, скрываем клавиатуру и список
                binding.txtSearchCity.setText(str)
                binding.rclAutocompleteCities.visibility = View.GONE
                //hideKeyBoard()
            }
        }


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

    private fun findCityByName() {
        val call = weatherApi.getCity("Moscow")
        call.enqueue(object : Callback<List<CityInfo>> {
            override fun onResponse(
                call: Call<List<CityInfo>>,
                response: Response<List<CityInfo>>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val city = response.body()?.first()
                        showMsg(city?.name.toString())
                    }
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

        this.listCityNames.addAll(setOf("Moscow", "Berlin", "Paris", "London", "Madrid"))

        initElements()
        setUiListeners()
        findCityByName()
    }
}