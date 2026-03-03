package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.WeatherData;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByCity(String city);
}