package de.Initium.AntiPortal.Main.Dispatcher;

import de.Initium.AntiPortal.Main.Main;

public class DataDispatcher {

    public static final String Host = Main.getConfiguration().getString("settings.MySqlData.Host");
    public static final String DataBase = Main.getConfiguration().getString("settings.MySqlData.DataBase");
    public static final String UserName = Main.getConfiguration().getString("settings.MySqlData.Username");
    public static final String Password = Main.getConfiguration().getString("settings.MySqlData.Password");
    public static final int Port = Main.getConfiguration().getInt("settings.MySqlData.Port");
}
