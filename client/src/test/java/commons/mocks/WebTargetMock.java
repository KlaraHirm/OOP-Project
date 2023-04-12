package commons.mocks;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebTargetMock implements WebTarget {

    public String path;

    public List<String> queryParamKeys;
    public List<Object[]> queryParamValues;

    BuilderMock builderMock;

    public WebTargetMock(BuilderMock builderMock) {
        this.builderMock = builderMock;

        this.queryParamKeys = new ArrayList<>();
        this.queryParamValues = new ArrayList<>();
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public UriBuilder getUriBuilder() {
        return null;
    }

    @Override
    public WebTarget path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public WebTarget resolveTemplate(String name, Object value) {
        return null;
    }

    @Override
    public WebTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
        return null;
    }

    @Override
    public WebTarget resolveTemplateFromEncoded(String name, Object value) {
        return null;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> templateValues) {
        return null;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) {
        return null;
    }

    @Override
    public WebTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
        return null;
    }

    @Override
    public WebTarget matrixParam(String name, Object... values) {
        return null;
    }

    @Override
    public WebTarget queryParam(String name, Object... values) {
        this.queryParamKeys.add(name);
        this.queryParamValues.add(values);
        return this;
    }

    @Override
    public Invocation.Builder request() {
        return builderMock;
    }

    @Override
    public Invocation.Builder request(String... acceptedResponseTypes) {
        return builderMock;
    }

    @Override
    public Invocation.Builder request(MediaType... acceptedResponseTypes) {
        return builderMock;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public WebTarget property(String name, Object value) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, int priority) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, Class<?>... contracts) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        return null;
    }

    @Override
    public WebTarget register(Object component) {
        return null;
    }

    @Override
    public WebTarget register(Object component, int priority) {
        return null;
    }

    @Override
    public WebTarget register(Object component, Class<?>... contracts) {
        return null;
    }

    @Override
    public WebTarget register(Object component, Map<Class<?>, Integer> contracts) {
        return null;
    }
}
