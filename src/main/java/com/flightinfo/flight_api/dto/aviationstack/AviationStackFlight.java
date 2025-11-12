package com.flightinfo.flight_api.dto.aviationstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AviationStackFlight(
        @JsonProperty("number")
        String flightNumber
) {}