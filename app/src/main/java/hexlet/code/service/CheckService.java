package hexlet.code.service;

import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import hexlet.code.util.StringUtils;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public final class CheckService {

    private CheckService() {
    }

    public static boolean checkUrl(Long urlId, String urlName) throws SQLException {
        log.info("Checking URL: id={}, url={}", urlId, urlName);

        try {
            var response = Unirest.get(urlName).asString();
            var statusCode = response.getStatus();

            if (statusCode < 200 || statusCode >= 300) {
                log.warn("Non-success status code: {}", statusCode);
                return false;
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
            return true;
        } catch (Exception e) {
            log.warn("Failed to check URL: {}", urlName, e);
            return false;
        }
    }
}
