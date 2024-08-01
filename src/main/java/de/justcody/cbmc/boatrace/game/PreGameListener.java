package de.justcody.cbmc.boatrace.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class PreGameListener implements Listener {
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getUniqueId().equals(UUID.fromString("014637d6-dbe7-4820-bf37-f37a60025a3c"))) {
            // This is the villager at start

        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (FloodgateApi.getInstance().isFloodgatePlayer(e.getPlayer().getUniqueId()) || e.getPlayer().getName().startsWith(".")) {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "§cThis game mode is only for Minecraft: Java Edition! Sorry!");
        } else e.allow();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

    }
}