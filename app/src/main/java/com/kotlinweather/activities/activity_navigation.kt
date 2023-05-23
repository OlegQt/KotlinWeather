package com.kotlinweather.activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kotlinweather.R
import com.kotlinweather.adapters.AutoCompleteAdapter
import com.kotlinweather.databinding.ActivityNavigationBinding
import com.kotlinweather.databinding.NavigationForecastBinding
import com.kotlinweather.databinding.NavigationOptionsBinding
import com.kotlinweather.databinding.NavigationSearchBinding
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather
import com.kotlinweather.http.OpenWeather
import com.kotlinweather.models.ScreenMode
import com.kotlinweather.sharedprefs.WeatherApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityNavigation : AppCompatActivity() {
    // Переменные для Binding
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var searchBinding: NavigationSearchBinding
    private lateinit var forecastBinding: NavigationForecastBinding
    private lateinit var optionsBinding: NavigationOptionsBinding

    //
    private var currentScreenMode: ScreenMode = ScreenMode.SEARCH
    private lateinit var searchTextWatcher: TextWatcher
    private lateinit var forecastBadge: BadgeDrawable

    // listCityNames содержит названия городов, которые когда-либо были найдены через приложение
    // При вводе названия города в поисковой строке, ищется match из списка всех названий городов
    // и заносится в listAutocompleteCityNames, на основе которого строится autocompleteAdapter
    // и RecyclerView, которые вместе реализуют интерфейс Autocomplete Text Input
    private val listAutocompleteCityNames: MutableMap<CityInfo, CityWeather?> = mutableMapOf()
    private val autocompleteAdapter = AutoCompleteAdapter(listAutocompleteCityNames)

    // Переменная для хранения найденной информации о геолокации по названию города
    private var cityLocationFound = CityInfo("", 0.0, 0.0, "")

    // Полный список городов
    private val listOfCities: MutableMap<CityInfo, CityWeather?> = mutableMapOf()

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

    private fun showAlertDialog(msg: String) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(msg)
            .setPositiveButton("OK") { d, w ->
                //showMsg("OK")
            }
            .setBackground(getDrawable(R.color.orange))
        dialog.show()

    }

    private fun showFoundCity(foundCity: CityInfo) {
        this.cityLocationFound = foundCity

        //binding.layoutSearch.rclAutocompleteCities.visibility = View.GONE
        searchBinding.txtSearchCity.setText("")

        val addCityListener = DialogInterface.OnClickListener { p0, p1 ->
            addCityToFavourite(foundCity)
            extractAutocompletedCities("")
        }
        val message = with(StringBuilder()) {
            append("Country: ${foundCity.country} \n")
            append("\n")
            append("Lat: ${foundCity.lat} \n")
            append("Lon: ${foundCity.lon}")

        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(foundCity.name)
            .setMessage(message)
            .setNegativeButton("decline") { dialog, which -> }
            .setPositiveButton("OK", addCityListener)
            .setIcon(R.drawable.location_on_48)
            .create()

        dialog.show()
    }

    private fun addCityToFavourite(city: CityInfo) {
        val insert = listOfCities.containsKey(city)

        // Проверяем, если такой город уже добавлен,
        // Выводим сообщение
        if (insert) showAlertDialog("City already inserted")
        else {
            forecastBadge.number = listOfCities.size // Увеличиваем badge в меню
            listOfCities[city] = null
            saveCities() // Сохраняем города в SharedPrefs
        }
    }

    private fun showStabNoCityFound() {
        searchBinding.rclAutocompleteCities.visibility = View.VISIBLE
        showAlertDialog("City ${searchBinding.txtSearchCity.text} doesn't found")
    }

    private fun extractAutocompletedCities(charSequence: CharSequence?) {
        // В любом случае скрываем кнопку с найденным городом
        // Так как если пользователь начал изменение поля ввода, надо отобразить подсказку
        if (charSequence.isNullOrEmpty()) {
            // Если поисковый запрос пустой, показываем список всех добавленых городов
            listAutocompleteCityNames.clear()
            listAutocompleteCityNames.putAll(listOfCities)
            autocompleteAdapter.notifyItemRangeChanged(0, listAutocompleteCityNames.size)
            searchBinding.rclAutocompleteCities.visibility = View.VISIBLE
        } else {
            // Если в текстовом поле введены символы
            // Производим поиск совпадений из списка городов
            val listOfMatch = listOfCities.filter {
                it.key.name.contains(charSequence, ignoreCase = true)
            }
            if (listOfMatch.isEmpty()) {
                searchBinding.rclAutocompleteCities.visibility = View.GONE
            } else {
                listAutocompleteCityNames.clear()
                listAutocompleteCityNames.putAll(listOfMatch)
                autocompleteAdapter.notifyItemRangeChanged(0, listAutocompleteCityNames.size)
                searchBinding.rclAutocompleteCities.visibility = View.VISIBLE
            }
        }


    }

    private fun initElements() {
        forecastBadge = binding.bottomNavBar.getOrCreateBadge(R.id.page_search)
        forecastBadge.isVisible = true
        loadCities()
        extractAutocompletedCities("")
        forecastBadge.number=listOfCities.size


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
        with(searchBinding.rclAutocompleteCities) {
            layoutManager = LinearLayoutManager(this@ActivityNavigation)
            adapter = autocompleteAdapter
            itemAnimator = null
            visibility = View.VISIBLE
            // Добавляем обработчик нажатий по элемету RecycleView
            autocompleteAdapter.setOnCardClickListener { city ->
                // Функция выполняется при нажатие на подсказку с назаванием города
                with(searchBinding) {
                    txtSearchCity.requestFocus() // Установили фокус в поле поиска
                    txtSearchCity.setText(city.name) // Загрузили название города из подсказки
                    rclAutocompleteCities.visibility = View.GONE // Скрыли остальные подсказки
                    txtSearchCity.setSelection(city.name.length) // Переместили каретку вконец строки
                    showKeyBoard(isVisible = true) // Отобразили клавиатуру
                }
            }
        }
    }

    private fun setUiListeners() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_forecast -> currentScreenMode = ScreenMode.FORECAST
                R.id.page_search -> currentScreenMode = ScreenMode.SEARCH
                R.id.page_options -> currentScreenMode = ScreenMode.OPTIONS
            }
            changeScreenMode(currentScreenMode)
        }

        // Слушатель для текстового поля поиска городов
        searchBinding.txtSearchCity.addTextChangedListener(this.searchTextWatcher)

        searchBinding.txtSearchCity.setOnEditorActionListener { textView, i, keyEvent ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    showKeyBoard(isVisible = false)
                    findCityLocationByName(textView.text.toString())
                    true
                }
                else -> false
            }
        }

        optionsBinding.btnTestJson.setOnClickListener{testJson()}

    }

    private fun saveCities(){
        if(listOfCities.isNotEmpty()) {
            val jsonCities = Gson().toJson(listOfCities.keys)
            WeatherApp.sharedPreferences.edit().putString(WeatherApp.APP_PREFERENCES_CITIES,jsonCities).apply()
        }
    }

    private fun loadCities() {
        val jsonCities = WeatherApp.sharedPreferences.getString(WeatherApp.APP_PREFERENCES_CITIES,"")
        if(!jsonCities.isNullOrEmpty()) {
            val array = Gson().fromJson(jsonCities, Array<CityInfo>::class.java)
            listOfCities.clear()
            array.forEach {
                listOfCities[it] = null
            }
        }
    }

    private fun testJson(){
        WeatherApp.sharedPreferences.edit().remove(WeatherApp.APP_PREFERENCES_CITIES).apply()
        listOfCities.clear()

    }

    private fun changeScreenMode(mode: ScreenMode): Boolean {
        when (mode) {
            ScreenMode.SEARCH -> {
                // Меняем видимость дочерних layout
                searchBinding.root.visibility = View.VISIBLE
                forecastBinding.root.visibility = View.GONE
                optionsBinding.root.visibility = View.GONE
                return true
            }
            ScreenMode.FORECAST -> {
                // Меняем видимость дочерних layout
                searchBinding.root.visibility = View.GONE
                forecastBinding.root.visibility = View.VISIBLE
                optionsBinding.root.visibility = View.GONE
                return true
            }
            ScreenMode.OPTIONS -> {
                // Меняем видимость дочерних layout
                searchBinding.root.visibility = View.GONE
                forecastBinding.root.visibility = View.GONE
                optionsBinding.root.visibility = View.VISIBLE
                return true
            }
            else -> return false
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

        // Получаем binding для вложенных элементов
        searchBinding = NavigationSearchBinding.bind(binding.layoutSearch.root)
        forecastBinding = NavigationForecastBinding.bind(binding.layoutForecast.root)
        optionsBinding = NavigationOptionsBinding.bind(binding.layoutOptions.root)

        // Основные функции для построения активити
        initElements()
        setUiListeners()
        changeScreenMode(ScreenMode.SEARCH)
    }
}