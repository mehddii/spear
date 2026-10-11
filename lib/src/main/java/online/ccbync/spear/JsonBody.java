package online.ccbync.spear;

interface JsonBody {
    <T> T parse(Class<T> clazz);
}
