package com.flightinfo.flight_api.dto.response;

/**
 * Un DTO (Data Transfer Object) que representa la información
 * de un punto de salida (departure) o llegada (arrival) para nuestro frontend.
 * (A DTO representing departure or arrival info for our frontend).
 */
public record FlightDetailDto(
        String airportName,
        String iataCode,
        String localTime // La hora local en ESE aeropuerto (ej: "2025-11-12T10:30:00")
) {}