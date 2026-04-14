package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static String normalizeUrl(String input) throws URISyntaxException, MalformedURLException {
        var uri = new URI(input);
        var url = uri.toURL();

        var protocol = url.getProtocol().toLowerCase();
        var host = url.getHost().toLowerCase();
        var port = url.getPort();

        return port == -1
                ? String.format("%s://%s", protocol, host)
                : String.format("%s://%s:%d", protocol, host, port);
    }
}
