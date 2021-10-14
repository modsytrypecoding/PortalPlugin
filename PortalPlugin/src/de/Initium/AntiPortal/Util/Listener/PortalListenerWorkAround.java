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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import ovh.bstruntz.hws.HWSPlugin;
import ovh.bstruntz.hws.UserWarp;

import java.util.ArrayList;

public class PortalListenerWorkAround implements Listener {

    @EventHandler
    public static void onPortalWarp(PlayerPortalEvent e) {
        //cancels the PortalEvent in the first place
        e.setCancelled(true);
        Player p = e.getPlayer();

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
                //if Region name isn't wegpunktportal
                for(UserWarp Warp : hws.getUserWarps()) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(Warp.getName().toLowerCase());
                    //check if Region name equals existing Warp
                    if(temp.contains(region1.getId())) {
                        //uses two Methods from the Hws Plugin
                        UserWarp warp = hws.getUserWarpFromName(region1.getId());
                        hws.teleportPlayerToWarp(p, warp);
                    }
                }
            }
        }
    }
}
