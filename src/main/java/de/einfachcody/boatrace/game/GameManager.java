package de.einfachcody.boatrace.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {
    private List<RacePlayer> players = new ArrayList<>();

    public RacePlayer getPlayer(UUID uuid) {
        for (RacePlayer p : players) if (p.getUniqueID().equals(uuid)) return p;
        return null;
    }
}
