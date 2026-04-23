package hexlet.code.controller;

import hexlet.code.dto.root.RootPage;
import hexlet.code.dto.root.urls.UrlPage;
import hexlet.code.dto.root.urls.UrlsPage;
import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.service.UrlService;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

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

        var url = UrlRepository.findById(id)
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
        String normalizedUrl;

        try {
            normalizedUrl = UrlUtils.normalizeUrl(rawUrl);
        } catch (Exception e) {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            var page = new RootPage();
            page.setFlash("Некорректный URL");
            page.setFlashType("danger");
            ctx.render("index.jte", Map.of("page", page));
            return;
        }

        var existing = UrlRepository.findByName(normalizedUrl);

        if (existing.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "danger");
            log.warn("Duplicate URL attempt: {}", normalizedUrl);
            ctx.redirect(NamedRoutes.urlPath(existing.get().getId()));
            return;
        }

        var url = UrlService.create(normalizedUrl);
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flashType", "success");
        log.info("URL created: id={}, url={}", url.getId(), normalizedUrl);
        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }
}
