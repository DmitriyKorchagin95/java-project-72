package hexlet.code.service;

import hexlet.code.dto.root.urls.UrlPage;
import hexlet.code.dto.root.urls.UrlsPage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtils;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

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
                    return new NotFoundResponse(
                            String.format("Entity with id %d not found", id)
                    );
                });
        var checks = CheckRepository.getEntitiesByUrlId(id);
        return new UrlPage(url, checks);
    }

    public static Map<String, String> createUrl(String rawUrl) throws SQLException {
        String normalizedUrl;

        try {
            normalizedUrl = UrlUtils.normalizeUrl(rawUrl);
        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "message", "Некорректный URL"
            );
        }

        var existing = UrlRepository.findByName(normalizedUrl);

        if (existing.isPresent()) {
            log.warn("Duplicate URL attempt: {}", normalizedUrl);

            return Map.of(
                    "status", "ok",
                    "message", "Страница уже существует",
                    "type", "danger",
                    "redirect", NamedRoutes.urlPath(existing.get().getId())
            );
        }

        var url = new Url(normalizedUrl);

        try {
            var saved = UrlRepository.save(url);

            log.info("URL saved: id={}, name={}", saved.getId(), saved.getName());

            return Map.of(
                    "status", "ok",
                    "message", "Страница успешно добавлена",
                    "type", "success",
                    "redirect", NamedRoutes.urlPath(saved.getId())
            );

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                log.warn("Duplicate URL (race): {}", normalizedUrl);

                var existingUrl = UrlRepository.findByName(normalizedUrl)
                        .orElseThrow(() -> new SQLException("URL exists but not found"));

                return Map.of(
                        "status", "ok",
                        "message", "Страница уже существует",
                        "type", "danger",
                        "redirect", NamedRoutes.urlPath(existingUrl.getId())
                );
            }

            log.error("DB error while saving URL: {}", normalizedUrl, e);
            throw e;
        }
    }
}
