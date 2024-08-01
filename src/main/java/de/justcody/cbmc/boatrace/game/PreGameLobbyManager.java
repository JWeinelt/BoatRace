package de.justcody.cbmc.boatrace.game;

import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PreGameLobbyManager {

    private final HashMap<Integer, PreGameLobby> lobbies = new HashMap<>();

    private final ArrayList<LocationWrapper> lobbyPositions = new ArrayList<>();

    public PreGameLobbyManager() {
        lobbyPositions.add(new LocationWrapper("world", -27.5, 32, -109.5, 0, 0));
        lobbyPositions.add(new LocationWrapper("world", 18.5, 32, -109.5, 0, 0));
        lobbyPositions.add(new LocationWrapper("world", 20.9, 32, -141.5, 0, 0));
        lobbyPositions.add(new LocationWrapper("world", -27.5, 32, -141.5, 0, 0));
    }

    public int reserveLobby() {
        PreGameLobby gameLobby = new PreGameLobby(LocUtil.fromWrapper(lobbyPositions.get(lobbies.size())));
        int id = lobbies.size();
        lobbies.put(id, gameLobby);
        return id;
    }

    public void sendPlayerToLobby(int id, Player player) {
        lobbies.getOrDefault(id, null).teleportPlayer(player);
    }

    public void freeWaitingLobby(int id) {
        lobbies.remove(id);
    }

    static class PreGameLobby {
        private final Location spawnLocation;

        public PreGameLobby(Location spawnLocation) {
            this.spawnLocation = spawnLocation;
        }

        public void teleportPlayer(Player player) {
            player.teleport(spawnLocation);
        }
    }
}
