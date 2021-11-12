package de.Initium.AntiPortal.Main;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.Initium.AntiPortal.Main.Dispatcher.DataDispatcher;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnector_SWarps {

    public static Connection connection;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(DataDispatcher.HostS);
        dataSource.setPort(DataDispatcher.PortS);
        dataSource.setDatabaseName(DataDispatcher.DataBaseS);
        dataSource.setUser(DataDispatcher.UserNameS);
        dataSource.setPassword(DataDispatcher.PasswordS);

        connection = dataSource.getConnection();
    }
}
