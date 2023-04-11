package commons.mocks;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.Map;

public class ClientMock implements Client {

    WebTargetMock webTargetMock;

    public String url;

    public ClientMock(WebTargetMock webTargetMock) {
        this.webTargetMock = webTargetMock;
    }

    @Override
    public WebTarget target(String url) {
        this.url = url;
        return webTargetMock;
    }

    @Override
    public WebTarget target(URI uri) {
        return webTargetMock;
    }

    @Override
    public WebTarget target(UriBuilder uriBuilder) {
        return webTargetMock;
    }

    @Override
    public WebTarget target(Link link) {
        return webTargetMock;
    }

    @Override
    public Invocation.Builder invocation(Link link) {
        return null;
    }

    @Override
    public SSLContext getSslContext() {
        return null;
    }

    @Override
    public HostnameVerifier getHostnameVerifier() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public Client property(String name, Object value) {
        return null;
    }

    @Override
    public Client register(Class<?> componentClass) {
        return null;
    }

    @Override
    public Client register(Class<?> componentClass, int priority) {
        return null;
    }

    @Override
    public Client register(Class<?> componentClass, Class<?>... contracts) {
        return null;
    }

    @Override
    public Client register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        return null;
    }

    @Override
    public Client register(Object component) {
        return null;
    }

    @Override
    public Client register(Object component, int priority) {
        return null;
    }

    @Override
    public Client register(Object component, Class<?>... contracts) {
        return null;
    }

    @Override
    public Client register(Object component, Map<Class<?>, Integer> contracts) {
        return null;
    }
}
