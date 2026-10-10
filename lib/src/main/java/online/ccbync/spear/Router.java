package online.ccbync.spear;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

// TODO:
//  - Implement Context with a clean api
//  - Add a method to merge prefix trees
public class Router {
    private final PrefixTree<Map<HttpMethod, Handler>> routes = new RadixTree<>();

    Router() {
    }

    Router(String toplevel) {
        routes.add(toplevel, null);
    }

    public Router get(String path, Handler handler) {
        return handle(HttpMethod.GET, path, handler);
    }

    public Router post(String path, Handler handler) {
        return handle(HttpMethod.POST, path, handler);
    }

    public Router put(String path, Handler handler) {
        return handle(HttpMethod.PUT, path, handler);
    }

    public Router patch(String path, Handler handler) {
        return handle(HttpMethod.PATCH, path, handler);
    }

    public Router delete(String path, Handler handler) {
        return handle(HttpMethod.DELETE, path, handler);
    }

    public Router query(String path, Handler handler) {
        return handle(HttpMethod.QUERY, path, handler);
    }

    public Router handle(HttpMethod method, String path, Handler handler) {
        var handlers = routes.lookup(path);
        if (handlers.isEmpty()) {
            handlers = Optional.of(new EnumMap<HttpMethod, Handler>(HttpMethod.class));
            routes.add(path, handlers.get());
        }

        handlers.get().put(method, handler);
        return this;
    }

    public Router addSubroute(String path, Router router) {
        return this;
    }
}
