package hexlet.code.service;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Timestamp;

@Slf4j
public final class UrlService {

    private UrlService() {
    }

    public static Url create(String normalizedUrl) throws SQLException {
        var url = new Url(normalizedUrl, new Timestamp(System.currentTimeMillis()));
        log.debug("Saving URL: {}", normalizedUrl);

        try {
            var saved = UrlRepository.save(url);
            log.info("URL saved: id={}, name={}", saved.getId(), saved.getName());
            return saved;
        } catch (SQLException e) {

            if ("23505".equals(e.getSQLState())) {
                log.warn("Duplicate URL: {}", normalizedUrl);
                return null;
            }

            log.error("DB error while saving URL: {}", normalizedUrl, e);
            throw e;
        }
    }
}
