package tc3005b224.amazonconnectinsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@SpringBootApplication
@OpenAPIDefinition(servers = {
		@Server(url = "http://localhost:8080", description = "Development server"),
		@Server(url = "https://back-p27ymwll2a-uc.a.run.app/", description = "Google Cloud Run server")

})
@EnableScheduling
public class AmazonConnectInsightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmazonConnectInsightsApplication.class, args);
	}

	@Configuration
	public class OpenAPI30Configuration {
		@Bean
		public OpenAPI customizeOpenAPI() {
			final String securitySchemeName = "bearerAuth";
			return new OpenAPI()
					.addSecurityItem(new SecurityRequirement()
							.addList(securitySchemeName))
					.components(new Components()
							.addSecuritySchemes(securitySchemeName, new SecurityScheme()
									.name(securitySchemeName)
									.type(SecurityScheme.Type.HTTP)
									.scheme("bearer")
									.bearerFormat("JWT")));
		}
	}
}
