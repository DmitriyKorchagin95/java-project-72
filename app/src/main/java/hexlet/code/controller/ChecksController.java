package hexlet.code.controller;

import hexlet.code.repository.UrlRepository;
import hexlet.code.service.CheckService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public final class ChecksController {

    private ChecksController() {
    }

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
            log.info("Check successful: urlId={}", urlId);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
        } else {
            log.warn("Check failed: urlId={}", urlId);
            ctx.sessionAttribute("flash", "Ошибка проверки");
            ctx.sessionAttribute("flashType", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
