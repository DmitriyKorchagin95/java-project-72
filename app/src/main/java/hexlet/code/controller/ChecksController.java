package hexlet.code.controller;

import hexlet.code.service.CheckService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ChecksController {

    private ChecksController() {
    }

    public static void create(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        log.info("POST /urls/{}/checks", urlId);

        try {
            CheckService.checkUrl(urlId);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
        } catch (Exception e) {
            log.error("Check failed for url id={}", urlId, e);
            ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
            ctx.sessionAttribute("flashType", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
