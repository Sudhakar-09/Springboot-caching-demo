package com.weather.service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.weather.entity.Weather;
import com.weather.repository.WeatherRepository;

import jakarta.transaction.Transactional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    // Cache all weather data
    @Cacheable(value = "weatherCacheAll")
    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    // Cache weather data by city
    @Cacheable(value = "weatherCache", key = "#city")
    public Optional<Weather> getWeatherByCity(String city) {
        return weatherRepository.findByCity(city);
    }

    // Add new weather data
    @Transactional
    @CachePut(value = "weatherCache", key = "#weather.city")
    public Weather addWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    // Update existing weather data
    @Transactional
    @CachePut(value = "weatherCache", key = "#newWeather.city")
    public Weather updateWeather(String city, Weather newWeather) {
        return weatherRepository.findByCity(city)
                .map(existingWeather -> {
                    existingWeather.setTemperature(newWeather.getTemperature());
                    existingWeather.setHumidity(newWeather.getHumidity());
                    existingWeather.setCondition(newWeather.getCondition());
                    return weatherRepository.save(existingWeather);
                })
                .orElseThrow(() -> new NoSuchElementException("City '" + city + "' not found"));
    }

    // Delete weather data by city
    @Transactional
    @CacheEvict(value = "weatherCache", key = "#city")
    public void deleteWeather(String city) {
        weatherRepository.findByCity(city).ifPresent(weatherRepository::delete);
    }

    // Clear all cached weather data
    @CacheEvict(value = "weatherCacheAll", allEntries = true)
    public void evictAllWeatherCache() {
        // Clears entire cache
    }
}
