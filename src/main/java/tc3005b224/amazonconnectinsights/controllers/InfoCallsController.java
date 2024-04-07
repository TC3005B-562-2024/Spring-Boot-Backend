// TODOs
// Vinculación con la base de datos para que el endpoint funcione con los ids reales de agentes y llamadas.
// Confirmar como se manejará la información del cliente y si se empleará id, para que en caso de no ser así mejorar
// y actualizar el endpoint con la información real a utilizar.


package tc3005b224.amazonconnectinsights.controllers;

import tc3005b224.amazonconnectinsights.dto.*;
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

@RestController
@RequestMapping("/agents/")
    public class InfoCallsController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found call information", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InfoCallsDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent or Call ID not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server couldn't process the application", content = @Content),
            @ApiResponse(responseCode = "503", description = "Server couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Get the individual information of a call attended by an specific agent during the day")
    @GetMapping("/1/calls/1")
    public ResponseEntity<List<InfoCallsDTO>> getCallInfo() {
        List<InfoCallsDTO> result = new ArrayList<>();
        InfoCallsDTO call1 = new InfoCallsDTO();
        call1.setClient_id(1);
        call1.setDate("2024-04-07 15:30:00");
        call1.setCall_duration("01:30:15");
        List<EmotionDTO> emotionsdetected = new ArrayList<>();
        EmotionDTO e1 = new EmotionDTO();
        e1.setEmotion("angry");
        e1.setTime("00:50:00");
        emotionsdetected.add(e1);
        call1.setEmotions_detected(emotionsdetected);
        result.add(call1);
        return ResponseEntity.ok(result);
    }
}
