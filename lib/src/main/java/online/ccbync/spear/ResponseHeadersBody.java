package online.ccbync.spear;

interface ResponseHeadersBody extends ResponseBody {
    ResponseHeadersBody headers(String header, String value);
    ResponseHeadersBody cookies(String cookies, String value);
}
