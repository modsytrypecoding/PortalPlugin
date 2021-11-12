package de.Initium.AntiPortal.Main;

import de.Initium.AntiPortal.Util.Commands.MainCMD;
import de.Initium.AntiPortal.Util.Listener.ClickEvent;
import de.Initium.AntiPortal.Util.Listener.PortalListenerWorkAround;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Main plugin;
    PluginDescriptionFile pdf = this.getDescription();
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final File configfile = new File("plugins//PortalPlugin//config.yml");
    private static YamlConfiguration configfileConfiguration = YamlConfiguration.loadConfiguration(configfile);

    public void onEnable() {

        try {
            log.info("Connecting to MySql...");
            MySqlConnector.connect();
            MySqlConnector_SWarps.connect();
            log.info(ChatColor.GREEN + "[MySql] Connection success");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        plugin = this;
        PluginManager pl = Bukkit.getPluginManager();
        pl.registerEvents(new PortalListenerWorkAround(), this);
        pl.registerEvents(new ClickEvent(), this);

        Objects.requireNonNull(getCommand("portal")).setExecutor(new MainCMD());

        this.getLogger().info("Plugin erfolgreich geladen.");
        this.getLogger().info("Version: " + pdf.getVersion());

        if (!configfile.exists() || !configfileConfiguration.isSet("settings")) {
            configfileConfiguration.createSection("settings");
            configfileConfiguration.createSection("settings.Messages");
            configfileConfiguration.set("settings.Messages.NoPerms", "Dazu hast du keine Berechtigungen!");
            configfileConfiguration.set("settings.Messages.NoNameChosen", "Du hast den Namen des Warps vergessen!");
            configfileConfiguration.set("settings.Messages.WayPointCreationSuccess", "WegPunktPortal erfolgreich erstellt!");
            configfileConfiguration.set("settings.Messages.WarpPortalCreationSuccess", "WarpPortal erfolgreich erstellt!");
            configfileConfiguration.set("settings.Messages.NoFreeSpace", "Es wurde kein freier Platz für ein Portal gefunden!");
            configfileConfiguration.set("settings.Messages.NoRegionMarked", "Du hast keinen Rahmen markiert!");
            configfileConfiguration.set("settings.Messages.WarpDoesNotExists", "Der gewählte Warp existiert nicht!");
            configfileConfiguration.set("settings.Messages.WarpPortalAlreadyExists", "Dieses Warp-Portal existiert bereits!");
            configfileConfiguration.set("settings.Messages.PortalDeletionSuccess", "Warp-Portal erfolgreich gelöscht!");
            configfileConfiguration.createSection("settings.MySqlData");
            configfileConfiguration.set("settings.MySqlData.Host", "localhost");
            configfileConfiguration.set("settings.MySqlData.DataBase", "Portal");
            configfileConfiguration.set("settings.MySqlData.Username", "Portal");
            configfileConfiguration.set("settings.MySqlData.Password", "6zrl.NVHbyxio.-m");
            configfileConfiguration.set("settings.MySqlData.Port", 3306);

            configfileConfiguration.createSection("settings.MySqlData-SWarps");
            configfileConfiguration.set("settings.MySqlData-SWarps.Host", "localhost");
            configfileConfiguration.set("settings.MySqlData-SWarps.Port", 3306);
            configfileConfiguration.set("settings.MySqlData-SWarps.DataBase", "HWS-Dev");
            configfileConfiguration.set("settings.MySqlData-SWarps.Username", "home_warp_spawn_dev");
            configfileConfiguration.set("settings.MySqlData-SWarps.Password", "@gqfeI(nXTL@/372");
            saveConfiguration();
        }

    }

    public static Main getPlugin() {
        return plugin;
    }

    public static YamlConfiguration getConfiguration() {
        return configfileConfiguration;
    }

    public static void saveConfiguration() {
        try {
            configfileConfiguration.save(configfile);
            configfileConfiguration = YamlConfiguration.loadConfiguration(configfile);
        } catch (IOException e) {
            getPlugin().getLogger().info("Fehler beim Speichern der config.yml: " + e);
        }
    }

}
