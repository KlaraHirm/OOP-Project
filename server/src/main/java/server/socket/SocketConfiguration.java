package server.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfiguration implements WebSocketMessageBrokerConfigurer {
    /**
     * sets up prefixes for the client-server and server-client messages
     * @param configuration
     */
    public void configureMessageBroker(MessageBrokerRegistry configuration) {
        configuration.enableSimpleBroker("/topic/");
        configuration.setApplicationDestinationPrefixes("/app/");
    }

    /**
     * sets up endpoint for the server connection
     * @param registry
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/hello");
    }
}
