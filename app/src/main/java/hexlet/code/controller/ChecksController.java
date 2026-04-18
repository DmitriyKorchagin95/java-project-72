package hexlet.code.controller;

import hexlet.code.repository.UrlRepository;
import hexlet.code.service.CheckService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class ChecksController {

    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();

        log.info("POST /urls/{}/checks", urlId);

        var url = UrlRepository.find(urlId)
                .orElseThrow(() -> {
                    log.warn("URL not found: id={}", urlId);
                    return new NotFoundResponse(
                            String.format("Entity with id = %d not found", urlId)
                    );
                });

        var success = CheckService.checkUrl(urlId, url.getName());

        if (success) {
            setFlash(ctx, "Страница успешно проверена", "success");
        } else {
            setFlash(ctx, "Некорректный адрес", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }

    private static void setFlash(Context ctx, String message, String type) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flashType", type);
    }
}
