package com.flightinfo.flight_api.controller;

import com.flightinfo.flight_api.service.FlightDataService;
// ¡¡CAMBIO DE IMPORTACIÓN!!
import com.flightinfo.flight_api.dto.response.FlightDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para todo lo relacionado con los vuelos.
 * (REST Controller for everything related to flights).
 */
@RestController
@RequestMapping("/api/v1/flights") // Todas las URLs en esta clase empezarán con /api/v1/flights
public class FlightController {

    // 1. Inyecta nuestro nuevo servicio
    private final FlightDataService flightDataService;

    @Autowired // Le dice a Spring que "inyecte" automáticamente la instancia de FlightDataService
    public FlightController(FlightDataService flightDataService) {
        this.flightDataService = flightDataService;
    }

    /**
     * ¡MÉTODO MODIFICADO!
     * Este es el endpoint "real". Obtiene datos de vuelos para un aeropuerto.
     * (MODIFIED METHOD!
     * (This is the "real" endpoint. It gets flight data for an airport).
     *
     * @param iataCode El código IATA del aeropuerto (ej: "JFK")
     * @return Un objeto JSON. Spring convierte nuestro objeto 'AviationStackResponse' en JSON.
     */
    @GetMapping("/airport/{iataCode}")
    public FlightDataResponse getFlightsForAirport(@PathVariable String iataCode) {
        // 2. Llama al nuevo método de nuestro servicio
        return flightDataService.getFlightsByAirport(iataCode);
    }

    /*
     * Ya no necesitamos este endpoint de prueba, pero lo dejaré comentado
     * por si lo necesitamos más adelante.
     *
    @GetMapping("/test/{iataCode}")
    public String getTestUrlForAirport(@PathVariable String iataCode) {
        // 2. Llama al método de nuestro servicio
        return flightDataService.getFlightsUrlForAirport(iataCode);
    }
    */
}