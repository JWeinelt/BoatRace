package de.einfachcody.boatrace.stats;

import de.einfachcody.boatrace.game.achieve.Driver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlayerStat {
    private LocalDate firstJoinDate;
    private String displayName;
    private String friendID;
    private int playedGames;
    private int gamesWon;
    private int gamesLost;
    private int gamesWonBattle;
    private int playedGamesBattle;
    private int gamesLostBattle;

    private int quartalPoints = 0;
    private int seasonPoints = 0;
    private int yearPoints = 0;

    private List<Driver> unlockedDrivers = new ArrayList<>();
    private Driver defaultDriver;
    // List of friend ids
    private List<String> friends = new ArrayList<>();

    public int getQuartalPoints() {
        return quartalPoints;
    }

    public int getSeasonPoints() {
        return seasonPoints;
    }

    public int getYearPoints() {
        return yearPoints;
    }

    public Driver getDefaultDriver() {
        return defaultDriver;
    }

    public void setQuartalPoints(int quartalPoints) {
        this.quartalPoints = quartalPoints;
    }

    public void setSeasonPoints(int seasonPoints) {
        this.seasonPoints = seasonPoints;
    }

    public void setYearPoints(int yearPoints) {
        this.yearPoints = yearPoints;
    }

    public void setDefaultDriver(Driver defaultDriver) {
        this.defaultDriver = defaultDriver;
    }

    public LocalDate getFirstJoinDate() {
        return firstJoinDate;
    }

    public void setFirstJoinDate(LocalDate firstJoinDate) {
        this.firstJoinDate = firstJoinDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getGamesWonBattle() {
        return gamesWonBattle;
    }

    public void setGamesWonBattle(int gamesWonBattle) {
        this.gamesWonBattle = gamesWonBattle;
    }

    public int getPlayedGamesBattle() {
        return playedGamesBattle;
    }

    public void setPlayedGamesBattle(int playedGamesBattle) {
        this.playedGamesBattle = playedGamesBattle;
    }

    public int getGamesLostBattle() {
        return gamesLostBattle;
    }

    public void setGamesLostBattle(int gamesLostBattle) {
        this.gamesLostBattle = gamesLostBattle;
    }

    public List<Driver> getUnlockedDrivers() {
        return unlockedDrivers;
    }

    public void unlockDriver(Driver driver) {
        unlockedDrivers.add(driver);
    }

    public List<String> getFriends() {
        return friends;
    }
}
