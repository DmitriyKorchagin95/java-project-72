package hexlet.code.controller;

import hexlet.code.dto.root.RootPage;
import hexlet.code.service.UrlService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
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
        var id = ctx.pathParamAsClass("id", Long.class).get();
        log.debug("Handling GET /urls/{}", id);
        var page = UrlService.getUrlPage(id);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", Map.of("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var rawUrl = ctx.formParam("url");
        log.info("Handling POST /urls, input={}", rawUrl);
        var result = UrlService.createUrl(rawUrl);

        if (result.get("status").equals("error")) {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            var page = new RootPage();
            page.setFlash(result.get("message"));
            page.setFlashType("danger");
            ctx.render("index.jte", Map.of("page", page));
            return;
        }

        ctx.sessionAttribute("flash", result.get("message"));
        ctx.sessionAttribute("flashType", result.get("type"));
        ctx.redirect(result.get("redirect"));
    }
}
