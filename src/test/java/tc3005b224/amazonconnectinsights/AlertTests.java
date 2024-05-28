package tc3005b224.amazonconnectinsights;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import tc3005b224.amazonconnectinsights.repository.AlertRepository;
import tc3005b224.amazonconnectinsights.service.AlertService;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class AlertTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private AlertService alertService;
    private String obtainAuthToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new AuthRequest("test@g.com", "123456", true));

        RequestBody body = RequestBody.create(json, okhttp3.MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyB1NzTkPKLQW-aHJrHwVUKjOVY209urse4")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.path("idToken").asText();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    static class AuthRequest {
        public String email;
        public String password;
        public boolean returnSecureToken;

        public AuthRequest(String email, String password, boolean returnSecureToken) {
            this.email = email;
            this.password = password;
            this.returnSecureToken = returnSecureToken;
        }
    }


    // Test the creation of an alert, and then retrieve it by its id to check if it was created correctly.
    @Test
    public void findAlertByIdTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        Alert alertToDB = alertService.fromDTO(alert);
        Alert savedAlert = alertRepository.save(alertToDB);

        mockMvc.perform(MockMvcRequestBuilders.get("/alerts/"+savedAlert.getId())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAlert.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resource").value("resource"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.solved").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trainingCompleted").value(false));

        ;
        alertRepository.delete(savedAlert);
    }

    // Test the retrieval of an alert that does not exist in the database.
    @Test
    public void findAlertByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alerts/-1")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Test the addition of an alert to the database, and then check if it was added correctly.
    @Test
    public void addAlertTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        String alertJson = new ObjectMapper().writeValueAsString(alert);

        mockMvc.perform(MockMvcRequestBuilders.post("/alerts")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alertJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Alert added successfully"));
        Alert savedAlert = alertRepository.findAll().iterator().next();
        alertRepository.delete(savedAlert);
    }

    // Test the update of an alert in the database, and then check if it was updated correctly.
    @Test
    public void updateAlertTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        Alert alertToDB = alertService.fromDTO(alert);
        Alert savedAlert = alertRepository.save(alertToDB);

        AlertDTO updatedAlert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", true, true);
        String updatedAlertJson = new ObjectMapper().writeValueAsString(updatedAlert);

        mockMvc.perform(MockMvcRequestBuilders.put("/alerts/"+savedAlert.getId())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAlertJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Alert upated successfully"));

        Alert updatedAlertFromDB = alertRepository.findById(savedAlert.getId()).get();
        assertThat(updatedAlertFromDB.getResource()).isEqualTo("resource");
        assertThat(updatedAlertFromDB.getSolved()).isTrue();
        alertRepository.delete(updatedAlertFromDB);
    }

    // Test the deletion of an alert from the database, and then check if it was deleted correctly.
    @Test
    public void deleteAlertTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        Alert alertToDB = alertService.fromDTO(alert);
        Alert savedAlert = alertRepository.save(alertToDB);

        mockMvc.perform(MockMvcRequestBuilders.delete("/alerts/" + savedAlert.getId())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Alert deleted successfully"));

        Optional<Alert> deletedAlert = alertRepository.findById(savedAlert.getId());
        assertThat(deletedAlert).isEmpty();
    }

    // Test the ignoring of an alert in the database, and then check if it was ignored correctly.
    @Test
    public void ignoreAlertTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        Alert alertToDB = alertService.fromDTO(alert);
        Alert savedAlert = alertRepository.save(alertToDB);

        mockMvc.perform(MockMvcRequestBuilders.post("/alerts/" + savedAlert.getId() + "/ignore")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Alert ignored successfully."));

        Alert ignoredAlert = alertRepository.findById(savedAlert.getId()).get();
        assertThat(ignoredAlert.getSolved()).isFalse();
        alertRepository.delete(ignoredAlert);
    }

    // Test the acceptance of an alert in the database, and then check if it was accepted correctly.
    @Test
    public void acceptAlertTest() throws Exception {
        AlertDTO alert = new AlertDTO((short) 1, (short) 16, (short) 1, "resource", false, false);
        Alert alertToDB = alertService.fromDTO(alert);
        Alert savedAlert = alertRepository.save(alertToDB);

        mockMvc.perform(MockMvcRequestBuilders.post("/alerts/" + savedAlert.getId() + "/accept")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("Alert of type")));

        Alert acceptedAlert = alertRepository.findById(savedAlert.getId()).get();
        assertThat(acceptedAlert.getSolved()).isTrue();
        alertRepository.delete(acceptedAlert);
    }




}