package tc3005b224.amazonconnectinsights;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.repository.CategoryRepository;
import tc3005b224.amazonconnectinsights.service.CategoryService;


@SpringBootTest
@AutoConfigureMockMvc
class CategoryTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;


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
    public void getAllCategoriesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void findCategoryByIdTest() throws Exception {
        CategoryDTO category = new CategoryDTO("denomination", "description", (short) 1, true);
        Category categoryToDB = categoryService.fromDTO(category);
        Category savedCategory = categoryRepository.save(categoryToDB);
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+savedCategory.getIdentifier())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
        categoryRepository.delete(savedCategory);
    }

    @Test
    public void findCategoryByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/0")
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        CategoryDTO category = new CategoryDTO("denomination", "description", (short) 1, true);
        Category categoryToDB = categoryService.fromDTO(category);
        Category savedCategory = categoryRepository.save(categoryToDB);
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+savedCategory.getIdentifier())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getIdentifier());
        assertThat(deletedCategory).isEmpty();
    }

    @Test
    public void updateCategoryTest() throws Exception {
        CategoryDTO category = new CategoryDTO("denomination", "description", (short) 1, true);
        Category categoryToDB = categoryService.fromDTO(category);
        Category savedCategory = categoryRepository.save(categoryToDB);
        CategoryDTO updatedCategory = new CategoryDTO("denomination", "description", (short) 1, false);
        String updatedCategoryJson = new ObjectMapper().writeValueAsString(updatedCategory);
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/"+savedCategory.getIdentifier())
                        .header("Authorization", "Bearer " + obtainAuthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCategoryJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        Optional<Category> updatedCategoryOptional = categoryRepository.findById(savedCategory.getIdentifier());
        assertThat(updatedCategoryOptional).isPresent();
        assertThat(updatedCategoryOptional.get().getActive()).isFalse();
        categoryRepository.delete(updatedCategoryOptional.get());
    }


}
