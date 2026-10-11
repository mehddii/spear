package online.ccbync.spear;

interface RequestContext {
    <T> T params(String param);
    String query(String param);
    String cookies(String cookie);
    JsonBody body();
    ResponseStatusBody response();
}
