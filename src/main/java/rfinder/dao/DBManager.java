package rfinder.dao;

import org.postgresql.util.PGobject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBManager {

    public static Connection newConnection() {

        Properties dbProp = new Properties();
        Connection conn = null;

        try {
            InputStream is = DBManager.class.getResourceAsStream("db.properties");
            dbProp.load(is);
            String url = dbProp.getProperty("db.url");
            String user = dbProp.getProperty("db.user");
            String password = dbProp.getProperty("db.password");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");

            ((org.postgresql.PGConnection)conn).addDataType("geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));
            ((org.postgresql.PGConnection)conn).addDataType("box3d", (Class<? extends PGobject>) Class.forName("org.postgis.PGbox3d"));

        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
