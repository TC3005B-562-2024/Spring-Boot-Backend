package tc3005b224.amazonconnectinsights.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configure message broker to handle messages with "/topic" prefix
        config.enableSimpleBroker("/topic"); 

        // Configure prefix for handling messages from clients
        config.setApplicationDestinationPrefixes("/app"); 
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register STOMP endpoint at "/ws" and enable SockJS fallback
        registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:3000")
        .withSockJS(); 
    }
}
