package hexlet.code.controller;

import hexlet.code.service.UrlService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;

@Slf4j
public final class UrlsController {

    private UrlsController() {
    }

    public static void index(Context ctx) throws SQLException {
        log.debug("Handling GET /urls");
        var page = UrlService.getUrlsPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", Map.of("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        try {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            log.debug("Handling GET /urls/{}", id);
            var page = UrlService.getUrlPage(id);
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flashType"));
            ctx.render("urls/show.jte", Map.of("page", page));
        } catch (SQLException e) {
            throw new SQLException("Страница не найдена", e);
        }
    }

    public static void create(Context ctx) throws Exception {
        try {
            var rawUrl = ctx.formParam("url");
            log.info("Handling POST /urls, input={}", rawUrl);
            var url = UrlService.createUrl(rawUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
            log.info("URL created: id={}, url={}", url.getId(), url.getName());
            ctx.redirect(NamedRoutes.urlPath(url.getId()));
        } catch (SQLException e) {
            throw new SQLException("Страница уже существует", e);
        } catch (Exception e) {
            throw new Exception("Некорректный URL", e);
        }
    }
}
