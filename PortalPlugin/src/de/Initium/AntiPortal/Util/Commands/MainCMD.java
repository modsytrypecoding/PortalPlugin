package de.Initium.AntiPortal.Util.Commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.Initium.AntiPortal.Main.Main;
import de.Initium.AntiPortal.Main.Dispatcher.MessageDispatcher;
import de.Initium.AntiPortal.Main.MySqlConnector;
import de.Initium.AntiPortal.Main.MySql_Warp_Box;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ovh.bstruntz.hws.HWSPlugin;
import ovh.bstruntz.hws.UserWarp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;


public class MainCMD implements CommandExecutor {
    public boolean iscreated = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            HWSPlugin hws = new HWSPlugin();
            if (s.equalsIgnoreCase("portal")) {
                if (args[0].equalsIgnoreCase("setportal")) {
                    if (p.hasPermission("AP.setWarpPortal")) {
                        if(args.length == 1) {
                            p.sendMessage(MessageDispatcher.NoNameChosen);
                        }
                        if(args.length <= 2) {
                            BukkitPlayer bp = BukkitAdapter.adapt(p);
                            ArrayList<String> tmp = new ArrayList<>();
                            for(String warps : MySql_Warp_Box.getAllSWarps()) {
                                tmp.add(warps);
                            }
                            if(!Main.getConfiguration().getStringList("settings.activePortals").contains(args[1])) {
                                if(args[1].equalsIgnoreCase("wegpunktportal")) {
                                    try {
                                        Region sel = WorldEditPlugin.getInstance().getWorldEdit().getSessionManager().get(bp).getSelection(bp.getWorld());
                                        BlockVector3 min = sel.getMinimumPoint();
                                        BlockVector3 max = sel.getMaximumPoint();
                                        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                        RegionManager wgregions = container.get(BukkitAdapter.adapt(p.getWorld()));
                                        ProtectedRegion region = new ProtectedCuboidRegion(args[1], min , max);

                                        for (int x = min.getBlockX(); x <= max.getBlockX(); x = x + 1) {
                                            for (int y = min.getBlockY(); y <= max.getBlockY(); y = y + 1) {
                                                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z = z + 1) {
                                                    Location tmpblock = new Location(p.getWorld(), x, y, z);
                                                    if(tmpblock.getBlock().getType().equals(Material.AIR)) {
                                                        tmpblock.getBlock().setType(Material.NETHER_PORTAL);
                                                        iscreated = true;
                                                    }
                                                }
                                            }
                                        }
                                        if(iscreated) {
                                            wgregions.addRegion(region);
                                            PreparedStatement statement = null;
                                            try {
                                                statement = MySqlConnector.connection.prepareStatement("INSERT INTO ActivePortals(Names) VALUES (?)");
                                                statement.setString(1, args[1]);
                                                statement.execute();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                                p.sendMessage("Dieses Portal existiert bereits");
                                            }

                                            p.sendMessage(MessageDispatcher.WayPCreationSuc);
                                        }else {
                                            p.sendMessage(MessageDispatcher.NoFreeSpace);
                                        }



                                    } catch (Exception e) {
                                        Bukkit.getConsoleSender().sendMessage("Fehler " + e + " ausgehend vom Spieler " + p.getName());
                                        p.sendMessage(MessageDispatcher.NoMarkedRegion);
                                    }
                                }else {
                                    if(tmp.contains(args[1])) {
                                        try {
                                            Region sel = WorldEditPlugin.getInstance().getWorldEdit().getSessionManager().get(bp).getSelection(bp.getWorld());
                                            BlockVector3 min = sel.getMinimumPoint();
                                            BlockVector3 max = sel.getMaximumPoint();
                                            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                            RegionManager wgregions = container.get(BukkitAdapter.adapt(p.getWorld()));
                                            ProtectedRegion region = new ProtectedCuboidRegion(args[1], min , max);
                                            for (int x = min.getBlockX(); x <= max.getBlockX(); x = x + 1) {
                                                for (int y = min.getBlockY(); y <= max.getBlockY(); y = y + 1) {
                                                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z = z + 1) {
                                                        Location tmpblock = new Location(p.getWorld(), x, y, z);
                                                        if(tmpblock.getBlock().getType().equals(Material.AIR)) {
                                                            tmpblock.getBlock().setType(Material.NETHER_PORTAL);
                                                            iscreated = true;

                                                        }
                                                    }
                                                }
                                            }
                                            if(iscreated) {
                                                wgregions.addRegion(region);
                                                PreparedStatement statement = null;
                                                try {
                                                    statement = MySqlConnector.connection.prepareStatement("INSERT INTO ActivePortals(Names) VALUES (?)");
                                                    statement.setString(1, args[1]);
                                                    statement.execute();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                                p.sendMessage(MessageDispatcher.WarpPCreationSuc);
                                            }else {
                                                p.sendMessage(MessageDispatcher.NoFreeSpace);
                                            }



                                        } catch (Exception e) {
                                            Bukkit.getConsoleSender().sendMessage("Fehler " + e + " ausgehend vom Spieler " + p.getName());
                                            p.sendMessage(MessageDispatcher.NoMarkedRegion);
                                        }
                                    } else {
                                        p.sendMessage(MessageDispatcher.NoWarpExists);
                                    }
                                }
                            }else {
                                p.sendMessage(MessageDispatcher.WarpPAlreadyExists);
                            }

                        }else {
                            p.sendMessage("Bitte benutze /portal setportal <Name>!");
                        }

                    } else {
                        p.sendMessage(MessageDispatcher.NoPerms);
                    }

                }
                if(args[0].equalsIgnoreCase("delPortal")) {
                    if(p.hasPermission("Ap.delWarpPortal")) {
                        if(args.length == 1) {
                            p.sendMessage(MessageDispatcher.NoNameChosen);
                        }
                        if(args.length <= 2) {
                            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionManager wgregions = container.get(BukkitAdapter.adapt(p.getWorld()));
                            if(wgregions.getRegions().containsKey(args[1])) {
                                Location locPortal = new Location(p.getWorld(), wgregions.getRegion(args[1]).getMinimumPoint().getBlockX(), wgregions.getRegion(args[1]).getMinimumPoint().getBlockY(), wgregions.getRegion(args[1]).getMinimumPoint().getBlockZ());
                                p.sendMessage("Die Warp Funktion des Portals an der Stelle \nx:" + locPortal.getBlockX() + "\ny: " + locPortal.getBlockY() + "\nz: " + locPortal.getBlockZ() + "\nwurden entfernt!");
                                p.sendMessage("Das zuvor genutze Portal wurde wieder gesperrt!");
                                PreparedStatement statement = null;
                                try {
                                    statement = MySqlConnector.connection.prepareStatement("DELETE FROM ActivePortals WHERE Names = ?");
                                    statement.setString(1, args[1]);
                                    statement.execute();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                wgregions.removeRegion(args[1]);
                                p.sendMessage(MessageDispatcher.PortalDelSuc);
                            }else {
                                p.sendMessage("Das von dir gewählte WarpPortal " + args[1] + " existiert nicht");
                            }
                        }else {
                            p.sendMessage("Bitte benutze /portal delportal <Name>!");
                        }

                    }else {
                        p.sendMessage(MessageDispatcher.NoPerms);
                    }
                }
            }
        }
        return false;
    }

}
