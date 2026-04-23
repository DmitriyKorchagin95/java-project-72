package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private Javalin app;
    private MockWebServer mockWebServer;
    private Url url;

    @BeforeEach
    void setUp() throws Exception {
        app = App.getApp();
        clearDatabase();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        url = new Url("https://www.example.com:8080");
    }

    private void clearDatabase() throws SQLException {
        try (var conn = BaseRepository.dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM checks");
            stmt.execute("DELETE FROM urls");
        }
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    void testUrlPage() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    void testNonExistingUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
    }

    @Test
    void testCreateUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url.getName() + "?foo=bar";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });

        assertThat(UrlRepository.getEntities()).hasSize(1);
    }

    @Test
    void testCreateDuplicateUrl() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=" + url.getName());

            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });

        assertThat(UrlRepository.getEntities()).hasSize(1);
    }

    @Test
    void testCreateInvalidUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=invalidUrl");

            assertThat(response.body().string()).contains("Некорректный URL");
            assertThat(response.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
        });

        assertThat(UrlRepository.getEntities()).isEmpty();
    }

    @Test
    void testCreateUrlCheck() throws SQLException, IOException {
        var filepath = Paths.get("src", "test", "resources", "test.html");
        var html = Files.readString(filepath);

        mockWebServer.enqueue(new MockResponse().setBody(html));
        url.setName(mockWebServer.url("/").toString());
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls/" + url.getId() + "/checks");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });

        assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        var checks = CheckRepository.getEntitiesByUrlId(url.getId());
        assertThat(checks).hasSize(1);
        var check = checks.getFirst();
        assertThat(check.getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(check.getTitle())
                .isEqualTo("Test HTML Page");
        assertThat(check.getH1())
                .isEqualTo("Welcome to Test HTML Page");
        assertThat(check.getDescription())
                .isEqualTo("This is a test HTML page.");
        assertThat(check.getCreatedAt()).isNotNull();
    }
}
