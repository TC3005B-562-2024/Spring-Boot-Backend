package tc3005b224.amazonconnectinsights.controllers;

import tc3005b224.amazonconnectinsights.dto.PostCreateDTO;
import tc3005b224.amazonconnectinsights.dto.PostDTO;
import tc3005b224.amazonconnectinsights.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the timeline", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Timeline not found", content = @Content),
    })
    @Operation(summary = "Get all posts for a user")
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getPosts() {
        List<PostDTO> result = new ArrayList<>();
        PostDTO post1 = new PostDTO();
        post1.setContent("Hello World");
        post1.setDate(new Date());
        UserDTO user1 = new UserDTO();
        user1.setName("John Doe");
        user1.setUsername("johndoe");
        user1.setVerified(true);
        user1.setProfilePicture("https://example.com/johndoe.jpg");
        post1.setUser(user1);
        result.add(post1);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<PostCreateDTO> createPost(@RequestBody PostCreateDTO post) {
        // Aqui haremos logica de negocio para guardar el post en la base de datos
        return ResponseEntity.ok(post);
    }
}
