package com.flightinfo.flight_api.dto.aviationstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Le dice a Jackson (el parser de JSON) que ignore cualquier campo del JSON
// que no esté explícitamente definido en este 'record'. ¡Es muy importante!
@JsonIgnoreProperties(ignoreUnknown = true)
public record AviationStackDepartureArrival(
        String airport,
        String timezone,

        // Mapea el campo "iata" del JSON a nuestra variable "iataCode"
        @JsonProperty("iata")
        String iataCode,

        // Mapea el campo "scheduled" del JSON a nuestra variable "scheduledTime"
        @JsonProperty("scheduled")
        String scheduledTime
) {}
