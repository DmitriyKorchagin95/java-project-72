package hexlet.code.service;

import hexlet.code.dto.root.urls.UrlPage;
import hexlet.code.dto.root.urls.UrlsPage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.UrlUtils;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class UrlService {

    private UrlService() {
    }

    public static UrlsPage getUrlsPage() throws SQLException {
        var urls = UrlRepository.getEntities();
        var latestChecks = CheckRepository.getLatestEntities();
        Map<Long, Check> latestChecksByUrlId = new HashMap<>();

        for (var check : latestChecks) {
            latestChecksByUrlId.put(check.getUrlId(), check);
        }

        return new UrlsPage(urls, latestChecksByUrlId);
    }

    public static UrlPage getUrlPage(Long id) throws SQLException {
        var url = UrlRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("URL not found: id={}", id);
                    return new NotFoundResponse();
                });
        var checks = CheckRepository.getEntitiesByUrlId(id);
        return new UrlPage(url, checks);
    }

    public static Url createUrl(String rawUrl) throws SQLException, MalformedURLException, URISyntaxException {
        var normalizedUrl = UrlUtils.normalizeUrl(rawUrl);
        var url = new Url(normalizedUrl);
        return UrlRepository.save(url);
    }
}
