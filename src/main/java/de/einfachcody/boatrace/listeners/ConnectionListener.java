package de.einfachcody.boatrace.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.einfachcody.boatrace.BoatRace.prefix;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(prefix + "§aWelcome to BoatRace, §e" + p.getDisplayName()
        + "§a!");
    }
}
