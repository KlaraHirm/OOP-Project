package server.api.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class SimpMessagingTemplateMock implements SimpMessageSendingOperations {

    /**
     * Create a new {@link SimpMessagingTemplate} instance.
     *
     */
    public SimpMessagingTemplateMock() {
        super();
    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void send(Message<?> message) throws MessagingException {

    }

    @Override
    public void send(String destination, Message<?> message) throws MessagingException {

    }

    @Override
    public void convertAndSend(Object payload) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload, Map<String, Object> headers) throws MessagingException {

    }

    @Override
    public void convertAndSend(Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException{

    }
}
