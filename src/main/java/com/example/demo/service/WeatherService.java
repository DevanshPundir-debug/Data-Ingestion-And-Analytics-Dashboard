package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;

import com.example.demo.repository.WeatherRepository;
import com.example.demo.entity.WeatherData;
import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final WeatherRepository weatherRepository;
    private final InfluxService influxService;

    public WeatherService(WeatherRepository weatherRepository, InfluxService influxService) {
        this.weatherRepository = weatherRepository;
        this.influxService = influxService;
    }

    public String testKey() {
        return apiKey;
    }

    public List<WeatherData> getAllWeather() {
        return weatherRepository.findAll();
    }

    public List<WeatherData> getWeatherByCity(String city) {
        return weatherRepository.findByCity(city);
    }

    public WeatherResponse getLatestWeather(String city) {

        WeatherData data =
                weatherRepository.findTopByCityOrderByTimestampDesc(city);

        if (data == null) {
            return new WeatherResponse(
                    "No Data Found",
                    0,
                    "No Record",
                    LocalDateTime.now()
            );
        }

        return new WeatherResponse(
                data.getCity(),
                data.getTemp(),
                data.getWeatherCondition(),
                data.getTimestamp()
        );
    }

    public Map<String, Object> getWeather(String city) {

        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city +
                    "&appid=" + apiKey +
                    "&units=metric";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.get("cod").asInt() != 200) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "City not found");
                return error;
            }

            String cityName = root.get("name").asText();
            double temp = root.get("main").get("temp").asDouble();
            String condition = root.get("weather").get(0).get("main").asText();

            //  Save in MySQL
            WeatherData weatherData = new WeatherData();
            weatherData.setCity(cityName);
            weatherData.setTemp(temp);
            weatherData.setWeatherCondition(condition);
            weatherData.setTimestamp(LocalDateTime.now());

            weatherRepository.save(weatherData);

            //  Save in InfluxDB
            influxService.saveWeather(cityName, temp);

            Map<String, Object> data = new HashMap<>();
            data.put("city", cityName);
            data.put("temp", temp);
            data.put("condition", condition);

            return data;

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }
}