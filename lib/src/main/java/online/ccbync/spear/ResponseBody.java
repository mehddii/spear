package online.ccbync.spear;

interface ResponseBody {
    void text(String text);
    void json(String json);
    void redirect(String path);
}
