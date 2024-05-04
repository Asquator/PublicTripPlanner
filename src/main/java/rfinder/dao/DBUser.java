package rfinder.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUser {
    protected Connection connection = DBManager.newConnection();

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
