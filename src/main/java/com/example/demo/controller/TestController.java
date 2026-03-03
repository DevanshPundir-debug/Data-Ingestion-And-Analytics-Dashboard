//package controller;
package com.example.demo.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.WeatherService;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import com.example.demo.entity.WeatherData;

@RestController
public class TestController {

    private final WeatherService weatherService;

    public TestController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/test")
    public String test() {
        return "project working";
    }

    @GetMapping("/check-key")
    public String checkKey() {
        return weatherService.testKey();

    }
//    @GetMapping("/weather")
//    public String getWeather() {
//        return weatherService.getWeather();
//    }
    @GetMapping("/weather")
    public Map<String, Object> weather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }

    @GetMapping("/weather/history")
    public List<WeatherData> getWeatherHistory(
            @RequestParam(required = false) String city) {

        if (city != null) {
            return weatherService.getWeatherByCity(city);
        } else {
            return weatherService.getAllWeather();
        }
    }
}