package online.ccbync.spear;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpStatusClass;

public class Context implements RequestContext, ResponseStatusBody, ResponseHeadersBody {
    private final FullHttpRequest request;
    private FullHttpResponse response;

    public Context(FullHttpRequest request) {
        this.request = request;
    }

    public <T> T params(String param) {
        return null;
    }

    public String query(String param) {
        return null;
    }

    public String cookies(String cookie) {
        return null;
    }

    public JsonBody body() {
        return null;
    }

    public ResponseStatusBody response() {
        return null;
    }

    public ResponseHeadersBody headers(String header, String value) {
        return null;
    }

    public ResponseHeadersBody cookies(String cookies, String value) {
        return null;
    }


    public ResponseHeadersBody status(HttpStatusClass status) {
        return null;
    }

    public void text(String text) {

    }

    public void json(String json) {

    }

    public void redirect(String path) {

    }
}
