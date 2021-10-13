package de.Initium.AntiPortal.Main.Dispatcher;

import de.Initium.AntiPortal.Main.Main;

public class MessageDispatcher {
    public static final String NoPerms = Main.getConfiguration().getString("settings.Messages.NoPerms");
    public static final String NoNameChosen = Main.getConfiguration().getString("settings.Messages.NoNameChosen");
    public static final String WayPCreationSuc = Main.getConfiguration().getString("settings.Messages.WayPointCreationSuccess");
    public static final String WarpPCreationSuc = Main.getConfiguration().getString("settings.Messages.WarpPortalCreationSuccess");
    public static final String NoFreeSpace = Main.getConfiguration().getString("settings.Messages.NoFreeSpace");
    public static final String NoMarkedRegion = Main.getConfiguration().getString("settings.Messages.NoRegionMarked");
    public static final String NoWarpExists = Main.getConfiguration().getString("settings.Messages.WarpDoesNotExists");
    public static final String WarpPAlreadyExists = Main.getConfiguration().getString("settings.Messages.WarpPortalAlreadyExists");
    public static final String PortalDelSuc = Main.getConfiguration().getString("settings.Messages.PortalDeletionSuccess");

}
