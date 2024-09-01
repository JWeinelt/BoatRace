package de.justcody.cbmc.boatrace.database;

import de.justcody.cbmc.boatrace.BoatRace;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BoatRacePlayer {
    private final UUID uniqueID;
    private int racesWins = 0;
    private int battleWins = 0;
    private int racesLoses = 0;
    private int battleLoses = 0;
    private int shellHitOthers = 0;
    private int bombHitOthers = 0;
    private int bananaHitOthers = 0;
    private int shellHitSelf = 0;
    private int bombHitSelf = 0;
    private int bananaHitSelf = 0;
    private int lightningsStroke = 0;
    private int level = 1;
    private int xp = 0;

    private int selectedDriver;
    private int selectedCar;

    public BoatRacePlayer(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void addXP(int amount) {
        xp += amount;
        if (xp >= 50) {
            xp = xp - 50;
            level++;
            BoatRace.getMySQL().sendPlayerData(this);
            DataManager.loadPlayer(uniqueID);
        }
    }
}
