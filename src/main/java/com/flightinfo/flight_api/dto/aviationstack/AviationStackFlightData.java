package com.flightinfo.flight_api.dto.aviationstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Este record representa un único vuelo dentro del array "data" del JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public record AviationStackFlightData(
        @JsonProperty("flight_date")
        String flightDate,

        @JsonProperty("flight_status")
        String flightStatus,

        // ¡Aquí anidamos los otros records que creamos!
        AviationStackDepartureArrival departure,
        AviationStackDepartureArrival arrival,
        AviationStackFlight flight
) {}
