package de.Initium.AntiPortal.Util.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ovh.bstruntz.hws.HWSPlugin;
import ovh.bstruntz.hws.UserWarp;

public class ClickEvent implements Listener {

    @EventHandler
    public static void onCLick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getView().getTitle().equalsIgnoreCase("WarpSelection")) {
            e.setCancelled(true);
            HWSPlugin hws = new HWSPlugin();
            UserWarp warp = hws.getUserWarpFromName(e.getCurrentItem().getItemMeta().getDisplayName());
            hws.teleportPlayerToWarp(p, warp);
        }
    }
}
