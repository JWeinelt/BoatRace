package de.justcody.cbmc.boatrace.database;


import de.codeblocksmc.codelib.MySQLTemplate;
import de.justcody.cbmc.boatrace.BoatRace;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class MySQL extends MySQLTemplate {

    public MySQL() {
        super(BoatRace.getLog(), "85.202.163.115", 3306, "plugins", "plugins", "3S+xf|3<@8K5pE£");
    }

    public void sendPlayerData() {
        checkConnection();

        for (BoatRacePlayer p : DataManager.players) {
            try {
                String sql = "INSERT INTO plugins.boatrace_levels (uuid, level, XP) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE " +
                        "level = ?" +
                        ", XP = ?";

                PreparedStatement pS = conn.prepareStatement(sql);

                int l = p.getLevel();
                int xp = p.getXp();

                pS.setString(1, p.getUniqueID().toString());
                pS.setInt(2, l);
                pS.setInt(3, xp);
                pS.setInt(4, l);
                pS.setInt(5, xp);

                pS.execute();
            } catch (SQLException e) {
                log.warning(e.getMessage());
            }


            try {
                String sql = "INSERT INTO plugins.boatrace_stats (uuid, race_wins, race_loses, shell_hit_others, " +
                        "bomb_hit_others, banana_hit_others, shell_hit_self, bomb_hit_self, banana_hit_self, " +
                        "lightnings_stroke, battle_wins, battle_loses) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE race_wins = ?, " +
                        "race_loses = ?, shell_hit_others = ?, bomb_hit_others = ?, banana_hit_others = ?, shell_hit_self = ?, " +
                        "bomb_hit_self = ?, banana_hit_self = ?, lightnings_stroke = ?, battle_wins = ?, battle_loses = ?";

                PreparedStatement pS = conn.prepareStatement(sql);

                pS.setString(1, p.getUniqueID().toString());
                pS.setInt(2, p.getRacesWins());
                pS.setInt(3, p.getRacesLoses());
                pS.setInt(4, p.getShellHitOthers());
                pS.setInt(5, p.getBombHitOthers());
                pS.setInt(6, p.getBananaHitOthers());
                pS.setInt(7, p.getShellHitSelf());
                pS.setInt(8, p.getBombHitSelf());
                pS.setInt(9, p.getBananaHitSelf());
                pS.setInt(10, p.getLightningsStroke());
                pS.setInt(11, p.getBattleWins());
                pS.setInt(12, p.getBattleLoses());
                pS.setInt(13, p.getRacesWins());
                pS.setInt(14, p.getRacesLoses());
                pS.setInt(15, p.getShellHitOthers());
                pS.setInt(16, p.getBombHitOthers());
                pS.setInt(17, p.getBananaHitOthers());
                pS.setInt(18, p.getShellHitSelf());
                pS.setInt(19, p.getBombHitSelf());
                pS.setInt(20, p.getBananaHitSelf());
                pS.setInt(21, p.getLightningsStroke());
                pS.setInt(22, p.getBattleWins());
                pS.setInt(23, p.getBattleLoses());

                pS.execute();
            } catch (SQLException e) {
                log.warning(e.getMessage());
            }


            try {
                String sql = "INSERT INTO plugins.boatrace_player_selections (uuid, driver, car) VALUES (?, ?, ?)" +
                        " ON DUPLICATE KEY UPDATE driver = ?, car = ?";

                PreparedStatement pS = conn.prepareStatement(sql);
                pS.setString(1, p.getUniqueID().toString());
                pS.setInt(2, p.getSelectedDriver());
                pS.setInt(3, p.getSelectedCar());
                pS.setInt(4, p.getSelectedDriver());
                pS.setInt(5, p.getSelectedCar());

                pS.execute();
            } catch (SQLException e) {
                log.warning(e.getMessage());
            }
        }
    }

    public void sendPlayerData(BoatRacePlayer p) {
        checkConnection();
        try {
            String sql = "INSERT INTO plugins.boatrace_levels (uuid, level, XP) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE " +
                    "level = ?" +
                    ", XP = ?";

            PreparedStatement pS = conn.prepareStatement(sql);

            int l = p.getLevel();
            int xp = p.getXp();

            pS.setString(1, p.getUniqueID().toString());
            pS.setInt(2, l);
            pS.setInt(3, xp);
            pS.setInt(4, l);
            pS.setInt(5, xp);

            pS.execute();
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }


        try {
            String sql = "INSERT INTO plugins.boatrace_stats (uuid, race_wins, race_loses, shell_hit_others, " +
                    "bomb_hit_others, banana_hit_others, shell_hit_self, bomb_hit_self, banana_hit_self, " +
                    "lightnings_stroke, battle_wins, battle_loses) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE race_wins = ?, " +
                    "race_loses = ?, shell_hit_others = ?, bomb_hit_others = ?, banana_hit_others = ?, shell_hit_self = ?, " +
                    "bomb_hit_self = ?, banana_hit_self = ?, lightnings_stroke = ?, battle_wins = ?, battle_loses = ?";

            PreparedStatement pS = conn.prepareStatement(sql);

            pS.setString(1, p.getUniqueID().toString());
            pS.setInt(2, p.getRacesWins());
            pS.setInt(3, p.getRacesLoses());
            pS.setInt(4, p.getShellHitOthers());
            pS.setInt(5, p.getBombHitOthers());
            pS.setInt(6, p.getBananaHitOthers());
            pS.setInt(7, p.getShellHitSelf());
            pS.setInt(8, p.getBombHitSelf());
            pS.setInt(9, p.getBananaHitSelf());
            pS.setInt(10, p.getLightningsStroke());
            pS.setInt(11, p.getBattleWins());
            pS.setInt(12, p.getBattleLoses());
            pS.setInt(13, p.getRacesWins());
            pS.setInt(14, p.getRacesLoses());
            pS.setInt(15, p.getShellHitOthers());
            pS.setInt(16, p.getBombHitOthers());
            pS.setInt(17, p.getBananaHitOthers());
            pS.setInt(18, p.getShellHitSelf());
            pS.setInt(19, p.getBombHitSelf());
            pS.setInt(20, p.getBananaHitSelf());
            pS.setInt(21, p.getLightningsStroke());
            pS.setInt(22, p.getBattleWins());
            pS.setInt(23, p.getBattleLoses());

            pS.execute();
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }


        try {
            String sql = "INSERT INTO plugins.boatrace_player_selections (uuid, driver, car) VALUES (?, ?, ?)" +
                    " ON DUPLICATE KEY UPDATE driver = ?, car = ?";

            PreparedStatement pS = conn.prepareStatement(sql);
            pS.setString(1, p.getUniqueID().toString());
            pS.setInt(2, p.getSelectedDriver());
            pS.setInt(3, p.getSelectedCar());
            pS.setInt(4, p.getSelectedDriver());
            pS.setInt(5, p.getSelectedCar());

            pS.execute();
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }

    }

    public BoatRacePlayer getPlayerData(UUID uuid) {
        checkConnection();

        try {
            String sql = "SELECT boatrace_stats.uuid, level, xp, race_wins, race_loses, shell_hit_others, shell_hit_self," +
                    " bomb_hit_others, bomb_hit_self, banana_hit_others, banana_hit_self, lightnings_stroke, battle_wins, " +
                    "battle_loses, driver, car FROM plugins.boatrace_stats LEFT OUTER JOIN plugins.boatrace_levels " +
                    "ON boatrace_stats.uuid = boatrace_levels.uuid LEFT OUTER JOIN plugins.boatrace_player_selections" +
                    " ON boatrace_stats.uuid = boatrace_player_selections.uuid WHERE boatrace_stats.uuid = ?";

            PreparedStatement pS = conn.prepareStatement(sql);
            pS.setString(1, uuid.toString());

            ResultSet set = pS.executeQuery();

            if (set.next()) {
                BoatRacePlayer player = new BoatRacePlayer(UUID.fromString(set.getString("uuid")));
                player.setLevel(set.getInt("level"));
                player.setXp(set.getInt("xp"));
                player.setRacesWins(set.getInt("race_wins"));
                player.setRacesLoses(set.getInt("race_loses"));
                player.setShellHitOthers(set.getInt("shell_hit_others"));
                player.setShellHitSelf(set.getInt("shell_hit_self"));
                player.setBombHitOthers(set.getInt("bomb_hit_others"));
                player.setBombHitSelf(set.getInt("bomb_hit_self"));
                player.setBananaHitOthers(set.getInt("banana_hit_others"));
                player.setBananaHitSelf(set.getInt("banana_hit_self"));
                player.setLightningsStroke(set.getInt("lightnings_stroke"));
                player.setBattleWins(set.getInt("battle_wins"));
                player.setBattleLoses(set.getInt("battle_loses"));
                player.setSelectedDriver(set.getInt("driver"));
                player.setSelectedCar(set.getInt("car"));
                return player;
            }
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }
        return null;
    }
}
