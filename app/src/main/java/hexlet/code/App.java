package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;
import hexlet.code.dto.root.RootPage;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
        log.info("Application started on port {}", getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static String getDatabaseUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        assert inputStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        hikariConfig.setMaximumPoolSize(5);

        var dataSource = new HikariDataSource(hikariConfig);

        var sql = readResourceFile("schema.sql");
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> config.fileRenderer(new JavalinJte(createTemplateEngine())));

        app.exception(Exception.class, (e, ctx) -> {
            log.info(e.getMessage());

            if (e.getMessage().equals("Некорректный URL")) {
                ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            }

            var page = new RootPage();
            page.setFlash(e.getMessage());
            page.setFlashType("danger");
            ctx.render("index.jte", Map.of("page", page));
        });

        app.get("/", RootController::index);
        app.get("/urls", UrlsController::index);
        app.get("/urls/{id}", UrlsController::show);
        app.post("/urls", UrlsController::create);
        app.post("/urls/{id}/checks", ChecksController::create);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        var resolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(resolver, ContentType.Html);
    }
}
