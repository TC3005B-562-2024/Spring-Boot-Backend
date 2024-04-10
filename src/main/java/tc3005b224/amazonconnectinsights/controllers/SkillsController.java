package tc3005b224.amazonconnectinsights.controllers;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tc3005b224.amazonconnectinsights.dto.agent.IdentityInfoDTO;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;
import tc3005b224.amazonconnectinsights.dto.skill.ListSkillDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.dto.skill.CriticalAlertDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/instance")
public class SkillsController {
    /**
     * This method is mapped to the "/skills" URL path and responds to HTTP GET requests.
     * It returns a list of SkillDTO objects.
     * Each SkillDTO object represents a skill with its details like skill id, title, description, critical alerts, and agents.
     * The skills are sorted in descending order based on the number of critical alerts.
     *
     * @return ResponseEntity containing the list of SkillDTO objects and HTTP status code.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found instance data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ListSkillDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "The server couldn’t connect with the Amazon Connect API.", content = @Content),
    })
    @GetMapping("/skills")
    public ResponseEntity<List<SkillDTO>> getSkills() {
        List<SkillDTO> skills = new ArrayList<>();

        SkillDTO skill1 = new SkillDTO();
        skill1.setSkill_id(123);
        skill1.setTitle("Atención al cliente");
        skill1.setDescription("Habilidad para resolver dudas y problemas de los clientes.");

        List<CriticalAlertDTO> criticalAlerts1 = new ArrayList<>();
        CriticalAlertDTO alert1 = new CriticalAlertDTO();
        alert1.setAgent_id(456);
        alert1.setHora_y_fecha_inicio_llamada(LocalDateTime.now());
        alert1.setInsight("El cliente está molesto por el largo tiempo de espera.");
        alert1.setPrioridad("Alta");
        criticalAlerts1.add(alert1);
        skill1.setCritic_alerts(criticalAlerts1);

        IdentityInfoDTO identityInfo1 = new IdentityInfoDTO("a", "b", "c", "d", "e");
        Date dateqq = new Date();
        List<AgentDTO> agents1 = new ArrayList<>();
        AgentDTO agent1 = new AgentDTO("hola", "Juan Pérez", identityInfo1, "juan.perez@ejemplo.com", "Neutral", "0", "2024-04-05T16:43:00Z", dateqq);

        agents1.add(agent1);
        skill1.setAgents(agents1);

        skills.add(skill1);

        skills.sort((s1, s2) -> {
            int criticalAlertsCount1 = s1.getCritic_alerts().size();
            int criticalAlertsCount2 = s2.getCritic_alerts().size();
            return Integer.compare(criticalAlertsCount2, criticalAlertsCount1);
        });

        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found instance data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ListSkillDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "The server couldn’t connect with the Amazon Connect API.", content = @Content),
    })
    @GetMapping("/skills/{skill_id}")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable("skill_id") int skillId) {
        // Method implementation goes here
        return null;
    }
}