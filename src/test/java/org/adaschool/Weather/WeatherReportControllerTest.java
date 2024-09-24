package org.adaschool.Weather;

import org.adaschool.Weather.controller.WeatherReportController;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class WeatherReportControllerTest {

    private WeatherReportController weatherReportController;
    private WeatherReportService weatherReportService;

    @BeforeEach
    public void setUp() {
        weatherReportService = Mockito.mock(WeatherReportService.class);
        weatherReportController = new WeatherReportController(weatherReportService);
    }

    @Test
    public void testGetWeatherReport_Success() {
        double latitude = 37.8267;
        double longitude = -122.4233;

        WeatherReport expectedReport = new WeatherReport();
        expectedReport.setTemperature(20.5);
        expectedReport.setHumidity(50.0);

        when(weatherReportService.getWeatherReport(latitude, longitude)).thenReturn(expectedReport);

        WeatherReport actualReport = weatherReportController.getWeatherReport(latitude, longitude);

        assertEquals(expectedReport.getTemperature(), actualReport.getTemperature());
        assertEquals(expectedReport.getHumidity(), actualReport.getHumidity());
    }

    @Test
    public void testGetWeatherReport_InvalidCoordinates() {
        double latitude = 999.0; // Invalid latitude
        double longitude = 999.0; // Invalid longitude

        when(weatherReportService.getWeatherReport(latitude, longitude)).thenThrow(new IllegalArgumentException("Invalid coordinates"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            weatherReportController.getWeatherReport(latitude, longitude);
        });

        assertEquals("Invalid coordinates", exception.getMessage());
    }

    @Test
    public void testGetWeatherReport_ServiceUnavailable() {
        double latitude = 37.8267;
        double longitude = -122.4233;

        when(weatherReportService.getWeatherReport(latitude, longitude)).thenThrow(new RuntimeException("Service unavailable"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            weatherReportController.getWeatherReport(latitude, longitude);
        });

        assertEquals("Service unavailable", exception.getMessage());
    }
}