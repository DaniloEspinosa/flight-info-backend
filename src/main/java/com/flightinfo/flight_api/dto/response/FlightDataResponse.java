package com.flightinfo.flight_api.dto.response;

import java.util.List;

/**
 * El objeto de respuesta de nivel superior para nuestra API de vuelos.
 * (The top-level response object for our flights API).
 */
public record FlightDataResponse(
        List<FlightResponseDto> flights
) {}