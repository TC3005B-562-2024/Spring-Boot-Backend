package tc3005b224.amazonconnectinsights.service;

import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SkillService {
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public List<SkillBriefDTO> findByInstance(String instanceId, int maxResults, String nextToken) {
        // TODO: Call Amazon Connect ListRoutingProfiles endpoint.

        // Mock Skills
        SkillBriefDTO serviceSkill = new SkillBriefDTO("1", "routing-profile:1", "Service", "phone_in_talk");
        SkillBriefDTO salesSkill = new SkillBriefDTO("2", "routing-profile:2", "Sales", "phone_in_talk");
        SkillBriefDTO supportSkill = new SkillBriefDTO("3", "routing-profile:3", "Support", "phone_in_talk");
        SkillBriefDTO qualitySkill = new SkillBriefDTO("4", "routing-profile:4", "Quality", "phone_in_talk");

        // Add Skills to list
        List<SkillBriefDTO> result = new ArrayList<SkillBriefDTO>();
        result.add(serviceSkill);
        result.add(salesSkill);
        result.add(supportSkill);
        result.add(qualitySkill);

        return result;
    }

    // Service that retrieves the Skills (Routing Profile) of an agent given its agentId.
    public List<SkillBriefDTO> findByAgentId(String isntanceId, String agentId) throws Exception {
        // TODO: Call Amazon Connect endpoint capable of retrieveng the Skills of an agent.

        // Mock Skills
        SkillBriefDTO serviceSkill = new SkillBriefDTO("1", "routing-profile:1", "Service", "phone_in_talk");
        SkillBriefDTO salesSkill = new SkillBriefDTO("2", "routing-profile:2", "Sales", "phone_in_talk");
        SkillBriefDTO supportSkill = new SkillBriefDTO("3", "routing-profile:3", "Support", "phone_in_talk");
        SkillBriefDTO qualitySkill = new SkillBriefDTO("4", "routing-profile:4", "Quality", "phone_in_talk");

        // Add Skills to list
        List<SkillBriefDTO> result = new ArrayList<SkillBriefDTO>();

        if(Objects.equals(agentId, "1")){
            result.add(serviceSkill);
            result.add(salesSkill);
            result.add(supportSkill);
            result.add(qualitySkill);
        } else if (Objects.equals(agentId, "2")) {
            result.add(supportSkill);
            result.add(qualitySkill);
        }else {
            throw new Exception("Agent with id: " + agentId + ", not found!");
        }

        return result;
    }
}
