package com.flightinfo.flight_api.dto.response;

/**
 * Un DTO que representa un único vuelo en el formato que nuestro frontend
 * espera recibir.
 * (A DTO representing a single flight in the format our frontend expects).
 */
public record FlightResponseDto(
        String flightNumber,
        String status,
        FlightDetailDto departure,
        FlightDetailDto arrival,

        // ¡¡LA MAGIA!!
        // La hora de llegada, pero convertida a la zona horaria de SALIDA.
        // (The arrival time, but converted to the DEPARTURE timezone).
        String arrivalTimeInDepartureZone
) {}