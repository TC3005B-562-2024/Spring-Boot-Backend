package tc3005b224.amazonconnectinsights.controllers;

import tc3005b224.amazonconnectinsights.dto.stats.OccupancyDTO;
import tc3005b224.amazonconnectinsights.dto.stats.TimeWindowDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Arrays;

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

}
