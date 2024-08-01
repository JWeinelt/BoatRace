package de.einfachcody.boatrace.game.arena;

import de.einfachcody.boatrace.game.music.Music;
import de.einfachcody.boatrace.util.Direction;

import org.bukkit.Location;

import java.util.List;

@SuppressWarnings("unused")
public class Arena {
    private String displayName;
    private Location startPos1;
    private Location startPos2;
    private Location startPos3;
    private Location startPos4;
    private Location startPos5;
    private Location startPos6;

    private int amountGoals;

    private List<GoalLine> checkpoints;
    private GoalLine goal1;
    private GoalLine goal2;
    private GoalLine goal3;

    private int rounds;

    private List<Location> itemBoxes;

    // Warp Cannon
    private Direction cannonDirection;
    private double cannonYStrength;

    private Music musicTrack;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Location getStartPos1() {
        return startPos1;
    }

    public void setStartPos1(Location startPos1) {
        this.startPos1 = startPos1;
    }

    public Location getStartPos2() {
        return startPos2;
    }

    public void setStartPos2(Location startPos2) {
        this.startPos2 = startPos2;
    }

    public Location getStartPos3() {
        return startPos3;
    }

    public void setStartPos3(Location startPos3) {
        this.startPos3 = startPos3;
    }

    public Location getStartPos4() {
        return startPos4;
    }

    public void setStartPos4(Location startPos4) {
        this.startPos4 = startPos4;
    }

    public Location getStartPos5() {
        return startPos5;
    }

    public void setStartPos5(Location startPos5) {
        this.startPos5 = startPos5;
    }

    public Location getStartPos6() {
        return startPos6;
    }

    public void setStartPos6(Location startPos6) {
        this.startPos6 = startPos6;
    }

    public int getAmountGoals() {
        return amountGoals;
    }

    public void setAmountGoals(int amountGoals) {
        this.amountGoals = amountGoals;
    }

    public List<GoalLine> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<GoalLine> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public GoalLine getGoal1() {
        return goal1;
    }

    public void setGoal1(GoalLine goal1) {
        this.goal1 = goal1;
    }

    public GoalLine getGoal2() {
        return goal2;
    }

    public void setGoal2(GoalLine goal2) {
        this.goal2 = goal2;
    }

    public GoalLine getGoal3() {
        return goal3;
    }

    public void setGoal3(GoalLine goal3) {
        this.goal3 = goal3;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public List<Location> getItemBoxes() {
        return itemBoxes;
    }

    public void setItemBoxes(List<Location> itemBoxes) {
        this.itemBoxes = itemBoxes;
    }

    public Direction getCannonDirection() {
        return cannonDirection;
    }

    public void setCannonDirection(Direction cannonDirection) {
        this.cannonDirection = cannonDirection;
    }

    public double getCannonYStrength() {
        return cannonYStrength;
    }

    public void setCannonYStrength(double cannonYStrength) {
        this.cannonYStrength = cannonYStrength;
    }

    public Music getMusicTrack() {
        return musicTrack;
    }

    public void setMusicTrack(Music musicTrack) {
        this.musicTrack = musicTrack;
    }

    public Location getStartLocation(int l) {
        switch (l) {
            case 1 -> {
                return startPos1;
            }
            case 2 -> {
                return startPos2;
            }
            case 3 -> {
                return startPos3;
            }
            case 4 -> {
                return startPos4;
            }
            case 5 -> {
                return startPos5;
            }
            case 6 -> {
                return startPos6;
            }
        }
        return null;
    }
}
