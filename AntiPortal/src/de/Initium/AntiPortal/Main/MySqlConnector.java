package de.Initium.AntiPortal.Main;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.Initium.AntiPortal.Main.Dispatcher.DataDispatcher;

public class MySqlConnector {

    public static Connection connection;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(DataDispatcher.Host);
        dataSource.setPort(DataDispatcher.Port);
        dataSource.setDatabaseName(DataDispatcher.DataBase);
        dataSource.setUser(DataDispatcher.UserName);
        dataSource.setPassword(DataDispatcher.Password);

        connection = dataSource.getConnection();
    }
}
