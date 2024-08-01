package de.justcody.cbmc.boatrace.game;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.map.Map;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import de.justcody.cbmc.translationsystem.TranslationAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Game implements Listener {

    private final TranslationAPI api = TranslationAPI.getInstance();

    private final String prefix = BoatRace.prefix;
    @Getter
    private final GameType type;
    @Getter
    private final List<UUID> players = new ArrayList<>();
    private String cup;
    private final Map map;
    private final List<Location> itemBoxes = new ArrayList<>();
    private final List<UUID> boxes = new ArrayList<>();
    private GameState state;
    private final int reservedLobbyID;

    private final HashMap<UUID, Integer> rounds = new HashMap<>();
    private final HashMap<UUID, Boolean> isAtCheckpoint = new HashMap<>();
    private final List<UUID> finished = new ArrayList<>();


    public Game(GameType type, String mapName) {
        this.type = type;
        map = BoatRace.loadMap(mapName);
        state = GameState.WAITING;
        reservedLobbyID = BoatRace.getPreGameLobbyManager().reserveLobby();
        BoatRace.getPlugin().getServer().getPluginManager().registerEvents(this, BoatRace.getPlugin());
    }

    public void open() {
        for (LocationWrapper w : map.getItemBoxes()) {
            Location l = LocUtil.fromWrapper(w);
            itemBoxes.add(l);
            if (l.getWorld() == null) continue;
            EnderCrystal c = l.getWorld().spawn(l, EnderCrystal.class);
            boxes.add(c.getUniqueId());
        }
    }

    public void joinPlayer(Player p) {
        players.add(p.getUniqueId());
        executeForPlayers(player -> {
            player.sendMessage(prefix+api.translate(player.getLocale().split("_")[0], "games.generic.join")
                    .replace("%player%", p.getDisplayName()));
        });
        BoatRace.getPreGameLobbyManager().sendPlayerToLobby(reservedLobbyID, p);
    }


    public void executeForPlayers(Consumer<Player> action) {
        for (UUID u : players) {
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;
            action.accept(p);
        }
    }

    public void prepareForStart() {
        if (map == null) return;
        BoatRace.getPreGameLobbyManager().freeWaitingLobby(reservedLobbyID);
        state = GameState.STARTING;
        open();
        executeForPlayers(player -> {
            player.sendMessage("\n§x§0§8§4§C§F§B§l§nB§x§0§7§6§6§E§A§l§no§x§0§6§7§F§D§9" +
                    "§l§na§x§0§5§9§9§C§8§l§nt§x§0§3§B§2§B§6§l§nR§x§0§2§C§C§A§5§l§na§x§0§1§E§5" +
                    "§9§4§l§nc§x§0§0§F§F§8§3§l§ne");
            player.sendMessage("§aWelcome to BoatRace!");
            player.sendMessage("§ePrepare yourself now for start. The race will start soon!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    BoatRace.getRaceManager().runMapIntro(map.getCameraPath1(), map.getCameraPath2(), map.getCameraPath3(), player, map, GameMode.ADVENTURE);
                }
            }.runTaskLater(BoatRace.getPlugin(), 40);
        });

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                executeForPlayers(player -> {
                    player.teleport(LocUtil.fromWrapper(map.getStartingPlaces().get(i)));
                    CarManager.createCar(player);
                    i++;
                });
            }
        }.runTaskLater(BoatRace.getPlugin(), 3*4*20+40);

        new BukkitRunnable() {
            @Override
            public void run() {
                startGame();
            }
        }.runTaskLater(BoatRace.getPlugin(), 3*4*20+70);
    }

    public void startGame() {
        executeForPlayers(player -> player.playSound(player, "boatrace:mk.race.start", 1, 1));
        if (map == null) return;
        new BukkitRunnable() {
            int sec = 3;
            @Override
            public void run() {
                switch (sec) {
                    case 3 -> {
                        executeForPlayers(player -> {
                            player.sendTitle("§c3", "", 5, 10, 5);
                        });
                    }
                    case 2 -> {
                        executeForPlayers(player -> {
                            player.sendTitle("§62", "", 5, 10, 5);
                        });
                    }
                    case 1 -> {
                        executeForPlayers(player -> {
                            player.sendTitle("§a1", "", 5, 10, 5);
                        });
                    }
                    case 0 -> {
                        executeForPlayers(player -> {
                            player.sendTitle("§bGOO!!", "", 5, 10, 5);
                            player.playSound(player, map.getMusic(), 1, 1);
                        });
                        state = GameState.RUNNING;
                        cancel();
                        return;
                    }
                }
                sec--;
            }
        }.runTaskTimer(BoatRace.getPlugin(), 0, 20);
    }


    public List<String> createGameSummary() {
        List<String> results = new ArrayList<>();

        int place = 0;

        for (UUID u : finished) {
            place++;
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;
            results.add("§6"+place+". §b"+p.getDisplayName());
        }

        for (int i = 0; i < 6-place;i++) {
            results.add("§6"+place+". §b§m==========");
        }

        return results;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (state.equals(GameState.RUNNING)) {
            if (!players.contains(player.getUniqueId())) return;
        }

        if (LocUtil.isInArea(player.getLocation(), map.getStartLine())) {
            if (isAtCheckpoint.getOrDefault(player.getUniqueId(), false)) {
                isAtCheckpoint.put(player.getUniqueId(), false);
                int nextLap = rounds.getOrDefault(player.getUniqueId(), 0)+1;
                player.sendMessage("§eDEBUG: §7Lap: " + nextLap);
                switch (nextLap) {
                    case 2 -> {
                        player.playSound(player, "boatrace:mk.race.next_lap", 1, 1);
                        player.sendTitle("§eLap 2", "", 10, 70, 20);
                    }
                    case 3 -> {
                        player.stopAllSounds();
                        player.playSound(player, "boatrace:mk.race.final_lap", 1, 1);
                        player.sendTitle("§eFinal Lap", "", 10, 70, 20);
                    }
                    case 4 -> {
                        if (finished.isEmpty() || finished.size() == 1)
                            player.playSound(player, "boatrace:mk.music.finish.excellent", 1, 1);
                        else if (finished.size() == 3 || finished.size() == 4)
                            player.playSound(player, "boatrace:mk.music.finish.good", 1, 1);
                        else
                            player.playSound(player, "boatrace:mk.music.finish.bad", 1, 1);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.playSound(player, "boatrace:mk.music.ending_song", 1, 1);
                            }
                        }.runTaskLater(BoatRace.getPlugin(), 20*5);

                        finished.add(player.getUniqueId());
                        if (finished.size() == players.size()) {
                            List<String> sum = createGameSummary();
                            //All players are done racing
                            executeForPlayers(p -> {
                                p.sendMessage("§7======= §6§lGAME SUMMARY §7=======");
                                for (String s : sum) p.sendMessage(s);
                            });

                        }
                    }
                }
                rounds.put(player.getUniqueId(), nextLap);
            }
        }

        if (LocUtil.isInArea(player.getLocation(), map.getCheckpoints().get(0))) {
            isAtCheckpoint.put(player.getUniqueId(), true);
            //player.sendMessage("§eDEBUG: §7Checkpoint reached!");
            //player.sendMessage("Checkpoint state: "+isAtCheckpoint.get(player.getUniqueId()));
        }
    }
}