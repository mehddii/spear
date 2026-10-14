package online.ccbync.spear;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

// TODO:
//  - Handle dynamic params
//  - Implement context api
//  - Add a method to merge prefix trees
public class Router {
    private final PrefixTree<Map<HttpMethod, Handler>> routes = new RadixTree<>();
    private static final Pattern dynamicParam = Pattern.compile("^{\\w+:\\w+}$");
    private static final Map<String, Class<?>> TYPES = Map.of(
        "int", Integer.class,
        "long", Long.class,
        "uuid", UUID.class,
        "UUID", UUID.class,
        "String", String.class,
        "string", String.class
    );
    private static final Map<Class<?>, Pattern> PATTERNS = Map.of(
        Integer.class, Pattern.compile("\\d+"),
        Long.class, Pattern.compile("\\d+"),
        UUID.class, Pattern.compile("[\\w-]+"),
        String.class, Pattern.compile("[^/]+")
    );
    private static final Map<Class<?>, Integer> PRIORITY = Map.of(
        Integer.class, 1,
        Long.class, 2,
        UUID.class, 3,
        String.class, 4
    );

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

    private Pair<String, List<Triplet<String, Pair<Class<?>, Integer>, Pattern>>> tokenize(String path) {
        StringBuilder sb = new StringBuilder();
        List<Triplet<String, Pair<Class<?>, Integer>, Pattern>> patterns = new ArrayList<>();

        for (var part: path.split("/")) {
            if (dynamicParam.matcher(part).matches()) {
                if (part.isEmpty()) {
                    continue;
                }

                var values = part.substring(1, part.length() - 1).split(":");
                var name = values[0];
                var type = values[1];

                var paramType = TYPES.getOrDefault(type, Void.class);
                var pattern = PATTERNS.getOrDefault(paramType, null);
                var order = PRIORITY.getOrDefault(paramType, 5);

                patterns.add(
                    new Triplet<String, Pair<Class<?>, Integer>, Pattern>(
                        name,
                        new Pair<>(paramType, order),
                        pattern
                    )
                );
                part = "?";
            }

            sb.append("/");
            sb.append(part);
        }

        return new Pair<String,List<Triplet<String, Pair<Class<?>, Integer>, Pattern>>>(
            sb.toString(),
            patterns
        );
    }

    public Router handle(HttpMethod method, String path, Handler handler) {
        var tokenizedPath = tokenize(path);

        var handlers = routes.lookup(tokenizedPath.key());
        if (handlers.isEmpty()) {
            handlers = Optional.of(new EnumMap<HttpMethod, Handler>(HttpMethod.class));

            if (tokenizedPath.value().isEmpty()) {
                routes.add(path, handlers.get());
            } else {
                routes.add(tokenizedPath.key(), handlers.get(), tokenizedPath.value());
            }
        }

        handlers.get().put(method, handler);
        return this;
    }

    public Router addSubroute(String path, Router router) {
        return this;
    }
}
