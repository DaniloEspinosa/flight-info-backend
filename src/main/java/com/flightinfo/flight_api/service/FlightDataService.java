package com.flightinfo.flight_api.service;

import com.flightinfo.flight_api.dto.aviationstack.AviationStackFlightData;
import com.flightinfo.flight_api.dto.aviationstack.AviationStackResponse;
import com.flightinfo.flight_api.dto.response.FlightDataResponse;
import com.flightinfo.flight_api.dto.response.FlightDetailDto;
import com.flightinfo.flight_api.dto.response.FlightResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class FlightDataService {

    // 1. Inyecta los valores desde application.properties
    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.baseurl}")
    private String baseUrl;

    // 2. Esta es la herramienta de Spring para hacer llamadas a otras APIs
    private final RestTemplate restTemplate;

    // Formateador para las horas
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FlightDataService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Método de prueba (legacy) que solo construye la URL.
     * (Legacy test method that just builds the URL).
     *
     * @param airportCode El código IATA del aeropuerto (ej: "JFK")
     * @return La URL que se usaría para la llamada a la API.
     */
    public String getFlightsUrlForAirport(String airportCode) {

        // Construimos la URL
        String url = baseUrl + "/flights" +
                "?access_key=" + apiKey +
                "&dep_iata=" + airportCode; // dep_iata = Salidas (Departures)

        return url;
    }

    /**
     * ¡MÉTODO MODIFICADO!
     * Llama a la API de AviationStack, transforma los datos a nuestro formato
     * y calcula las zonas horarias.
     * (MODIFIED METHOD!
     * Calls AviationStack API, transforms data to our format, and calculates timezones).
     *
     * @param airportCode El código IATA del aeropuerto (ej: "JFK")
     * @return Un objeto 'FlightDataResponse' con los datos procesados.
     */
    public FlightDataResponse getFlightsByAirport(String airportCode) {

        String url = baseUrl + "/flights" +
                "?access_key=" + apiKey +
                "&dep_iata=" + airportCode;

        try {
            // 1. Llama a la API y la convierte a nuestros DTOs de AviationStack
            AviationStackResponse externalResponse = restTemplate.getForObject(url, AviationStackResponse.class);

            if (externalResponse != null && externalResponse.data() != null) {
                // 2. ¡¡AQUÍ SUCEDE LA TRANSFORMACIÓN!!
                // Convierte la lista de datos de AviationStack a nuestra lista de DTOs de respuesta
                List<FlightResponseDto> processedFlights = externalResponse.data().stream()
                        .map(this::transformToFlightResponseDto)
                        .toList();

                // 3. Devuelve nuestro objeto de respuesta personalizado
                return new FlightDataResponse(processedFlights);
            } else {
                // Si la API no devuelve nada, devolvemos una lista vacía
                return new FlightDataResponse(Collections.emptyList());
            }

        } catch (Exception e) {
            // Manejo de errores simple
            System.err.println("Error al llamar o parsear la API de AviationStack: " + e.getMessage());
            return new FlightDataResponse(Collections.emptyList());
        }
    }

    /**
     * Método privado "helper" para transformar un objeto de AviationStack
     * en nuestro objeto de respuesta DTO.
     * (Private helper method to transform an AviationStack object
     * into our response DTO object).
     */
    private FlightResponseDto transformToFlightResponseDto(AviationStackFlightData externalFlight) {

        // 1. Extraer los datos de salida (departure)
        FlightDetailDto departureDto = new FlightDetailDto(
                externalFlight.departure().airport(),
                externalFlight.departure().iataCode(),
                formatLocalTime(externalFlight.departure().scheduledTime())
        );

        // 2. Extraer los datos de llegada (arrival)
        FlightDetailDto arrivalDto = new FlightDetailDto(
                externalFlight.arrival().airport(),
                externalFlight.arrival().iataCode(),
                formatLocalTime(externalFlight.arrival().scheduledTime())
        );

        // 3. ¡¡LA MAGIA DE LA ZONA HORARIA!!
        String arrivalTimeInDepartureZone = convertTimezone(
                externalFlight.arrival().scheduledTime(), // Hora de llegada
                externalFlight.departure().timezone()     // Zona horaria de SALIDA
        );

        // 4. Construir y devolver nuestro objeto
        return new FlightResponseDto(
                externalFlight.flight().flightNumber(),
                externalFlight.flightStatus(),
                departureDto,
                arrivalDto,
                arrivalTimeInDepartureZone
        );
    }

    /**
     * Parsea una fecha/hora (ej: "2025-11-12T10:30:00+00:00")
     * y la devuelve como un string local (ej: "2025-11-12T10:30:00").
     */
    private String formatLocalTime(String dateTimeString) {
        if (dateTimeString == null) return null;
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString);
            return zonedDateTime.format(OUTPUT_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error al formatear fecha: " + dateTimeString + " - " + e.getMessage());
            return dateTimeString; // Devolvemos el original si falla
        }
    }

    /**
     * Convierte un string de fecha/hora (ZonedDateTime) a una zona horaria
     * de destino diferente.
     */
    private String convertTimezone(String dateTimeString, String targetZoneId) {
        if (dateTimeString == null || targetZoneId == null) return null;

        try {
            // 1. Parsear la hora original (ej: 14:00 en JFK, que es -05:00)
            ZonedDateTime originalTime = ZonedDateTime.parse(dateTimeString);

            // 2. Definir la zona horaria de destino (ej: "Europe/Madrid")
            ZoneId targetZone = ZoneId.of(targetZoneId);

            // 3. Convertir la hora a esa zona horaria (ej: 20:00 en Madrid)
            ZonedDateTime convertedTime = originalTime.withZoneSameInstant(targetZone);

            // 4. Formatear como string local
            return convertedTime.format(OUTPUT_FORMATTER);

        } catch (Exception e) {
            System.err.println("Error al convertir zona horaria: " + dateTimeString + " a " + targetZoneId + " - " + e.getMessage());
            return null;
        }
    }
}