# Details

## Handling dynamic params

1. in the router take a path like "/api/users/{id:long}/profile" and tokenize it into: 
```java
var result = new Pair<>("/api/users/?/profile", List.of(Pattern.compile("^\d+$")))
```
2. In the Edge class we add a type and pattern fields
3. In the radix tree we add a new method 
```java 
addWithPatterns(path, patterns)
```
It behaves exactly like the old one but when it encounters ? it assigns the pattern to the edge
4. The lookup will no be changed for the most part the only thing we add is if we don't find no static route we try to find a dynamic one
5. Never combine ? edges in add
6. Move the suffixHead the right amount using the matched part length
7. To find the part that matches of a route, use lookingAt
