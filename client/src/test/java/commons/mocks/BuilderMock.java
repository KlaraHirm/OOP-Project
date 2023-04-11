package commons.mocks;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;

import java.util.Locale;

public class BuilderMock implements Invocation.Builder {

    public MediaType[] accept;

    public String method;
    private Entity<?> entity;

    @Override
    public Invocation build(String method) {
        return null;
    }

    @Override
    public Invocation build(String method, Entity<?> entity) {
        return null;
    }

    @Override
    public Invocation buildGet() {
        return null;
    }

    @Override
    public Invocation buildDelete() {
        return null;
    }

    @Override
    public Invocation buildPost(Entity<?> entity) {
        return null;
    }

    @Override
    public Invocation buildPut(Entity<?> entity) {
        return null;
    }

    @Override
    public AsyncInvoker async() {
        return null;
    }

    @Override
    public Invocation.Builder accept(String... mediaTypes) {
        return this;
    }

    @Override
    public Invocation.Builder accept(MediaType... mediaTypes) {
        this.accept = mediaTypes;
        return this;
    }

    @Override
    public Invocation.Builder acceptLanguage(Locale... locales) {
        return null;
    }

    @Override
    public Invocation.Builder acceptLanguage(String... locales) {
        return null;
    }

    @Override
    public Invocation.Builder acceptEncoding(String... encodings) {
        return null;
    }

    @Override
    public Invocation.Builder cookie(Cookie cookie) {
        return null;
    }

    @Override
    public Invocation.Builder cookie(String name, String value) {
        return null;
    }

    @Override
    public Invocation.Builder cacheControl(CacheControl cacheControl) {
        return null;
    }

    @Override
    public Invocation.Builder header(String name, Object value) {
        return null;
    }

    @Override
    public Invocation.Builder headers(MultivaluedMap<String, Object> headers) {
        return null;
    }

    @Override
    public Invocation.Builder property(String name, Object value) {
        return null;
    }

    @Override
    public CompletionStageRxInvoker rx() {
        return null;
    }

    @Override
    public <T extends RxInvoker> T rx(Class<T> clazz) {
        return null;
    }

    @Override
    public Response get() {
        method = "GET";
        return Response.ok().build();
    }

    @Override
    public <T> T get(Class<T> responseType) {
        method = "GET";
        return null;
    }

    @Override
    public <T> T get(GenericType<T> responseType) {
        method = "GET";
        return null;
    }

    @Override
    public Response put(Entity<?> entity) {
        method = "PUT";
        this.entity = entity;
        return Response.ok().build();
    }

    @Override
    public <T> T put(Entity<?> entity, Class<T> responseType) {
        method = "PUT";
        return null;
    }

    @Override
    public <T> T put(Entity<?> entity, GenericType<T> responseType) {
        method = "PUT";
        return null;
    }

    @Override
    public Response post(Entity<?> entity) {
        method = "POST";
        return Response.ok().build();
    }

    @Override
    public <T> T post(Entity<?> entity, Class<T> responseType) {
        method = "POST";
        return null;
    }

    @Override
    public <T> T post(Entity<?> entity, GenericType<T> responseType) {
        method = "POST";
        return null;
    }

    @Override
    public Response delete() {
        method = "DELETE";
        return Response.ok().build();
    }

    @Override
    public <T> T delete(Class<T> responseType) {
        method = "DELETE";
        return null;
    }

    @Override
    public <T> T delete(GenericType<T> responseType) {
        method = "DELETE";
        return null;
    }

    @Override
    public Response head() {
        return null;
    }

    @Override
    public Response options() {
        return null;
    }

    @Override
    public <T> T options(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T options(GenericType<T> responseType) {
        return null;
    }

    @Override
    public Response trace() {
        return null;
    }

    @Override
    public <T> T trace(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T trace(GenericType<T> responseType) {
        return null;
    }

    @Override
    public Response method(String name) {
        return null;
    }

    @Override
    public <T> T method(String name, Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T method(String name, GenericType<T> responseType) {
        return null;
    }

    @Override
    public Response method(String name, Entity<?> entity) {
        return null;
    }

    @Override
    public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T method(String name, Entity<?> entity, GenericType<T> responseType) {
        return null;
    }
}
