package de.Initium.AntiPortal.Main.Dispatcher;

import de.Initium.AntiPortal.Main.Main;

public class DataDispatcher {

    //Portals
    public static final String Host = Main.getConfiguration().getString("settings.MySqlData.Host");
    public static final String DataBase = Main.getConfiguration().getString("settings.MySqlData.DataBase");
    public static final String UserName = Main.getConfiguration().getString("settings.MySqlData.Username");
    public static final String Password = Main.getConfiguration().getString("settings.MySqlData.Password");
    public static final int Port = Main.getConfiguration().getInt("settings.MySqlData.Port");

    //S-Warps
    public static final String HostS = Main.getConfiguration().getString("settings.MySqlData-SWarps.Host");
    public static final String DataBaseS = Main.getConfiguration().getString("settings.MySqlData-SWarps.DataBase");
    public static final String UserNameS = Main.getConfiguration().getString("settings.MySqlData-SWarps.Username");
    public static final String PasswordS = Main.getConfiguration().getString("settings.MySqlData-SWarps.Password");
    public static final int PortS = Main.getConfiguration().getInt("settings.MySqlData-SWarps.Port");
}
