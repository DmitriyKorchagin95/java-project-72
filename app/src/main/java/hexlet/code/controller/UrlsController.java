package hexlet.code.controller;

import hexlet.code.dto.root.urls.UrlPage;
import hexlet.code.dto.root.urls.UrlsPage;
import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.service.UrlService;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class UrlsController {

    private UrlsController() {
    }

    public static void index(Context ctx) throws SQLException {
        log.debug("Handling GET /urls");

        var urls = UrlRepository.getEntities();
        var latestChecks = CheckRepository.getLatestEntities();
        Map<Long, Check> latestUrlChecksByUrlId = new HashMap<>();

        for (var check : latestChecks) {
            latestUrlChecksByUrlId.put(check.getUrlId(), check);
        }

        var page = new UrlsPage(urls, latestUrlChecksByUrlId);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", Map.of("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        log.debug("Handling GET /urls/{}", id);

        var url = UrlRepository.find(id)
                .orElseThrow(() -> {
                    log.warn("URL not found: id={}", id);
                    return new NotFoundResponse(String.format("Entity with id %d not found", id));
                });

        var checks = CheckRepository.getEntitiesByUrlId(id);
        var page = new UrlPage(url, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", Map.of("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var rawUrl = ctx.formParam("url");
        log.info("Handling POST /urls, input={}", rawUrl);

        try {
            var normalizedUrl = UrlUtils.normalizeUrl(rawUrl);
            var created = UrlService.create(normalizedUrl);

            if (created) {
                setFlash(ctx, "Страница успешно добавлена", "success");
                log.info("URL created: {}", normalizedUrl);
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                setFlash(ctx, "Страница уже существует", "danger");
                log.warn("Duplicate URL attempt: {}", normalizedUrl);
                ctx.redirect(NamedRoutes.mainPath());
            }

        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            log.warn("Invalid URL: {}", rawUrl, e);
            setFlash(ctx, "Некорректный URL", "danger");
            ctx.redirect(NamedRoutes.mainPath());
        }
    }

    private static void setFlash(Context ctx, String message, String type) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flashType", type);
    }
}
