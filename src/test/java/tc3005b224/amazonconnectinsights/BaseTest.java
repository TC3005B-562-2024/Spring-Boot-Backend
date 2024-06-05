package tc3005b224.amazonconnectinsights;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    protected String obtainAuthToken() throws IOException {
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthRequest {
        private String email;
        private String password;
        private boolean returnSecureToken;
    }

}
