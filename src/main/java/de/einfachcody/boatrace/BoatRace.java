package de.einfachcody.boatrace;

import org.bukkit.plugin.java.JavaPlugin;

public final class BoatRace extends JavaPlugin {

    private static BoatRace plugin;
    public static final String prefix =
            "§x§f§b§5§f§5§f§lB§x§f§3§7§5§7§6§lo§x§e§a§8§a§8§c§la§" +
                    "x§e§2§a§0§a§3§lt§x§d§9§b§5§b§9§lR§x§d§1§c§b§d§0§la§x§c§8§e§0§e§6§lc§x§c§0§f§6§f§d§le §7| ";



    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BoatRace getPlugin() {
        return plugin;
    }
}
