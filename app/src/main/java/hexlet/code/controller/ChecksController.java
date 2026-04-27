package hexlet.code.controller;

import hexlet.code.service.CheckService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public final class ChecksController {

    private ChecksController() {
    }

    public static void create(Context ctx) throws SQLException {
        try {
            var urlId = ctx.pathParamAsClass("id", Long.class).get();
            log.info("POST /urls/{}/checks", urlId);
            CheckService.checkUrl(urlId);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (SQLException e) {
            ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.urlPath(
                    ctx.pathParamAsClass("id", Long.class).get())
            );
        } catch (UnirestException e) {
            throw new UnirestException("Ошибка соединения во время проверки", e);
        }
    }
}
