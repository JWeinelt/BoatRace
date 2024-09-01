package de.justcody.cbmc.boatrace.database;

import de.justcody.cbmc.boatrace.BoatRace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataManager {
    public static final List<BoatRacePlayer> players = new ArrayList<>();

    public static void loadPlayer(UUID uuid) {
        BoatRacePlayer p = BoatRace.getMySQL().getPlayerData(uuid);
        if (p == null) return;
        removePlayer(uuid);
        players.add(p);
    }

    public static void removePlayer(UUID uuid) {
        players.removeIf(p -> p.getUniqueID().equals(uuid));
    }

    public static void quitPlayer(Player player) {
        BoatRacePlayer p = getPlayer(player.getUniqueId());
        BoatRace.getMySQL().sendPlayerData(p);
        removePlayer(player.getUniqueId());
    }

    @NotNull
    public static BoatRacePlayer getPlayer(UUID uuid) {
        for (BoatRacePlayer p : players) if (p.getUniqueID().equals(uuid)) return p;
        BoatRacePlayer p = new BoatRacePlayer(uuid);
        players.add(p);
        return p;
    }
}