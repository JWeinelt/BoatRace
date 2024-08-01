package de.einfachcody.boatrace.game;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.awt.*;
import java.util.UUID;

public class RacePlayer {
    private UUID uniqueID;
    private String displayName;


    // In Race
    private int round;
    private Color teamColor;
    private Boat boat;
    private Zombie zombie;
    private boolean hasItem;
    private boolean isThundered;
    private int coins;
    private int balloons;


    public RacePlayer(Player player) {
        uniqueID = player.getUniqueId();
        displayName = player.getName().replace(".", "");
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRound() {
        return round;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public Boat getBoat() {
        return boat;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public boolean isHasItem() {
        return hasItem;
    }

    public boolean isThundered() {
        return isThundered;
    }

    public int getCoins() {
        return coins;
    }

    public int getBalloons() {
        return balloons;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setTeamColor(Color teamColor) {
        this.teamColor = teamColor;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public void setZombie(Zombie zombie) {
        this.zombie = zombie;
    }

    public void setHasItem(boolean hasItem) {
        this.hasItem = hasItem;
    }

    public void setThundered(boolean thundered) {
        isThundered = thundered;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setBalloons(int balloons) {
        this.balloons = balloons;
    }
}
