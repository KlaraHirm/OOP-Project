package server.api;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

public class CardPoller implements Runnable {

    private final long cardId;
    private final RestTemplate restTemplate;
    private final AtomicBoolean taskExists;

    public CardPoller(long cardId, AtomicBoolean taskExists) {
        this.cardId = cardId;
        this.restTemplate = new RestTemplate();
        this.taskExists = taskExists;
    }

    @Override
    public void run() {
        while (taskExists.get()) {
            boolean exists = Boolean.TRUE.equals(restTemplate.getForObject("/api/card/poll/{id}", Boolean.class, cardId));
            if (!exists) {
                taskExists.set(false);
                break;
            }
            try {
                Thread.sleep(5000); // wait for 5 seconds before polling again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

