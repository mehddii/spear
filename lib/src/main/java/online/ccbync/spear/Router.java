package online.ccbync.spear;

import java.util.*;

public class Router {
    private final Map<String, Object> handlers = new HashMap<>();
    private final Map<String, HttpMethod> methods = new HashMap<>();

    enum HttpMethod {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        QUERY,
    }

    public Router get(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.GET);
        return this;
    }

    public Router post(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.POST);
        return this;
    }

    public Router put(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.PUT);
        return this;
    }

    public Router patch(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.PATCH);
        return this;
    }

    public Router delete(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.DELETE);
        return this;
    }

    public Router query(String route, Object handler) {
        handlers.put(route, handler);
        methods.put(route, HttpMethod.QUERY);
        return this;
    }

    public Router handle(String method, String route, Object handler) {
        handlers.put(route, handler);
        return this;
    }

    // Catch all
    public Router handle(String route, Object handler) {
        handlers.put(route, handler);
        return this;
    }

    // Catch all for multiple routes
    public Router handle(List<String> routes, Object handler) {
        for (var route: routes) {
            handlers.put(route, handler);
        }
        return this;
    }

    public Router add(String path, Router router) {
        return this;
    }
}
