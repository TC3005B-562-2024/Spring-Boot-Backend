package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.filter.FilterDTO;
import tc3005b224.amazonconnectinsights.dto.information.AgentInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.ContactInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;

@Service
public class AgentService {
    @Autowired
    private SkillService skillService;

    @Autowired
    private AlertService alertService;
    @Autowired
    private TrainingsService trainingsService;

    // Service that calls amazon connects ListUsers and returns a list of AgentDTO
    public List<AgentDTO> findByInstance(String instanceId, int maxResults, String nextToken) throws Exception {
        // TODO: Call Amazon Connect's ListUsers endpoint and enerate a list of AgentDTO.
        List<AgentDTO> agents = new ArrayList<AgentDTO>();

        for (Integer i = 1; i < 3; i++) {
            try {
                AgentDTO agent = this.findById(instanceId, i.toString());
                agents.add(agent);
            }catch (Error e) {
                throw new Exception("Something failed.");
            }
        }

        return agents;
    }

    public AgentDTO findById(String instanceId, String agentId) throws Exception {
        // TODO: Call Amazon Connect's DescribeUser endpoint and populate AgentDTO.
        // TODO: Call Amazon Connect's Contact Lens and check if it exists, if it does use it
        // TODO: Find Out how to connect agent, user and contact.

        String resource = "agent:" + agentId;

        List<String> resourceArray = new ArrayList<String>();
        resourceArray.add(resource);
        FilterDTO filter = new FilterDTO("resource", resourceArray);
        List<FilterDTO> filterArray = new ArrayList<FilterDTO>();
        filterArray.add(filter);

        ContactInformationDTO contactInformationDTO = new ContactInformationDTO("123", "1:43 min", true, "POSITIVE");
        AlertPriorityDTO alertPriorityDTO = alertService.findByResource(1, resource);

        try {
            List<SkillBriefDTO> skills = skillService.findByAgentId(instanceId, agentId);
            AgentInformationDTO agentInformationDTO = new AgentInformationDTO("John Doe", skills.get(0).getAlias(), "ROUTABLE");
            Iterable<TrainingDTO> trainings = trainingsService.findAll(filterArray);

            return new AgentDTO(agentId, resource, skills, agentInformationDTO, contactInformationDTO, alertPriorityDTO, trainings);
        }catch (Error e) {
            throw new Exception("Something failed.");
        }
    }
}
