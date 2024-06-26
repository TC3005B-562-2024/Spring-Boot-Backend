package tc3005b224.amazonconnectinsights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InstanceControllerTests {
    @Autowired
    MockMvc mockMvc;

    private String obtainAuthToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new AlertTests.AuthRequest("test@g.com", "123456", true));

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

    @Test
    @DisplayName("Test to get instance details")
    public void getInstanceDetailsTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/instance")
                    .header("Authorization", "Bearer " + obtainAuthToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print()
        );
    }

    @Test
    @DisplayName("Test to get instance details with invalid token")
    public void tokenExceptionGetInstanceDetailsTest()throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/instance")
                    .header("Authorization", "Bearer " + "invalidToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print()
        );
    }
}

