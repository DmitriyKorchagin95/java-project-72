package hexlet.code.service;

import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.StringUtils;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public final class CheckService {

    private CheckService() {
    }

    public static Map<String, String> checkUrl(Long urlId) throws SQLException {
        log.info("Checking URL: id={}", urlId);

        var url = UrlRepository.findById(urlId)
                .orElseThrow(() -> {
                    log.warn("URL not found: id={}", urlId);
                    return new NotFoundResponse(
                            String.format("Entity with id = %d not found", urlId)
                    );
                });

        try {
            var response = Unirest.get(url.getName()).asString();
            var statusCode = response.getStatus();

            if (statusCode < HttpStatus.OK.getCode() || statusCode >= HttpStatus.MULTIPLE_CHOICES.getCode()) {
                log.warn("Non-success status code: {}", statusCode);
                return Map.of(
                        "message", "Произошла ошибка при проверке",
                        "type", "danger"
                );
            }

            var body = response.getBody();
            var doc = Jsoup.parse(body != null ? body : "");
            var title = StringUtils.truncate(doc.title(), 200);
            var h1 = Optional.ofNullable(doc.selectFirst("h1"))
                    .map(Element::text)
                    .map(text -> StringUtils.truncate(text, 200))
                    .orElse(null);
            var description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                    .map(tag -> tag.attr("content"))
                    .map(content -> StringUtils.truncate(content, 200))
                    .orElse(null);

            var check = new Check(
                    statusCode,
                    title,
                    h1,
                    description,
                    urlId
            );

            CheckRepository.save(check);
            log.info("Check saved: urlId={}, status={}", urlId, statusCode);

            return Map.of(
                    "message", "Страница успешно проверена",
                    "type", "success"
            );
        } catch (Exception e) {
            log.warn("Failed to check URL: {}", url.getName(), e);

            return Map.of(
                    "message", "Произошла ошибка при проверке",
                    "type", "danger"
            );
        }
    }
}
