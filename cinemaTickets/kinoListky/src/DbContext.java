import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * 
 * @author Alexander Šimko
 */
public class DbContext { //prevzate zo vzoroveho projektu
    private static Connection connection;

    public static void setConnection(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("connection cannot be null");
        }

        DbContext.connection = connection;
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("connection must be set before calling this method");
        }

        return connection;
    }

    public static void clear() {
        connection = null;
    }
}

