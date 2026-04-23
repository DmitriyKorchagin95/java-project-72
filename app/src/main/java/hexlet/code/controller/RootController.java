package hexlet.code.controller;

import hexlet.code.dto.root.RootPage;
import io.javalin.http.Context;

import java.util.Map;

public final class RootController {

    private RootController() {
    }

    public static void index(Context ctx) {
        var page = new RootPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("index.jte", Map.of("page", page));
    }
}
