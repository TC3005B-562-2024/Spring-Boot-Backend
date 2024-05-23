package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.RoutingProfile;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class SkillService extends BaseService {
    @Autowired
    private AlertService alertService;
    
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

    // Service that retrieves the Skill (Routing Profile) information given its skillId.
    public SkillDTO findById(String token, String skillId) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        DescribeRoutingProfileRequest describeRoutingProfileRequest = DescribeRoutingProfileRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .routingProfileId(skillId)
                .build();
        DescribeRoutingProfileResponse describeRoutingProfileResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeRoutingProfile(describeRoutingProfileRequest);
        RoutingProfile data = describeRoutingProfileResponse.routingProfile();
        AlertPriorityDTO alerts = alertService.findByResource(clientInfo.getConnectionIdentifier(), data.routingProfileArn());

        Instant createdAt = Instant.now(); // Mocked creation time
        SkillsInformationDTO skillsInformationDTO = new SkillsInformationDTO(data.name(), createdAt, data.numberOfAssociatedUsers());
        return new SkillDTO(
                skillId,
                data.name(),
                createdAt, // Assuming createdAt is a mock
                data.numberOfAssociatedUsers(),
                null, // serviceLevel
                null, // acr
                null, // asa
                null, // fcr
                null, // adherence
                alerts,
                skillsInformationDTO, // skillsInformationDTO
                null, // trainings
                null  // agents
        );
    }
    
    // Service that retrieves the Skills (Routing Profile) of an agent given its agentId.
    public SkillsInformationDTO findByAgentId(String isntanceId, String agentId) throws Exception {
        // TODO: Call Amazon Connect endpoint capable of retrieveng the Skills of an agent.
        String alias = "Agent Alias";
        Instant createdAt = Instant.now();
        Long totalAgents = 10L;

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

        return new SkillsInformationDTO(alias, createdAt, totalAgents);
    }
}
