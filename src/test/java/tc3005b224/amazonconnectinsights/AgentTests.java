package tc3005b224.amazonconnectinsights;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class AgentTests extends BaseTest {

    /**
     * ID: B.AgentTests.1 - Test to get all agents from the Amazon Connect instance.
     * Verify that the response contains the following information for each agent:
     * 1. The agent information.
     * 2. The highest priority alert.
     * 3. Queues for the agent.
     * 4. Status of the agent.
     * 5. Sentiment of contacts handled by the agent.
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testGetAllAgents() throws IOException, Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents")
                .header("Authorization", "Bearer " + obtainAuthToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].arn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].queues").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].topPriorityAlert").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sentiment").isEmpty());
    }

    /**
     * ID: B.AgentTests.2 - Test to get a single agent from the Amazon Connect instance.
     * Verify that the response contains the following information for the agent:
     * 1. The agent ID.
     * 2. The agent ARN.
     * 3. The agent information.
     * 4. The agent metrics.
     * 5. The agent alerts.
     * 6. The agent trainings.
     * 7. The agent queues.
     * 8. The agent contacts information.
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testSingleAgent() throws IOException, Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents/6887b106-f684-485e-9c47-a6b1e16cdd21")
                .header("Authorization", "Bearer " + obtainAuthToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("6887b106-f684-485e-9c47-a6b1e16cdd21"))
                .andExpect(MockMvcResultMatchers.jsonPath("arn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("information").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("metrics").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("alerts").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("trainings").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("queues").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("contactInformationDTO").isArray());
    }
}