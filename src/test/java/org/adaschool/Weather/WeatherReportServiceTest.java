package org.adaschool.Weather;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherReportServiceTest {

    @InjectMocks
    private WeatherReportService weatherReportService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testGetWeatherReport() {
        // Arrange
        double latitude = 37.8267;
        double longitude = -122.4233;
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
        // Cambiamos la temperatura a 298.15 Kelvin (25°C)
        weatherApiResponse.setMain(new WeatherApiResponse.Main(298.15, 60.0));

        when(restTemplate.getForObject(
                "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + "890d956f7be244951684f6e296bb0164",
                WeatherApiResponse.class))
                .thenReturn(weatherApiResponse);

        // Act
        WeatherReport weatherReport = weatherReportService.getWeatherReport(latitude, longitude);

        // Assert
        assertEquals(25.0, weatherReport.getTemperature(), 0.01);
        assertEquals(60.0, weatherReport.getHumidity());
    }
    @Test
    void testGetWeatherReport_NullResponse() {
        // Arrange
        double latitude = 37.8267;
        double longitude = -122.4233;

        when(restTemplate.getForObject(
                "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + "890d956f7be244951684f6e296bb0164",
                WeatherApiResponse.class))
                .thenReturn(null);

        // Act
        WeatherReport weatherReport = weatherReportService.getWeatherReport(latitude, longitude);

        // Assert
        assertEquals(0.0, weatherReport.getTemperature(), 0.01);
        assertEquals(0.0, weatherReport.getHumidity(), 0.01);
    }
    @Test
    void testGetWeatherReport_MissingMainData() {
        // Arrange
        double latitude = 37.8267;
        double longitude = -122.4233;
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();

        weatherApiResponse.setMain(null);

        when(restTemplate.getForObject(
                "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + "890d956f7be244951684f6e296bb0164",
                WeatherApiResponse.class))
                .thenReturn(weatherApiResponse);

        // Act
        WeatherReport weatherReport = weatherReportService.getWeatherReport(latitude, longitude);

        // Assert
        assertEquals(0.0, weatherReport.getTemperature(), 0.01);
        assertEquals(0.0, weatherReport.getHumidity(), 0.01);
    }
    @Test
    void testGetWeatherReport_HighTemperature() {
        // Arrange
        double latitude = 37.8267;
        double longitude = -122.4233;
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
        weatherApiResponse.setMain(new WeatherApiResponse.Main(500.0, 60.0));

        when(restTemplate.getForObject(
                "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + "890d956f7be244951684f6e296bb0164",
                WeatherApiResponse.class))
                .thenReturn(weatherApiResponse);

        // Act
        WeatherReport weatherReport = weatherReportService.getWeatherReport(latitude, longitude);

        // Assert
        assertTrue(weatherReport.getTemperature() > 226.85);
    }




}
