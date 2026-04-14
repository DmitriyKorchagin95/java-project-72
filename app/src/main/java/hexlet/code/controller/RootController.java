package hexlet.code.controller;

import hexlet.code.dto.root.MainPage;
import io.javalin.http.Context;

import java.util.Map;

public class RootController {
    public static void index(Context ctx) {
        var page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("index.jte", Map.of("page", page));
    }
}