package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;

@Service
public class SkillService extends BaseService {
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public List<SkillBriefDTO> findByInstance(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        // Build the request for the ListRoutingProfiles endpoint using the instanceId
        ListRoutingProfilesRequest listRoutingProfilesRequest = ListRoutingProfilesRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .build();
        // The response of the ListRoutingProfiles endpoint
        ListRoutingProfilesResponse listRoutingProfilesResponse = getConnectClient(clientInfo.getAccessKeyId(),
        clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .listRoutingProfiles(listRoutingProfilesRequest);
        // The list of Routing Profiles, excluding the nextToken, hopefully the list is not too long.
        // If it is, we will need to implement pagination.
        List<RoutingProfileSummary> data = listRoutingProfilesResponse.routingProfileSummaryList();
        // Build the result as a list of SkillBriefDTO
        List<SkillBriefDTO> result = new ArrayList<SkillBriefDTO>();
        data.forEach(routingProfileSummary -> {
            result.add(new SkillBriefDTO(routingProfileSummary.id(), routingProfileSummary.arn(),
                    routingProfileSummary.name(), "phone_in_talk"));
        });
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
