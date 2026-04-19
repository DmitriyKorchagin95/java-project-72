package hexlet.code.repository;

import hexlet.code.model.Check;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CheckRepository extends BaseRepository {

    public static void save(Check check) throws SQLException {
        var sql = "INSERT INTO checks (status_code, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, check.getTitle());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getDescription());
            stmt.setLong(5, check.getUrlId());
            stmt.setTimestamp(6, check.getCreatedAt());

            stmt.executeUpdate();

            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    check.setId(keys.getLong(1));
                } else {
                    throw new SQLException("Database did not return an ID after saving the entity");
                }
            }

        } catch (SQLException e) {
            log.error("Error saving check for urlId={}", check.getUrlId(), e);
            throw e;
        }
    }

    public static List<Check> getEntitiesByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT id, status_code, title, h1, description, url_id, created_at "
                + "FROM checks WHERE url_id = ? ORDER BY created_at DESC";

        var result = new ArrayList<Check>();

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, urlId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Error fetching checks for urlId={}", urlId, e);
            throw e;
        }

        return result;
    }

    public static List<Check> getLatestEntities() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) id, status_code, title, h1, description, url_id, created_at "
                + "FROM checks ORDER BY url_id, created_at DESC";

        var result = new ArrayList<Check>();

        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql);
                var rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                result.add(map(rs));
            }

        } catch (SQLException e) {
            log.error("Error fetching latest checks", e);
            throw e;
        }

        return result;
    }

    private static Check map(java.sql.ResultSet rs) throws SQLException {
        var id = rs.getLong("id");
        var statusCode = rs.getInt("status_code");
        var title = rs.getString("title");
        var h1 = rs.getString("h1");
        var description = rs.getString("description");
        var urlId = rs.getLong("url_id");
        var createdAt = rs.getTimestamp("created_at");
        var check = new Check(statusCode, title, h1, description, urlId, createdAt);
        check.setId(id);
        return check;
    }
}
