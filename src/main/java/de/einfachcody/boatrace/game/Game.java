package de.einfachcody.boatrace.game;

import de.einfachcody.boatrace.BoatRace;
import de.einfachcody.boatrace.game.arena.Arena;
import de.einfachcody.boatrace.game.arena.ArenaManager;
import de.einfachcody.boatrace.game.cups.Course;
import de.einfachcody.boatrace.game.cups.Cup;
import de.einfachcody.boatrace.util.translation.TranslationFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private ArenaManager arenaManager = new ArenaManager();
    private String gameID = "";
    private boolean isRunning = false;
    private HashMap<RacePlayer, Integer> points = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private Cup cup;
    private Course currentCourse;
    private Arena arena;
    private int currentTrack = 1;

    private Course firstCourse;
    private Course secondCourse;
    private Course thirdCourse;
    private Course forthCourse;

    private HashMap<Cup, Integer> votes = new HashMap<>();

    private Arena currentArena;


    // TASKS DURING GAME
    private BukkitTask timerTask;
    private BukkitTask songTask;
    private BukkitTask playersTask;
    private BukkitTask courseTask;


    public void submitVote(Cup cup) {
        votes.put(cup, votes.getOrDefault(cup, 0) + 1);
    }

    public void endVoting() {
        Cup winner = Cup.DIAMOND; // DEFAULT VALUE
        int highest = 0;
        for (Cup c : votes.keySet()) {
            if (votes.get(c) > highest) {
                highest = votes.get(c);
                winner = c;
            }
        }
        cup = winner;
    }

    public void handleGameJoin(Player player) {
        if (players.size() == 6) {
            player.sendMessage(BoatRace.prefix + "§a This game is full.");
        } else {
            player.setPlayerListHeader(BoatRace.prefix + "\n§8§m================\n§aYou are in Game §e" + gameID
                    + "\n§aPlayers: §e" + players.size() + "/6\n§aGame Status: §eIn Game");
            players.add(player);
            for (Player p : players) p.sendMessage("§7[§aBoatRace§7] §e"
                    + player.getDisplayName().replace(".", "") + " §ajoined the game.");
        }
    }

    public void handleGameLeave(Player player) {}

    public void startGame() {
        currentCourse = cup.firstTrack;
        currentArena = arenaManager.loadArena(currentCourse.courseID);
        for (int i = 0; i < players.size(); i++) {
            preparePlayer(currentArena.getStartLocation(i), players.get(i));
        }

        // Start Game
        new BukkitRunnable() {

            String courseName = TranslationFileManager.configurations.get("en").getString("courses." + currentCourse.courseID + ".name");
            String builtBy = TranslationFileManager.configurations.get("en").getString("courses." + currentCourse.courseID + ".built-by");
            @Override
            public void run() {
                for (Player p : players) {
                    p.sendTitle("§a" + courseName, "§e" + builtBy);
                }
            }
        }.runTaskLater(BoatRace.getPlugin(), 20);

        new BukkitRunnable() {
            int timer = 3;
            @Override
            public void run() {
                for (Player p : players) {
                    p.sendTitle("§e" + timer, "");
                }
                timer--;
            }
        }.runTaskTimer(BoatRace.getPlugin(), 80, 20);
    }


    public void preparePlayer(Location loc, Player p) {
        p.sendMessage(BoatRace.prefix + "§ePrepare for Race!");
        p.teleport(loc);
        RacePlayer player = new RacePlayer(p);
        Boat boat = (Boat) loc.getWorld().spawnEntity(loc, EntityType.BOAT);
        boat.setBoatType(Boat.Type.OAK);
        boat.addPassenger(p);
        Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        zombie.setInvisible(true);
        zombie.setAI(false);
        zombie.setShouldBurnInDay(false);
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.LAPIS_LAZULI));
        boat.addPassenger(zombie);
        player.setZombie(zombie);
        player.setBoat(boat);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
