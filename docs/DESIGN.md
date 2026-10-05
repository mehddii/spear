```java
// 1. The low level primitives:
// - fast
// - bad dx
// - boilerplate
// We need a Router, Context & a Listener structures
// The best patterns for these structures are:
// * Router should use the chaining design pattern
// * Listener should use the builder pattern
// * Context contains a Request object and a bunch of methods to easily build the response
// for example text(), json(), setHeader(), status()...
// The request should easily provide params from the route
// The SAM interface should be Function<Context, void>
// Lets build a products api with this structure
public record Product(Long id, String name, String description) {

}

List<Product> products = List.of(
    new Product(1, "Phone", "Phone"),
    new Product(2, "IPad", "IPad"),
    new Product(3, "Computer", "Computer"),
)
// Handles status automatically but you can specify what to return
// 200 = Http.OK support for both numbers and codes for convenience
var productsRouter = new Router("/api/v1/products")
    .get("/", ctx -> ctx.status(200).json(products));
    .get("/{id:long}", ctx -> {
        // auto parsing id is long
        var id = ctx.params("id");
        var product = products().
            stream().
            filter(p -> p.id == id)
        ctx.json(product.get(0));
    })
    .post("/", ctx -> {
        var product = ctx.body().parse(Product.class);
        // add product to the db
        ctx.json(product);
    })
    .delete("/{id:long}", ctx -> {
        var id = ctx.params("id");
        // delete from db
        ctx.status(Http.NoContent);
    })
    .put("/{id:long}", ctx -> {
        var id = ctx.params("id");
        var product = ctx.body().parse(Product.class)
        // find product with that id and update
        ctx.json(product);
    })
// Now if we want to scale this, if we have a users, cart and locations ressources.
// We could add a method handle(route, Class<Controller>) where Controller is a interface with a route method
// and the user can now create controllers and bind them for example
// public class UserController implements Controller {
//      public void get(Context ctx) {
//      }
//      ...
//      public Router route(String route) {
//          return new Router()
//              .get(route + "...", this::get)
//               ...;
//      }
// }
//
// and somewhere in main
// we have apiRouter.handle("/users", UserController.class)
// This makes more sence and way scalable but it would be better
// if could use annotations for example instead of the router you just
// directly annotate a method with @Get("")...
// Thats way better the only downside is performance if we use the easy route
// of checking and invoking methods on the runtime
// the hard and exciting route is to generate the low level code in compile time
// using java annotation processing.

SpearListener.builder()
    .port(8080)
    .mount(router)
    .build()
    .run()
```
