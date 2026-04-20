package hexlet.code.repository;

import hexlet.code.model.Url;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UrlRepository extends BaseRepository {

    public static Url save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name) VALUES (?)";

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();

            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    url.setId(keys.getLong(1));
                    return url;
                } else {
                    throw new SQLException("Database didn't return an ID after saving the entity");
                }
            }

        } catch (SQLException e) {
            log.error("Error saving URL: {}", url.getName(), e);
            throw e;
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls ORDER BY created_at DESC";
        var result = new ArrayList<Url>();

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql);
                var rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                result.add(map(rs));
            }

        } catch (SQLException e) {
            log.error("Error fetching URLs", e);
            throw e;
        }

        return result;
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls WHERE id = ?";

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Error fetching URL by id={}", id, e);
            throw e;
        }

        return Optional.empty();
    }

    private static Url map(java.sql.ResultSet rs) throws SQLException {
        var id = rs.getLong("id");
        var name = rs.getString("name");
        var createdAt = rs.getTimestamp("created_at");
        var url = new Url(name, createdAt);
        url.setId(id);
        return url;
    }
}
