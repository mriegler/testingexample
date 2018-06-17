package org.example.beans

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response from API:
 * {"coord":{"lon":139,"lat":35},
"sys":{"country":"JP","sunrise":1369769524,"sunset":1369821049},
"weather":[{"id":804,"main":"clouds","description":"overcast clouds","icon":"04n"}],
"main":{"temp":289.5,"humidity":89,"pressure":1013,"temp_min":287.04,"temp_max":292.04},
"wind":{"speed":7.31,"deg":187.002},
"rain":{"3h":0},
"clouds":{"all":92},
"dt":1369824698,
"id":1851632,
"name":"Shuzenji",
"cod":200}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherInformation(
    val name: String,
    val id: Int,
    val weather: List<WeatherInfo>,
    val main: MainWeatherInfo,
    val rain: RainVolume?
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MainWeatherInfo(
    val temp: Float,
    val humidity: Int,
    val pressure: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RainVolume(
    @JsonProperty("3h")
    val threeHours: Int
)