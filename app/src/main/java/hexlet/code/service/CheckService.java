package hexlet.code.service;

import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.sql.Timestamp;
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
            var body = response.getBody();
            var doc = Jsoup.parse(body);
            var title = doc.title();
            var h1 = Optional.ofNullable(doc.selectFirst("h1"))
                    .map(Element::text)
                    .orElse(null);
            var description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                    .map(tag -> tag.attr("content"))
                    .orElse(null);

            var check = new Check(
                    statusCode,
                    title,
                    h1,
                    description,
                    urlId,
                    new Timestamp(System.currentTimeMillis())
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
