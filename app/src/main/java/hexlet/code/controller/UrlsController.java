package hexlet.code.controller;

import hexlet.code.dto.root.RootPage;
import hexlet.code.service.UrlService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    public static void create(Context ctx) throws MalformedURLException, SQLException, URISyntaxException {
        var rawUrl = ctx.formParam("url");
        log.info("Handling POST /urls, input={}", rawUrl);

        try {
            var url = UrlService.createUrl(rawUrl);
            log.info("URL created: id={}, url={}", url.getId(), url.getName());
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect(NamedRoutes.urlPath(url.getId()));
        } catch (MalformedURLException | URISyntaxException e) {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            var page = new RootPage();
            page.setFlash("Некорректный URL");
            page.setFlashType("danger");
            ctx.render("index.jte", Map.of("page", page));
        } catch (SQLException e) {
            var url = UrlService.urlExistByName(rawUrl).orElseThrow();
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.urlPath(url.getId()));
        }
    }
}
