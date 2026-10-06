package online.ccbync.spear;

import java.util.function.Function;
import java.lang.Void;

// TODO:
//  - Fast routing (start with radix tree)
//  - Implement Context with a clean api
public class Router {
    Router() {
    }

    Router(String toplevel) {
    }

    public Router get(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router post(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router put(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router patch(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router delete(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router query(String path, Function<Context, Void> handler) {
        return this;
    }

    public Router handle(HttpMethod method, String path, Function<Context, Void> handler) {
        return this;
    }

    public Router addSubroute(String path, Router router) {
        return this;
    }
}
