package de.einfachcody.boatrace.game.arena;

import de.einfachcody.boatrace.game.music.Music;
import de.einfachcody.boatrace.util.Direction;

import java.util.List;


@SuppressWarnings("unused")
public class ArenaWrapper {
    private String displayName;
    private LocationWrapper startPos1;
    private LocationWrapper startPos2;
    private LocationWrapper startPos3;
    private LocationWrapper startPos4;
    private LocationWrapper startPos5;
    private LocationWrapper startPos6;

    private int amountGoals;

    private List<GoalLine> checkpoints;
    private GoalLine goal1;
    private GoalLine goal2;
    private GoalLine goal3;

    private int rounds;

    private List<LocationWrapper> itemBoxes;

    // Warp Cannon
    private Direction cannonDirection;
    private double cannonYStrength;

    private Music musicTrack;

    public Music getMusicTrack() {
        return musicTrack;
    }

    public void setMusicTrack(Music musicTrack) {
        this.musicTrack = musicTrack;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocationWrapper getStartPos1() {
        return startPos1;
    }

    public void setStartPos1(LocationWrapper startPos1) {
        this.startPos1 = startPos1;
    }

    public LocationWrapper getStartPos2() {
        return startPos2;
    }

    public void setStartPos2(LocationWrapper startPos2) {
        this.startPos2 = startPos2;
    }

    public LocationWrapper getStartPos3() {
        return startPos3;
    }

    public void setStartPos3(LocationWrapper startPos3) {
        this.startPos3 = startPos3;
    }

    public LocationWrapper getStartPos4() {
        return startPos4;
    }

    public void setStartPos4(LocationWrapper startPos4) {
        this.startPos4 = startPos4;
    }

    public LocationWrapper getStartPos5() {
        return startPos5;
    }

    public void setStartPos5(LocationWrapper startPos5) {
        this.startPos5 = startPos5;
    }

    public LocationWrapper getStartPos6() {
        return startPos6;
    }

    public void setStartPos6(LocationWrapper startPos6) {
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

    public List<LocationWrapper> getItemBoxes() {
        return itemBoxes;
    }

    public void setItemBoxes(List<LocationWrapper> itemBoxes) {
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
}
