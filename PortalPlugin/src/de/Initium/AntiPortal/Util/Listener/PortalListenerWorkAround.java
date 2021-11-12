package de.Initium.AntiPortal.Util.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.Initium.AntiPortal.Main.Main;
import de.Initium.AntiPortal.Main.MySql_Warp_Box;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import ovh.bstruntz.hws.HWSPlugin;
import ovh.bstruntz.hws.UserWarp;

import java.util.ArrayList;

public class PortalListenerWorkAround implements Listener {

    @EventHandler
    public static void onPortalWarp(PlayerMoveEvent e) {


        Player p = e.getPlayer();

        org.bukkit.Location tempLoc = new org.bukkit.Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ());
        if(tempLoc.getBlock().getType().equals(Material.NETHER_PORTAL)) {
            HWSPlugin hws = new HWSPlugin();

            //get the Region(s) Player stands in
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager wgregions = container.get(BukkitAdapter.adapt(p.getWorld()));
            Location Loc = BukkitAdapter.adapt(p.getLocation());
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(Loc);

            //do smth for the specific Region(s)
            for(ProtectedRegion region1 : set) {
                if(region1.getId().equalsIgnoreCase("wegpunktportal")) {

                    //teleport player out of the Portal
                    Vector v = p.getLocation().getDirection();
                    org.bukkit.Location loc = p.getLocation();
                    v.normalize();
                    v.multiply(-2); //two blocks
                    loc.add(v);
                    p.teleport(loc);
                    p.teleport(p.getLocation().add(0, wgregions.getRegion(region1.getId()).getMinimumPoint().getY() - p.getLocation().getY(), 0));
                    //run a DelayedTask to open the Warp inv *after* teleporting
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            Inventory inv = Bukkit.createInventory(null, 9*3, "WarpSelection");
                            for(UserWarp warps : hws.getUserWarps()) {
                                ItemStack item = new ItemStack(Material.ENDER_EYE);
                                ItemMeta im = item.getItemMeta();
                                im.setDisplayName(warps.getName());
                                item.setItemMeta(im);
                                inv.addItem(item);
                            }
                            p.openInventory(inv);
                        }
                    },1L); // 1 tick

                }else {
                    if(MySql_Warp_Box.getAllSWarps().contains(region1.getId())) {
                        String Warp = (String) MySql_Warp_Box.getSWarpLocationByName(region1.getId());
                        String [] parm = Warp.split(",");

                        org.bukkit.Location loc = new org.bukkit.Location( Bukkit.getWorld(parm[0]), Double.parseDouble(parm[1]), Double.parseDouble(parm[2]), Double.parseDouble(parm[3]), Float.parseFloat(parm[4]), Float.parseFloat(parm[5]));
                        p.teleport(loc);
                    }
                }
            }
        }

    }

    @EventHandler
    public static void onStopEntityPortal(EntityPortalEvent e) {
        //stops Entities from using Portals
        e.setCancelled(true);
    }

    @EventHandler
    public static void stopPlayerPortal(PlayerPortalEvent e) {
        e.setCancelled(true);
    }
}
