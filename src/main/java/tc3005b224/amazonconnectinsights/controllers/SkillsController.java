package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tc3005b224.amazonconnectinsights.dto.agent.IdentityInfoDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.dto.skill.CriticalAlertDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/instance")
public class SkillsController {

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
        List<AgentDTO> agents1 = new ArrayList<>();
        AgentDTO agent1 = new AgentDTO("hola", "Juan Pérez", identityInfo1, "juan.perez@ejemplo.com", "Neutral", "0", "2024-04-05T16:43:00Z", "123");

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
}