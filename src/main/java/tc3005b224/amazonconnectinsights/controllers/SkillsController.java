package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.service.SkillService;

@RestController
@RequestMapping("/skills")
public class SkillsController {
    @Autowired
    private SkillService skillService;

    @Operation(summary = "Get all skills (routing profiles from the instance)", responses = {
            @ApiResponse(responseCode = "200", description = "List of skills", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SkillBriefDTO.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }) })
    @GetMapping
    public ResponseEntity<List<SkillBriefDTO>> getAllSkills(Principal principal) {
        List<SkillBriefDTO> response = skillService.findByInstance(principal.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get the details from a single skill (routing profile)", responses = {
        @ApiResponse(responseCode = "200", description = "Skill information", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDTO.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }) })
    @GetMapping("/{skillId}")
    public ResponseEntity<?> getSkillById(
            @PathVariable String skillId, Principal principal) {
        SkillDTO response;
        try {
            response = skillService.findById(principal.getName(), skillId);
        } catch (BadRequestException e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
        return ResponseEntity.ok(response);
    }
}
