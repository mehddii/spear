package online.ccbync.spear;

import io.netty.handler.codec.http.HttpStatusClass;

interface ResponseStatusBody extends ResponseBody {
    ResponseHeadersBody status(HttpStatusClass status);
}
