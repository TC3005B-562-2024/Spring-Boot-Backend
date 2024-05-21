package tc3005b224.amazonconnectinsights;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import tc3005b224.amazonconnectinsights.controllers.InstanceController;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;
import tc3005b224.amazonconnectinsights.service.InstanceService;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class InstanceControllerTests {

    @Autowired
    private InstanceService instanceService;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void getInstanceDetailsTest()throws Exception {
        String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlIjoiYWdlbnQiLCJpbnN0YW5jZUlkIjoieW91ci1pbnN0YW5jZS1pZCJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        mockMvc.perform(MockMvcRequestBuilders.get("/instance?token=" + tokenValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
    @Test
    public void tokenExceptionGetInstanceDetailsTest()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/instance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }
    @Test
    void getInstanceDetailsServiceUnavailableTest() throws Exception {
        String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlIjoiYWdlbnQiLCJpbnN0YW5jZUlkIjoieW91ci1pbnN0YW5jZS1pZCJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        // Perform the request and expect a 500 Internal Server Error status
        mockMvc.perform(MockMvcRequestBuilders.get("/instance/test?token=" + tokenValue))
                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
                .andDo(print());
    }

}