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

    // Переменная для хранения найденной информации о геолокации по названию города
    private var cityLocationFound = CityInfo("", 0.0, 0.0, "")

    // Полный список городов, добавленных на экран прогнозов
    private val listOfCities: MutableSet<CityInfo> = mutableSetOf()

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

    private fun showFoundCity(foundCity: CityInfo) {
        this.cityLocationFound = foundCity
        binding.rclAutocompleteCities.visibility = View.GONE
        binding.btnAddNewCity.visibility = View.VISIBLE
        binding.btnAddNewCity.text = cityLocationFound.name
    }

    private fun showStabNoCityFound() {
        binding.rclAutocompleteCities.visibility = View.VISIBLE
        binding.btnAddNewCity.visibility = View.GONE
        showMsg("${binding.txtSearchCity.text} doesn't exist")
    }

    private fun extractAutocompletedCities(charSequence: CharSequence?) {
        // В любом случае скрываем кнопку с найденным городом
        // Так как если пользователь начал изменение поля ввода, надо отобразить подсказку
        binding.btnAddNewCity.visibility = View.GONE
        if (charSequence.isNullOrEmpty()) {
            // Если поисковый запрос пустой, показываем список всех добавленых городов
            listAutocompleteCityNames.clear()
            listAutocompleteCityNames.addAll(listCityNames)
            autocompleteAdapter.notifyItemRangeChanged(0, listAutocompleteCityNames.size)
            binding.rclAutocompleteCities.visibility = View.VISIBLE
        } else {
            // Если в текстовом поле введены символы
            // Производим поиск совпадений из списка городов
            val listOfMatch = listCityNames.filterIndexed { index, s ->
                s.contains(charSequence, ignoreCase = true)
            }
            if (listOfMatch.isEmpty()) {
                binding.rclAutocompleteCities.visibility = View.GONE
            } else {
                listAutocompleteCityNames.clear()
                listAutocompleteCityNames.addAll(listOfMatch)
                autocompleteAdapter.notifyItemRangeChanged(0, listAutocompleteCityNames.size)
                binding.rclAutocompleteCities.visibility = View.VISIBLE
            }
        }


    }

    private fun initElements() {
        forecastBadge = binding.bottomNavBar.getOrCreateBadge(R.id.page_forecast)
        forecastBadge.isVisible = true

        listCityNames.addAll(
            setOf(
                "Moscow",
                "London",
                "Berlin",
                "Paris",
                "Petersburg",
                "Minsk",
                "Dubai"
            )
        )
        listAutocompleteCityNames.addAll(listCityNames)

        binding.btnAddNewCity.visibility = View.GONE

        // Здесь прописываем поведение при изменении текста в поисковой строке
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                //TODO("Not yet implemented")
                extractAutocompletedCities(charSequence)
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
                // Функция выполняется при нажатие на подсказку с назаванием города
                with(binding) {
                    txtSearchCity.requestFocus() // Установили фокус в поле поиска
                    txtSearchCity.setText(str) // Загрузили название города из подсказки
                    rclAutocompleteCities.visibility = View.GONE // Скрыли остальные подсказки
                    txtSearchCity.setSelection(str.length) // Переместили каретку вконец строки
                    showKeyBoard(isVisible = true) // Отобразили клавиатуру
                    rclAutocompleteCities.visibility = View.VISIBLE
                }
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
                    showKeyBoard(isVisible = false)
                    findCityLocationByName(textView.text.toString())
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

    private fun showKeyBoard(isVisible: Boolean) {
        val view: View? = this.currentFocus
        // on below line checking if view is not null.
        if (view != null) {
            // on below line we are creating a variable
            // for input manager and initializing it.
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // on below line hiding our keyboard.
            if (isVisible) inputMethodManager.showSoftInput(view, 0)
            else inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun findCityLocationByName(cityName: String) {
        val call = weatherApi.getCity(cityName)
        call.enqueue(object : Callback<List<CityInfo>> {
            override fun onResponse(
                call: Call<List<CityInfo>>,
                response: Response<List<CityInfo>>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val lst = response.body()
                        if (lst != null) {
                            if (lst.isNotEmpty()) {
                                showFoundCity(lst.first())
                            } else showStabNoCityFound()
                        }
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

        initElements()
        setUiListeners()

    }
}