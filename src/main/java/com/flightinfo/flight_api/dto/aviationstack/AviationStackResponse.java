package com.flightinfo.flight_api.dto.aviationstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Este es el objeto de más alto nivel.
// El JSON tiene "pagination" y "data", pero solo nos importa "data".
@JsonIgnoreProperties(ignoreUnknown = true)
public record AviationStackResponse(
        // Mapea el array "data" del JSON a una Lista de nuestros objetos
        List<AviationStackFlightData> data
) {}