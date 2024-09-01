package de.justcody.cbmc.boatrace.util.papi;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.database.BoatRacePlayer;
import de.justcody.cbmc.boatrace.database.DataManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BoatRacePAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "boatrace";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JustCody";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equals("level")) {
            BoatRacePlayer player1 = DataManager.getPlayer(player.getUniqueId());
            return player1.getLevel() + "";
        }
        return null;
    }
}
