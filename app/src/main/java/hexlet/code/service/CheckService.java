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
import java.util.Optional;

@Slf4j
public final class CheckService {

    private CheckService() {
    }

    public static void checkUrl(Long urlId) throws SQLException {
        log.info("Checking URL: id={}", urlId);
        var url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse(
                        String.format("Entity with id = %d not found", urlId)
                ));
        var response = Unirest.get(url.getName()).asString();
        var statusCode = response.getStatus();

        if (statusCode < HttpStatus.OK.getCode() || statusCode >= HttpStatus.MULTIPLE_CHOICES.getCode()) {
                throw new NotFoundResponse("Invalid status code: %d".formatted(statusCode));
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
        var check = new Check(statusCode, title, h1, description, urlId);
        CheckRepository.save(check);
        log.info("Check saved: urlId={}, status={}", urlId, statusCode);
    }
}
