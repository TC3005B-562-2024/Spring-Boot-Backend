package tc3005b224.amazonconnectinsights.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.stats.NumberOfContactsDTO;
import tc3005b224.amazonconnectinsights.dto.stats.OccupancyDTO;
import tc3005b224.amazonconnectinsights.dto.stats.ServiceLevelDTO;
import tc3005b224.amazonconnectinsights.dto.stats.SpeedOfAnswerDTO;
import tc3005b224.amazonconnectinsights.dto.stats.TimeWindowDTO;

@RestController
@RequestMapping("/stats")
public class StatsController {

    // Método para obtener la ocupación de los agentes en un intervalo de tiempo
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Occupancy value retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OccupancyDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Occupancy data not found", content = @Content),
    })
    @Operation(summary = "Get agents occupancy within a time window")
    @GetMapping("/occupancy")
    public ResponseEntity<OccupancyDTO> getAgentsOccupancy(@RequestParam Date start, @RequestParam Date end) {
        // Aquí se simularía la lógica para calcular la ocupación de los agentes en el intervalo de tiempo
        double calculatedOccupancy = calculateOccupancy(start, end);

        OccupancyDTO occupancyDTO = new OccupancyDTO();
        occupancyDTO.setOccupancyValue(calculatedOccupancy);

        TimeWindowDTO timeWindowDTO = new TimeWindowDTO();
        timeWindowDTO.setStart(start);
        timeWindowDTO.setEnd(end);

        occupancyDTO.setTimeWindow(timeWindowDTO);

        return ResponseEntity.ok(occupancyDTO);
    }

    // Método para obtener la velocidad media de respuesta en un intervalo de tiempo
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average speed of answer retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpeedOfAnswerDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Speed of answer data not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Date format invalid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpeedOfAnswerDTO.class))
            }),
    })
    @Operation(summary = "Get average speed of answer within a time window")
    @GetMapping("/asa")
    public ResponseEntity<List<SpeedOfAnswerDTO>> getAverageSpeedOfAnswer(@RequestParam Date start, @RequestParam Date end) {
        // Simular la obtención de la velocidad media de respuesta en el intervalo de tiempo
        List<SpeedOfAnswerDTO> speedOfAnswerDTOList = new ArrayList<>();

        // Simular múltiples velocidades medias de respuesta
        for (int i = 0; i < 1; i++) {
            double calculatedSpeedOfAnswer = calculateSpeedOfAnswer(start, end);

            SpeedOfAnswerDTO speedOfAnswerDTO = new SpeedOfAnswerDTO();
            speedOfAnswerDTO.setSpeedOfAnswerValue(calculatedSpeedOfAnswer);

            TimeWindowDTO timeWindowDTO = new TimeWindowDTO();
            timeWindowDTO.setStart(start);
            timeWindowDTO.setEnd(end);

            speedOfAnswerDTO.setTimeWindow(timeWindowDTO);

            speedOfAnswerDTOList.add(speedOfAnswerDTO);
        }

        return ResponseEntity.ok(speedOfAnswerDTOList);     //Retornar la lista de velocidades medias de respuesta
    }

    // Método para obtener el número de contactos perdidos en un intervalo de tiempo
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Number of lost contacts retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NumberOfContactsDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Date format invalid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NumberOfContactsDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content),
    })
    @Operation(summary = "Get number of lost contacts within a time window")
    @GetMapping("/lost-contacts")
    public ResponseEntity<NumberOfContactsDTO> getNumberOfLostContacts(@RequestParam Date start, @RequestParam Date end) {
        // Simulación de la obtención del número de contactos perdidos en el intervalo de tiempo
        int lostContacts = calculateLostContacts(start, end);

        NumberOfContactsDTO numberOfContactsDTO = new NumberOfContactsDTO();
        numberOfContactsDTO.setNumberOfContacts(lostContacts);

        TimeWindowDTO timeWindowDTO = new TimeWindowDTO();
        timeWindowDTO.setStart(start);
        timeWindowDTO.setEnd(end);

        numberOfContactsDTO.setTimeWindow(timeWindowDTO);

        return ResponseEntity.ok(numberOfContactsDTO);
    }

    // Método para obtener el valor del nivel de servicio en un intervalo de tiempo
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service level value retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceLevelDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Date format invalid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceLevelDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content),
    })
    @Operation(summary = "Get service level value within a time window")
    @GetMapping("/service-level")
    public ResponseEntity<ServiceLevelDTO> getServiceLevel(@RequestParam Date start, @RequestParam Date end) {
        // Simulación de la obtención del valor del nivel de servicio en el intervalo de tiempo
        double serviceLevelValue = calculateServiceLevel(start, end);

        ServiceLevelDTO serviceLevelDTO = new ServiceLevelDTO();
        serviceLevelDTO.setServiceLevelValue(serviceLevelValue);

        TimeWindowDTO timeWindowDTO = new TimeWindowDTO();
        timeWindowDTO.setStart(start);
        timeWindowDTO.setEnd(end);

        serviceLevelDTO.setTimeWindow(timeWindowDTO);

        return ResponseEntity.ok(serviceLevelDTO);
    }

    // Método de utilidad para calcular el número de contactos perdidos
    private int calculateLostContacts(Date start, Date end) {
        // Simulación de datos de contactos perdidos para un intervalo de tiempo dado (start, end)
        // Aquí se puede implementar la lógica para obtener el número de contactos perdidos desde la base de datos u otro origen de datos.
        // Por ahora, se generará un valor aleatorio para la demostración.
        return (int) (Math.random() * 100); // Generar un valor aleatorio entre 0 y 100 para representar el número de contactos perdidos.
    }

    // Método de utilidad para calcular el valor del nivel de servicio
    private double calculateServiceLevel(Date start, Date end) {
        // Simulación de datos del valor del nivel de servicio para un intervalo de tiempo dado (start, end)
        // Aquí se puede implementar la lógica para calcular el nivel de servicio desde la base de datos u otro origen de datos.
        // Por ahora, se generará un valor aleatorio para la demostración.
        return Math.random(); // Generar un valor aleatorio entre 0 y 1 para representar el nivel de servicio.
    }

    // Método de utilidad para calcular la ocupación de los agentes
    private double calculateOccupancy(Date start, Date end) {
        // Simulación de datos de ocupación para un intervalo de tiempo dado (start, end)

        // Simular una lista de tiempos de actividad de agentes (en milisegundos desde la época)
        List<Long> agentActivityTimes = Arrays.asList(
                1619852400000L, // 1 de mayo de 2021, 9:00 AM
                1619859600000L, // 1 de mayo de 2021, 11:00 AM
                1619863200000L, // 1 de mayo de 2021, 12:00 PM
                1619874000000L, // 1 de mayo de 2021, 3:00 PM
                1619881200000L  // 1 de mayo de 2021, 5:00 PM
        );

        // Contar la cantidad de agentes activos dentro del intervalo de tiempo
        long startTimeMillis = start.getTime();
        long endTimeMillis = end.getTime();
        int activeAgentsCount = 0;

        for (Long activityTime : agentActivityTimes) {
            if (activityTime >= startTimeMillis && activityTime <= endTimeMillis) {
                // El agente estuvo activo dentro del intervalo de tiempo dado
                activeAgentsCount++;
            }
        }

        // Calcular la ocupación como un porcentaje de agentes activos respecto al total de agentes
        int totalAgents = agentActivityTimes.size();
        double occupancyPercentage = (double) activeAgentsCount / totalAgents * 100;

        return occupancyPercentage;
    }

    // Método de utilidad para calcular la velocidad media de respuesta
    private double calculateSpeedOfAnswer(Date start, Date end) {
        // Simulación de datos de velocidad media de respuesta para un intervalo de tiempo dado (start, end)
        // Aquí se cambiara deacuerdo a como se tenga en la base de datos la lógica para calcular la velocidad media de respuesta
        return Math.random() * 100; // Valor aleatorio entre 0 y 100
    }
}
