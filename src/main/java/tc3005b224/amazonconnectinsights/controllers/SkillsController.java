package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.service.SkillService;

@RestController
@RequestMapping("/skills")
public class SkillsController {
    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillBriefDTO>> getAllSkills(
            @RequestParam(required = true) String token) {
        List<SkillBriefDTO> response = skillService.findByInstance(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<SkillDTO> getSkillById(
            @PathVariable String skillId,
            @RequestBody Principal principal) {
        SkillDTO response = skillService.findById(principal.getName(), skillId);
        return ResponseEntity.ok(response);
    }
}
