# Weather App with Spring Boot Caching

## Introduction
This project demonstrates the implementation of caching mechanisms in a Spring Boot application using both in-memory caching (ConcurrentHashMap) and Redis. The application provides a RESTful API to manage weather data and utilizes caching to optimize performance and reduce database queries.

## What is Caching?
Caching is a mechanism that stores frequently accessed data in memory to improve performance and reduce latency. Instead of fetching data from the database every time, caching allows retrieval from a faster storage layer.

### Advantages of Caching:
- Reduces database load and improves response time.
- Enhances application scalability.
- Minimizes network latency.

### Disadvantages of In-Memory Caching:
- Limited by RAM size, leading to potential memory exhaustion.
- Data loss on application restart (unless persisted in an external storage).
- Not suitable for distributed applications without additional configurations.

## Types of Caching Used in This Project
1. **In-Memory Caching**: Uses Java's `ConcurrentHashMap` (via `@Cacheable`) to store frequently accessed data within the application's memory.
2. **Redis Caching**: Stores cache data in Redis, a high-performance, distributed in-memory datastore. It provides persistence, eviction policies, and better scalability.

## Technologies Used
- **Spring Boot**
- **Spring Cache Abstraction**
- **Redis**
- **Spring Data Redis**
- **Swagger (OpenAPI)**

## Project Structure
```
├── src/main/java/com/weather
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic layer
│   ├── entity/           # Data model
│   ├── repository/       # Data access layer
│   ├── config/           # Configuration files
│   ├── WeatherApplication.java   # Main application entry point
```

## API Endpoints

### Weather Controller
- `GET /weather/{city}` - Fetch weather data for a city.
- `PUT /weather/{city}` - Update weather data for a city.
- `DELETE /weather/{city}` - Remove weather data for a city.
- `GET /weather` - Get all weather data.
- `POST /weather` - Add new weather data.

### Cache Controller
- `GET /cache/names` - Get all cache names.
- `GET /cache/contents/{cacheName}` - Fetch all cache contents for a specific cache.
- `GET /cache/contents/{cacheName}/{key}` - Fetch a specific cache entry.
- `DELETE /cache/evict/{cacheName}` - Clear a cache by name.

## Example Data
```json
[
    {"city": "Bangalore", "temperature": 28.5, "humidity": 65, "condition": "Cloudy"},
    {"city": "Mysore", "temperature": 30.2, "humidity": 60, "condition": "Sunny"},
    {"city": "Hubli", "temperature": 32.8, "humidity": 55, "condition": "Partly Cloudy"}
]
```

## Caching Annotations in Spring Boot
Spring provides annotations to simplify caching implementation:

1. `@EnableCaching` - Enables Spring’s cache management capability.
   ```java
   @Configuration
   @EnableCaching
   public class CacheConfig {
   }
   ```

2. `@Cacheable` - Caches method results to improve performance.
   ```java
   @Cacheable(value = "weatherCache", key = "#city")
   public Weather getWeather(String city) {
       return weatherRepository.findByCity(city);
   }
   ```

3. `@CachePut` - Updates the cache with the latest data when a method is executed.
   ```java
   @CachePut(value = "weatherCache", key = "#city")
   public Weather updateWeather(String city, Weather weather) {
       return weatherRepository.save(weather);
   }
   ```

4. `@CacheEvict` - Removes cache entries when data changes.
   ```java
   @CacheEvict(value = "weatherCache", key = "#city")
   public void deleteWeather(String city) {
       weatherRepository.deleteByCity(city);
   }
   ```

5. `@Caching` - Combines multiple caching annotations.
   ```java
   @Caching(
       evict = { @CacheEvict(value = "weatherCache", key = "#city") },
       put = { @CachePut(value = "weatherCache", key = "#city") }
   )
   public Weather updateWeather(String city, Weather weather) {
       return weatherRepository.save(weather);
   }
   ```

## Running the Application
1. Start Redis Server (`redis-server` command).
2. Run the Spring Boot application (`mvn spring-boot:run`).
3. Access Swagger UI at [http://localhost:8777/swagger-ui.html](http://localhost:8777/swagger-ui.html).

## Essential Redis Commands

### Basic Commands
```sh
PING                 # Check Redis server connection
SET key value       # Store a key-value pair
GET key             # Retrieve the value of a key
DEL key             # Delete a key
EXISTS key          # Check if a key exists
KEYS *              # List all keys
FLUSHALL            # Clear all data in Redis
```

### Advanced Cache Management
```sh
TTL key             # Get time-to-live of a key
EXPIRE key seconds  # Set expiration time for a key
PERSIST key         # Remove expiration from a key
```

### Working with Lists
```sh
LPUSH key value     # Insert value at the beginning of a list
RPUSH key value     # Insert value at the end of a list
LPOP key           # Remove and return the first element
RPOP key           # Remove and return the last element
LRANGE key start stop  # Get a range of elements from a list
```

### Working with Hashes
```sh
HSET key field value  # Set a field in a hash
HGET key field        # Get a field value from a hash
HGETALL key          # Get all fields and values from a hash
```

## Checking Cache in Redis CLI
Use the following commands to inspect cache data:
```sh
$ redis-cli
127.0.0.1:6379> KEYS *
127.0.0.1:6379> GET "weather::Bangalore"
```

## Conclusion
This project provides a hands-on implementation of caching in Spring Boot using Redis. It demonstrates how caching improves performance and how different caching techniques (in-memory vs Redis) impact scalability and reliability.


