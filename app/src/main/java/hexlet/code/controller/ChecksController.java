package hexlet.code.controller;

import hexlet.code.service.CheckService;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public final class ChecksController {

    private ChecksController() {
    }

    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        log.info("POST /urls/{}/checks", urlId);
        var check = CheckService.checkUrl(urlId);
        ctx.sessionAttribute("flash", check.get("message"));
        ctx.sessionAttribute("flashType", check.get("type"));
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
