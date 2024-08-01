package de.einfachcody.boatrace.game.arena;

public class GoalLine {
    private final LocationWrapper loc1;
    private final LocationWrapper loc2;

    public GoalLine(LocationWrapper loc1, LocationWrapper loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public LocationWrapper getLoc1() {
        return loc1;
    }

    public LocationWrapper getLoc2() {
        return loc2;
    }
}
