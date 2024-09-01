package de.justcody.cbmc.boatrace.game;

import com.destroystokyo.paper.ClientOption;
import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.database.DataManager;
import de.justcody.cbmc.boatrace.game.map.LocationSection;
import de.justcody.cbmc.boatrace.game.map.Map;
import de.justcody.cbmc.boatrace.game.powerup.*;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import de.justcody.cbmc.translationsystem.TranslationAPI;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class Game implements Listener {

    private final TranslationAPI api = TranslationAPI.getInstance();

    private final String prefix = BoatRace.prefix;
    @Getter
    private final GameType type;
    @Getter
    private final List<UUID> players = new ArrayList<>();
    @Getter
    @Setter
    private String cup;
    private final Map map;
    @Getter
    private final List<Location> itemBoxes = new ArrayList<>();
    private final List<UUID> boxes = new ArrayList<>();
    private GameState state;
    private final int reservedLobbyID;

    private BukkitTask musicTask;

    private final HashMap<UUID, Integer> rounds = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> points = new HashMap<>();
    private final HashMap<UUID, Integer> isAtCheckpoint = new HashMap<>();
    private final List<UUID> playersResetting = new ArrayList<>();
    private final List<UUID> finished = new ArrayList<>();

    private final TranslationAPI tapi;

    private final Game game;

    private boolean prepareState = false;


    public Game(GameType type, String mapName) {
        this.type = type;
        map = BoatRace.loadMap(mapName);
        if (!(map == null))
            if (!(map.getCheckpoints() == null))
                BoatRace.getLog().info("Found " + map.getCheckpoints().size() + " checkpoints in this map.");
        state = GameState.WAITING;
        reservedLobbyID = BoatRace.getPreGameLobbyManager().reserveLobby();
        BoatRace.getPlugin().getServer().getPluginManager().registerEvents(this, BoatRace.getPlugin());
        tapi = TranslationAPI.getInstance();
        game = this;
    }

    public Game(Game game2, String mapName, HashMap<UUID, Integer> points) {
        this.type = game2.getType();
        map = BoatRace.loadMap(mapName);
        state = GameState.STARTING;
        tapi = TranslationAPI.getInstance();
        this.game = this;
        players.addAll(game2.getPlayers());
        itemBoxes.addAll(game2.getItemBoxes());
        BoatRace.getPlugin().getServer().getPluginManager().registerEvents(this, BoatRace.getPlugin());
        reservedLobbyID = -1;
        cup = game2.getCup();
        this.points = points;
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
        executeForPlayers(player -> player.sendMessage(api.translate(player.getClientOption(ClientOption.LOCALE).split("_")[0], "games.generic.join")
                .replace("%player%", PlainTextComponentSerializer.plainText().serialize(p.displayName())).replace("%prefix%", prefix)));
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
        long time = BoatRace.getRaceManager().getTime(map.getMapName());
        BoatRace.getPreGameLobbyManager().freeWaitingLobby(reservedLobbyID);
        state = GameState.STARTING;
        open();
        executeForPlayers(player -> {
            player.setPlayerTime(time, false);
            String lang = player.getClientOption(ClientOption.LOCALE).split("_")[0];
            player.sendMessage("\n§x§0§8§4§C§F§B§l§nB§x§0§7§6§6§E§A§l§no§x§0§6§7§F§D§9" +
                    "§l§na§x§0§5§9§9§C§8§l§nt§x§0§3§B§2§B§6§l§nR§x§0§2§C§C§A§5§l§na§x§0§1§E§5" +
                    "§9§4§l§nc§x§0§0§F§F§8§3§l§ne");
            player.sendMessage(tapi.translate(lang, "boatrace.start.prepare.1"));
            player.sendMessage(tapi.translate(lang, "boatrace.start.prepare.2"));
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
                prepareState = true;
                executeForPlayers(player -> {
                    player.teleport(LocUtil.fromWrapper(map.getStartingPlaces().get(i)));
                    CarManager.createCar(player);
                    CarManager.getCar(player.getUniqueId()).prepareForStart();
                    i++;
                });
            }
        }.runTaskLater(BoatRace.getPlugin(), 3*4*20+50);

        new BukkitRunnable() {
            @Override
            public void run() {
                startGame();
            }
        }.runTaskLater(BoatRace.getPlugin(), 3*4*20+100);
    }

    public void startGame() {
        executeForPlayers(player -> player.playSound(player, "boatrace:mk.race.start", 1, 1));
        if (map == null) return;
        new BukkitRunnable() {
            int sec = 3;
            @Override
            public void run() {
                switch (sec) {
                    case 3 -> executeForPlayers(player -> player.sendTitle("§c3", "", 5, 10, 5));
                    case 2 -> executeForPlayers(player -> player.sendTitle("§62", "", 5, 10, 5));
                    case 1 -> executeForPlayers(player -> player.sendTitle("§a1", "", 5, 10, 5));
                    case 0 -> {
                        prepareState = false;
                        executeForPlayers(player -> {
                            CarManager.getCar(player.getUniqueId()).startRace();
                            player.sendTitle(
                                    tapi.translate(player.getClientOption(ClientOption.LOCALE).split("_")[0], "boatrace.start.go"),
                                    "", 5, 10, 5);
                            //player.playSound(player, map.getMusic(), 1, 1);
                            startMusicLoop();
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

    public void startMusicLoop() {
        int loop = 200;
        switch (map.getMusic().replace("mk.music.", "")) {
            case "beach" -> loop = 77;
            case "bowser" -> loop = 170;
            case "circuit" -> loop = 70;
            case "city" -> loop = 194;
            case "desert" -> loop = 76;
            case "desert2" -> loop = 131;
            case "rainbow" -> loop = 106;
            case "summit" -> loop = 110;
            case "treeway" -> loop = 150;
        }
        musicTask = new BukkitRunnable() {

            @Override
            public void run() {
                executeForPlayers(p->p.playSound(p, map.getMusic(), 1, 1));
            }
        }.runTaskTimer(BoatRace.getPlugin(), 0, loop*20);
    }

    public List<String> createGameSummary() {
        List<String> results = new ArrayList<>();

        int place = 0;

        for (UUID u : finished) {
            place++;
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;
            DataManager.getPlayer(p.getUniqueId()).addXP(15);
            points.put(p.getUniqueId(), points.getOrDefault(p.getUniqueId(), 0)+determinePoints(place));
            results.add("§6" + place+". §b" + PlainTextComponentSerializer.plainText().serialize(p.displayName())
                    + " §e[" + points.getOrDefault(p.getUniqueId(), 0) + "]");
        }

        for (int i = 0; i < 6 - place; i++) {
            results.add("§6" + (5 - place+ i) + ". §b§m==========");
        }

        return results;
    }

    public int determinePoints(int place) {
        switch (place) {
            case 1 -> {
                return 15;
            }
            case 2 -> {
                return 12;
            }
            case 3 -> {
                return 10;
            }
            case 4 -> {
                return 7;
            }
            case 5 -> {
                return 5;
            }
            case 6 -> {
                return 1;
            }
        }
        return 0;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (LocUtil.getBlocksAround(player.getLocation()).contains(Material.STONE)) player.getVehicle()
                .setVelocity(player.getVehicle().getVelocity().add(new Vector(0,1.5,0)));

        if (state.equals(GameState.RUNNING)) {
            if (!players.contains(player.getUniqueId())) return;
        }

        if (LocUtil.isInArea(player.getLocation(), map.getStartLine())) {
            if (isAtCheckpoint.getOrDefault(player.getUniqueId(), -1) != -1) {
                isAtCheckpoint.put(player.getUniqueId(), -1);
                int nextLap = rounds.getOrDefault(player.getUniqueId(), 1)+1;
                player.sendMessage("§eDEBUG: §7Lap: " + nextLap);
                switch (nextLap) {
                    case 2 -> {
                        player.playSound(player, "boatrace:mk.race.next_lap", 1, 1);
                        player.sendTitle("§eLap 2", "", 10, 70, 20);
                    }
                    case 3 -> {
                        musicTask.cancel();
                        player.stopAllSounds();
                        player.playSound(player, "boatrace:mk.race.final_lap", 1, 1);
                        player.sendTitle("§eFinal Lap", "", 10, 70, 20);
                        player.playSound(player, map.getMusic(), 1, 1.2F);
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


                        if (!finished.contains(player.getUniqueId())) {
                            finished.add(player.getUniqueId());
                            if (finished.size() == players.size()) {
                                List<String> sum = createGameSummary();
                                // All players are done racing
                                executeForPlayers(p -> {
                                    p.sendMessage(tapi.translate(player.getClientOption(ClientOption.LOCALE).split("_")[0], "boatrace.race.summary"));
                                    for (String s : sum) p.sendMessage(s);
                                    CarManager.delete(p.getUniqueId());
                                });
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        executeForPlayers(Player::stopAllSounds);
                                        boxes.forEach(u -> {
                                            Entity e = Bukkit.getEntity(u);
                                            if (e != null)
                                                e.remove();
                                        });
                                        BoatRace.getRaceManager().continueGame(game);
                                    }
                                }.runTaskLater(BoatRace.getPlugin(), 20 * 5);

                            }
                        }
                    }
                }
                rounds.put(player.getUniqueId(), nextLap);
            }
        }

        int j = 0;
        for (LocationSection c : map.getCheckpoints()) {
            if (LocUtil.isInArea(player.getLocation(), c) && isAtCheckpoint.getOrDefault(player.getUniqueId(), -1) != j) {
                isAtCheckpoint.put(player.getUniqueId(), j);

                //player.sendMessage("Checkpoint: " + j);
                break;
            }
            j++;
        }

        /*for (int i = 0; i < map.getCheckpoints().size(); i++) {
            if (LocUtil.isInArea(player.getLocation(), map.getCheckpoints().get(i)) && !(isAtCheckpoint.getOrDefault(player.getUniqueId(), -1) == -1)) {
                isAtCheckpoint.put(player.getUniqueId(), i);

                player.sendMessage("Checkpoint: " + i);
            }
        }*/

        for (Location l : itemBoxes) {
            if (l.getBlockX() == player.getLocation().getBlockX() &&
                    l.getBlockY() == player.getLocation().getBlockY() &&
                    l.getBlockZ() == player.getLocation().getBlockZ()) {
                if (!map.getMapName().contains("Rainbow"))
                    player.playSound(player.getLocation(), "boatrace:mk.race.item_roulette", 1, 1);
                else
                    player.playSound(player.getLocation(), "boatrace:mk.race.item_roulette_rainbow", 1, 1);

                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (i >= 26) cancel();
                        i++;
                        PowerUpItem item = getRandomItem();
                        for (int j = 0; j < 9; j++) {
                            player.getInventory().setItem(j, new ItemBuilder(Material.DIAMOND).customModelData(item.modelData).displayname("§e§l???").lore("§e???§b ITEM BOX §e???").build());
                        }
                    }
                }.runTaskTimerAsynchronously(BoatRace.getPlugin(), 0, 2);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        PowerUpItem item = getRandomItem();
                        Audience.audience(player).sendActionBar(Component.text("§bUse §d").append(Component.keybind("key.swapOffhand"))
                                .append(Component.text("§b to use it!")));
                        for (int j = 0; j < 9; j++) {
                            player.getInventory().setItem(j, new ItemBuilder(Material.DIAMOND).customModelData(item.modelData).displayname(item.toString()).lore("§e???§b ITEM BOX §e???").build());
                        }
                    }
                }.runTaskLater(BoatRace.getPlugin(), 2*28);
            }
        }

        for (LocationSection lS : map.getDeathAreas())
            if (LocUtil.isInArea(e.getPlayer().getLocation(), lS) && !playersResetting.contains(player.getUniqueId())) {
                player.sendTitle("\uE000", "", 10, 20, 10);
                playersResetting.add(player.getUniqueId());
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        CarManager.delete(player.getUniqueId());
                        player.teleport(LocUtil.fromWrapper(LocUtil
                                .getMiddle(map.getCheckpoints()
                                        .get(isAtCheckpoint.getOrDefault(player.getUniqueId(), 0)))));
                        CarManager.createCar(player);
                        playersResetting.remove(player.getUniqueId());
                        player.getInventory().clear();
                    }
                }.runTaskLater(BoatRace.getPlugin(), 20);
            }


        if (map.getCustomLap2() == null) return;
        if (LocUtil.isInArea(player.getLocation(), map.getCustomLap2()) && rounds.getOrDefault(player.getUniqueId(), 1) == 1) {
            rounds.put(player.getUniqueId(), 2);

            player.playSound(player, "boatrace:mk.race.next_lap", 1, 1);
            player.sendTitle("§eLap 2", "", 10, 70, 20);
        }

        if (map.getCustomLap3() == null) return;
        if (LocUtil.isInArea(player.getLocation(), map.getCustomLap3()) && rounds.getOrDefault(player.getUniqueId(), 1) == 2) {
            rounds.put(player.getUniqueId(), 3);
            player.stopAllSounds();
            player.playSound(player, "boatrace:mk.race.final_lap", 1, 1);
            player.sendTitle("§eFinal Lap", "", 10, 70, 20);
            player.playSound(player, map.getMusic(), 1, 1.2F);
        }


        if (map.getCustomEnd() == null) return;
        if (LocUtil.isInArea(player.getLocation(), map.getCustomEnd())) {
            if (!finished.contains(player.getUniqueId())) {

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
                    // All players are done racing
                    executeForPlayers(p -> {
                        p.stopAllSounds();
                        p.sendMessage(tapi.translate(player.getClientOption(ClientOption.LOCALE).split("_")[0], "boatrace.race.summary"));
                        for (String s : sum) p.sendMessage(s);
                        CarManager.delete(p.getUniqueId());
                    });
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            executeForPlayers(Player::stopAllSounds);
                            boxes.forEach(u -> {
                                Entity e = Bukkit.getEntity(u);
                                if (e != null)
                                    e.remove();
                            });
                            BoatRace.getRaceManager().continueGame(game);
                        }
                    }.runTaskLater(BoatRace.getPlugin(), 20 * 5);

                }
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (e.getOffHandItem().getType().equals(Material.DIAMOND)) {
            if (e.getOffHandItem().getItemMeta() == null) return;
            switch (e.getOffHandItem().getItemMeta().getCustomModelData()) {
                case 30 -> { // Red shell
                    UUID target = null;
                    for (Entity en : e.getPlayer().getNearbyEntities(20, 10, 20)) {
                        if (en instanceof Player p) {
                            if (p.getUniqueId().equals(e.getPlayer().getUniqueId())) continue;
                            target = p.getUniqueId();
                            break;
                        }
                    }
                    if (target == null) target = e.getPlayer().getUniqueId();
                    RedShell shell = new RedShell(e.getPlayer().getUniqueId())
                            .setTarget(target);
                    shell.spawn(e.getPlayer().getLocation());
                }
                case 31 -> { // Green shell
                    GreenShell shell = new GreenShell(e.getPlayer().getUniqueId());
                    shell.spawn(e.getPlayer().getLocation());
                }
                case 32 -> { // Blue shell
                    UUID target = null;
                    for (Entity en : e.getPlayer().getNearbyEntities(20, 10, 20)) {
                        if (en instanceof Player p) {
                            if (p.getUniqueId().equals(e.getPlayer().getUniqueId())) continue;
                            target = p.getUniqueId();
                            break;
                        }
                    }
                    if (target == null) target = e.getPlayer().getUniqueId();
                    BlueShell shell = new BlueShell(e.getPlayer().getUniqueId())
                            .setTarget(target);
                    shell.spawn(e.getPlayer().getLocation());
                }
                case 33 -> // Banana
                        new BukkitRunnable() {
                            final Location location = e.getPlayer().getLocation();
                            @Override
                            public void run() {
                                Banana banana = new Banana(e.getPlayer().getUniqueId());
                                banana.spawn(location.add(0, 1, 0));
                            }
                        }.runTaskLater(BoatRace.getPlugin(), 20);
                case 34 -> // Bomb
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bomb bomb = new Bomb(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
                                bomb.spawn(e.getPlayer().getLocation().add(0, 1, 0));
                            }
                        }.runTaskLater(BoatRace.getPlugin(), 20);
                case 35 -> { // Thunder
                    e.getPlayer().sendTitle("\uE004", "", 0, 20, 10);
                    e.getPlayer().playSound(e.getPlayer(), "boatrace:mk.item.thunder_use", 1, 1);
                    executeForPlayers(p->{
                        Car car = CarManager.getCar(p.getUniqueId());
                        if (car == null) return;
                        car.makeLightningStruck(true);
                        SecureRandom random = new SecureRandom();
                        int seconds = random.nextInt(4, 10);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                Car car = CarManager.getCar(p.getUniqueId());
                                if (car == null) return;
                                car.makeLightningStruck(false);
                            }
                        }.runTaskLater(BoatRace.getPlugin(), 20L * seconds);
                    });
                }
                case 36 -> // Blooper / Squid
                        executeForPlayers(p->{
                            if (!p.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1, false, false, false));
                                p.playSound(p, Sound.ENTITY_SQUID_HURT, 1, 1);
                            }
                        });
            }
            for (int i = 0; i < 9; i++) e.getPlayer().getInventory().setItem(i, null); // Clear hotbar
            e.setCancelled(true);
            e.setOffHandItem(null);
            e.setMainHandItem(null);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent e) {
        if (prepareState) e.getVehicle().teleport(e.getFrom());
    }

    @EventHandler
    public void onBoatDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Boat) e.setCancelled(true);
    }


    public String getMapName() {
        return map.getMapName();
    }


    public PowerUpItem getRandomItem() {
        List<PowerUpItem> items = Arrays.asList(PowerUpItem.values());
        double totalProbability = items.stream().mapToDouble(PowerUpItem::getProbability).sum();

        // Generate a random number between 0 and the total probability
        double randomValue = new Random().nextDouble() * totalProbability;

        double cumulativeProbability = 0.0;
        for (PowerUpItem item : items) {
            cumulativeProbability += item.getProbability();
            if (randomValue <= cumulativeProbability) {
                return item;
            }
        }

        // This point should theoretically never be reached if probabilities are correct
        return PowerUpItem.BANANA;
    }


    public enum PowerUpItem {
        RED_SHELL(30, 0.5),
        GREEN_SHELL(31, 0.7),
        BANANA(33, 0.7),
        BOMB(34, 0.4),
        THUNDER(35, 0.3),
        BLOOPER(36, 0.3),
        BLUE_SHELL(32, 0.2);

        public final int modelData;
        public final double random;

        PowerUpItem(int modelData, double random) {
            this.modelData = modelData;
            this.random = random;
        }

        public double getProbability() {
            return random;
        }
    }
}