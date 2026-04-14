package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    Javalin app;
    MockWebServer mockWebServer;
    Url url;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        url = new Url("https://www.example.com:8080", new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testNonExistingUrlPage() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url.getName() + "?foo=bar";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(url.getName());
        });

        assertThat(UrlRepository.existsByName(url.getName())).isTrue();
    }

    @Test
    public void testCreateDuplicateUrl() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url.getName();
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        assertThat(UrlRepository.getEntities()).hasSize(1);
    }

    @Test
    public void testCreateInvalidUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=invalidUrl";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        assertThat(UrlRepository.getEntities()).hasSize(0);
    }

    @AfterEach
    public final void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
