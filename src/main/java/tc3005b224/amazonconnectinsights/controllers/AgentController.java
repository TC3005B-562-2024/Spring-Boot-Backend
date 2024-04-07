package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDataDTO;


@RestController
@RequestMapping("/agents")
public class AgentController {
    @PostMapping("/{id}/transfer")
    public ResponseEntity<String> transfer(@RequestBody SkillDataDTO skill, @PathVariable("id") int id)  {
        // TODO: Call API that shows status of the agent with the specified id.
        // TODO: Check status if on-call or disconnected send error.
        // TODO: Check status if available move to skill stablished.

        String result = String.format("Tranfer agent %d, to skill %s.", id, skill.getId());
        return ResponseEntity.ok(result);
    }
}
