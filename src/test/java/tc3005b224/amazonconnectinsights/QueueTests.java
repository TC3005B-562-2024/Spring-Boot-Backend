package tc3005b224.amazonconnectinsights;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tc3005b224.amazonconnectinsights.controllers.QueueController;
import tc3005b224.amazonconnectinsights.dto.queue.QueueMinDTO;
import tc3005b224.amazonconnectinsights.service.QueueService;

@WebMvcTest(QueueController.class)
public class QueueTests {

    @MockBean
    private QueueService queueService;

    @InjectMocks
    private QueueController queueController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(queueController).build();
    }

    @Test
    public void getAllQueuesMinTest() throws Exception {
        QueueMinDTO queue1 = new QueueMinDTO("1", "Queue 1");
        QueueMinDTO queue2 = new QueueMinDTO("2", "Queue 2");

        given(queueService.findAllMin("testuser", "")).willReturn(Arrays.asList(queue1, queue2));

        Principal mockPrincipal = () -> "testuser";

        mockMvc.perform(get("/queues/min").principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Queue 1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Queue 2"));
    }

    @Test
    public void getAllQueuesMinEmptyTest() throws Exception {
        given(queueService.findAllMin("testuser", "")).willReturn(Collections.emptyList());

        Principal mockPrincipal = () -> "testuser";

        mockMvc.perform(get("/queues/min").principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
