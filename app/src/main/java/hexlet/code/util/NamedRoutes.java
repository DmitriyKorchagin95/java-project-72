package hexlet.code.util;

public final class NamedRoutes {
    static final String MAIN_PATH = "/";
    static final String URLS_PATH = "/urls";

    private NamedRoutes() {
    }

    public static String rootPath() {
        return MAIN_PATH;
    }

    public static String urlsPath() {
        return URLS_PATH;
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return String.format("/urls/%s", id);
    }

    public static String checksPath(Long id) {
        return checksPath(String.valueOf(id));
    }

    public static String checksPath(String id) {
        return String.format("/urls/%s/checks", id);
    }
}
