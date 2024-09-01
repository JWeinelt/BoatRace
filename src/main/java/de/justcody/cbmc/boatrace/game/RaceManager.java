package de.justcody.cbmc.boatrace.game;

import com.destroystokyo.paper.ClientOption;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.map.CameraIntroPath;
import de.justcody.cbmc.boatrace.game.map.CupInst;
import de.justcody.cbmc.boatrace.game.map.Map;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.translationsystem.TranslationAPI;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RaceManager {
    private final Gson GSON;
    private final File dataDir;

    private final List<Game> games = new ArrayList<>();
    private final List<Cup> loadedCups = new ArrayList<>();

    private HashMap<String, Long> mapTimeZones = new HashMap<>();

    public RaceManager() {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        dataDir = BoatRace.getPlugin().getDataFolder();
        BoatRace.getPlugin().getDataFolder().mkdirs();
    }

    public void setTimeZone(String map, long time) {
        mapTimeZones.put(map, time);
    }

    public long getTime(String map) {
        return mapTimeZones.getOrDefault(map, 0L);
    }

    public void saveTimeZones() {
        String json = GSON.toJson(mapTimeZones);

        try (FileWriter writer = new FileWriter(new File(dataDir, "timeZones.json"))) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            BoatRace.getLog().warning(e.getMessage());
        }
    }

    public void loadTimes() {
        if (!(new File(dataDir, "timeZones.json").exists())) saveCups();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dataDir, "timeZones.json")));) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            Type mapType = new TypeToken<HashMap<String, Long>>(){}.getType();
            mapTimeZones = GSON.fromJson(jsonStringBuilder.toString(), mapType);

        } catch (IOException e) {
            BoatRace.getLog().warning(e.getMessage());
        }
    }

    public void joinGame(Player player, GameType t) {
        Game newGame = findGame(t);
        if (newGame == null) newGame = createNewGame(t);
        newGame.joinPlayer(player);
    }

    public Game findGame(GameType t) {
        for (Game g : games) if (g.getType().equals(t)) return g;
        return null;
    }

    @Nullable
    public Game getPlayerGame(UUID uuid) {
        for (Game g : games) {
            if (g.getPlayers().contains(uuid)) return g;
        }
        return null;
    }

    public Game createNewGame(GameType t) {
        Game g = new Game(t, "Redline"); //TODO: Make relative
        games.add(g);
        return g;
    }

    public Game createNewGameMap(GameType t, String mapName) {
        Game g = new Game(t, mapName);
        games.add(g);
        return g;
    }
    @Nullable
    public Game createNewGame(GameType t, String cupName) {
        Cup cup = getCup(cupName);
        if (cup == null) {
            return null;
        }
        if (cup.getTrack1() == null) {
            BoatRace.getLog().severe("Could not find track 1");
            return null;
        }
        Game g = new Game(t, cup.getTrack1());
        g.setCup(cupName);
        games.add(g);
        return g;
    }
    public void continueGame(Game game) {
        Cup cup = getCup(game.getCup());
        if (cup == null) return;
        GameResult gameResult = getGameResult(cup, game);
        HandlerList.unregisterAll(game);
        goOnCup(game, gameResult);
    }

    public void goOnCup(Game game, GameResult result) {
        if (!result.equals(GameResult.WINNING_SCENE)) {
            String map = getNextTrack(game, getCup(game.getCup()));
            Game game1 = new Game(game, map, game.getPoints());
            game1.prepareForStart();
            games.add(game1);
            games.remove(game);
        }
    }


    private GameResult getGameResult(Cup cup, Game game) {
        int lastTrack;
        if (game.getMapName().equals(cup.getTrack1())) {
            lastTrack = 1;
        } else if (game.getMapName().equals(cup.getTrack2())) {
            lastTrack = 2;
        } else if (game.getMapName().equals(cup.getTrack3())) {
            lastTrack = 3;
        } else if (game.getMapName().equals(cup.getTrack4())) {
            lastTrack = 4;
        } else lastTrack = 4;
        switch (lastTrack) {
            case 1 -> {
                return GameResult.NEXT_RACE_2;
            }
            case 2 -> {
                return GameResult.NEXT_RACE_3;
            }
            case 3 -> {
                return GameResult.NEXT_RACE_4;
            }
            default -> {
                return GameResult.WINNING_SCENE;
            }
        }
    }

    public String getNextTrack(Game game, Cup cup) {
        int lastTrack;
        if (game.getMapName().equals(cup.getTrack1())) {
            lastTrack = 1;
        } else if (game.getMapName().equals(cup.getTrack2())) {
            lastTrack = 2;
        } else if (game.getMapName().equals(cup.getTrack3())) {
            lastTrack = 3;
        } else if (game.getMapName().equals(cup.getTrack4())) {
            lastTrack = 4;
        } else lastTrack = 4;
        switch (lastTrack) {
            case 1 -> {
                return cup.getTrack2();
            }
            case 2 -> {
                return cup.getTrack3();
            }
            case 3 -> {
                return cup.getTrack4();
            }
            default -> {
                return "Winning";
            }
        }
    }


    public void addCup(String name, Material mat) {
        loadedCups.add(new Cup(name, mat));
    }

    public void addCup(Cup cup) {
        loadedCups.add(cup);
    }

    public List<String> getCups() {
        List<String> s = new ArrayList<>();

        for (Cup c : loadedCups) {
            s.add(c.getName());
        }

        return s;
    }
    @Nullable
    public Cup getCup(String name) {
        for (Cup c : loadedCups) if (c.getName().equals(name)) return c;
        return null;
    }

    public void runMapIntro(CameraIntroPath p1, CameraIntroPath p2, CameraIntroPath p3, Player player, Map map, GameMode modeAfter) {
        //player.getInventory().clear();
        TranslationAPI api = TranslationAPI.getInstance();
        previewCameraPath(p1, player, GameMode.SPECTATOR);
        player.closeInventory();

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§aBoatRace", "§eMap: "+api.translate(player.getClientOption(ClientOption.LOCALE).split("_")[0], "boatrace.map."+map.getMapName()), 10, 70, 20);
            }
        }.runTaskLater(BoatRace.getPlugin(), 20*2);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("", "§eBuilt by: §7"+map.getBuilder(), 10, 70, 20);
            }
        }.runTaskLater(BoatRace.getPlugin(), 20*7);

        new BukkitRunnable() {
            @Override
            public void run() {
                previewCameraPath(p2, player, GameMode.SPECTATOR);
            }
        }.runTaskLater(BoatRace.getPlugin(), 20*4+5);

        new BukkitRunnable() {
            @Override
            public void run() {
                previewCameraPath(p3, player, modeAfter);
            }
        }.runTaskLater(BoatRace.getPlugin(), 20*8+5);
    }


    public void previewCameraPath(CameraIntroPath path, Player player, GameMode modeAfter) {
        // Spawn den ArmorStand am Startpunkt
        ArmorStand s1 = player.getWorld().spawn(LocUtil.fromWrapper(path.getStartPos()), ArmorStand.class);
        s1.setVisible(false);
        s1.setAI(false);
        s1.setBasePlate(false);
        s1.setGravity(false);

        // Setze den Spieler in den Zuschauermodus und lasse ihn den ArmorStand beobachten
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(s1);

        // Berechne die Entfernung zwischen Start und Ende
        Location start = LocUtil.fromWrapper(path.getStartPos());
        Location end = LocUtil.fromWrapper(path.getEnPos());
        double distance = start.distance(end);

        // Berechne die Anzahl der Ticks für die Bewegung (4 Sekunden)
        int totalTicks = 20 * 4; // 20 Ticks pro Sekunde * 4 Sekunden
        // Berechne die Bewegungsvektoren pro Tick
        double stepX = (end.getX() - start.getX()) / totalTicks;
        double stepY = (end.getY() - start.getY()) / totalTicks;
        double stepZ = (end.getZ() - start.getZ()) / totalTicks;
        float yaw = (end.getYaw() - start.getYaw()) / totalTicks;
        float pitch = (end.getPitch() - start.getPitch()) / totalTicks;

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(modeAfter);
                s1.remove();
            }
        }.runTaskLater(BoatRace.getPlugin(), 20*4);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {

                if (ticks == totalTicks - 10) {
                    player.sendTitle("\uE000", "", 10, 20, 10);
                }
                if (ticks >= totalTicks) {
                    s1.remove();
                    cancel();
                    return;
                }

                // Berechne die neue Position des ArmorStands
                Location currentLocation = s1.getLocation();
                currentLocation.add(new Location(currentLocation.getWorld(), stepX, stepY, stepZ, yaw, pitch));

                // Teleportiere den ArmorStand zur neuen Position
                s1.teleport(currentLocation);
                player.setSpectatorTarget(s1);
                player.teleport(s1.getLocation());

                ticks++;
            }
        }.runTaskTimer(BoatRace.getPlugin(), 0, 1);

    }



    public void saveCups() {
        String json = GSON.toJson(loadedCups);

        try (FileWriter writer = new FileWriter(new File(dataDir, "cups.json"))) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCups() {
        if (!(new File(dataDir, "cups.json").exists())) saveCups();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dataDir, "cups.json")));) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            Type mapType = new TypeToken<List<CupInst>>(){}.getType();
            List<CupInst> c = GSON.fromJson(jsonStringBuilder.toString(), mapType);
            for (CupInst i : c) {
                loadedCups.add(new Cup(i.getName(), i.getDisplayMaterial(), i.getTrack1(), i.getTrack2(), i.getTrack3(), i.getTrack4()));
                BoatRace.getPlugin().getLogger().info("Found and loaded cup: " + i.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAvailableMaps() {
        List<String> maps = new ArrayList<>();
        File[] files = new File(dataDir, "maps").listFiles();
        if (files == null) return maps;
        for (File f : files) {
            if (f.isFile()) maps.add(f.getName().replace(".json", ""));
        }
        return maps;
    }
}