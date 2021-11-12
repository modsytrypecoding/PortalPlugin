package de.Initium.AntiPortal.Main;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySql_Warp_Box {

    public static Object getSWarpLocationByName(String Name) {
        PreparedStatement statement = null;
        Object test = null;
        try {
            statement = MySqlConnector_SWarps.connection.prepareStatement("SELECT warp_loc FROM tbl_adminWarps WHERE warp_name = ?");
            statement.setString(1, Name);
            ResultSet result = statement.executeQuery();
            if(!(result == null)) {
                while (result.next()) {
                    test = result.getObject("warp_loc");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return test;
    }

   public static ArrayList<String> getAllSWarps() {

        ArrayList<String> Swarps = new ArrayList<>();
        try {
            PreparedStatement statement = MySqlConnector_SWarps.connection.prepareStatement("SELECT warp_name FROM tbl_adminWarps");
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String Warps = result.getString("warp_name").toLowerCase();
                Swarps.add(Warps);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Swarps;
    }
}
