package tc3005b224.amazonconnectinsights.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.ChatResultsDTO;
import tc3005b224.amazonconnectinsights.dto.TranscriptionDTO;

@RestController
@RequestMapping("/chats")
public class ChatResultsController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found agent chats analysis", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResultsDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Chat ID not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server couldn´t process the application", content = @Content),
            @ApiResponse(responseCode = "503", description = "Server couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Get the results of the analysis of the chats attended by an agent")
    @GetMapping("/{chat_id}")
    public ResponseEntity<List<ChatResultsDTO>> getChatResults(@PathVariable("chat_id") int chatId) {
        List<ChatResultsDTO> result = new ArrayList<>();
        ChatResultsDTO chat1 = new ChatResultsDTO();
        chat1.setChat_id(chatId);
        chat1.setAgent_id(1);
        TranscriptionDTO transcription1 = new TranscriptionDTO();
        transcription1.setContent("Lorem ipsum dolor sit amet. Est illum eveniet non dolor maiores ab maiores explicabo qui harum internos! Sit internos recusandae et fugit laboriosam ut quis eaque cum ducimus possimus.");
        transcription1.setId("XXXX");
        transcription1.setParticipant_id("1");
        transcription1.setParticipant_role("AGENT");
        transcription1.setTime("00:25:15");
        transcription1.setDisplay_name("Iván Hernández");
        transcription1.setSentiment("POSITIVE");
        chat1.setResults(transcription1);
        chat1.setStatus("COMPLETED");
        result.add(chat1);
        return ResponseEntity.ok(result);
    }
}
