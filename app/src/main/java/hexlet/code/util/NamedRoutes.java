package hexlet.code.util;

public final class NamedRoutes {

    private NamedRoutes() {
    }

    public static String mainPath() {
        return String.valueOf("/");
    }

    public static String urlsPath() {
        return String.valueOf("/urls");
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String checksPath(Long id) {
        return checksPath(String.valueOf(id));
    }

    public static String checksPath(String id) {
        return "/urls/" + id + "/checks";
    }
}
