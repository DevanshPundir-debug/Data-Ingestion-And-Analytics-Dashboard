package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private double temp;
    private String weatherCondition;
    private LocalDateTime timestamp;

    //  SETTERS
    public void setCity(String city) {
        this.city = city;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    //  GETTERS
    public String getCity() {
        return city;
    }

    public double getTemp() {
        return temp;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}