package com.weather.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
	Optional<Weather> findByCity(String city);
}