package de.justcody.cbmc.boatrace;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.justcody.cbmc.boatrace.commands.BoatRaceCommand;
import de.justcody.cbmc.boatrace.commands.BoatRaceCompletion;
import de.justcody.cbmc.boatrace.game.CarManager;
import de.justcody.cbmc.boatrace.game.PreGameListener;
import de.justcody.cbmc.boatrace.game.PreGameLobbyManager;
import de.justcody.cbmc.boatrace.game.RaceManager;
import de.justcody.cbmc.boatrace.game.map.Map;
import de.justcody.cbmc.boatrace.game.map.SetupManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

public final class BoatRace extends JavaPlugin {
    public static final String prefix = "§x§f§b§5§f§5§f§lB§x§f§3§7§5§7§6§lo§x§e§a§8§a§8§c§la§x§e§2§a§0§a§3§lt§x§d§9§b§" +
            "5§b§9§lR§x§d§1§c§b§d§0§la§x§c§8§e§0§e§6§lc§x§c§0§f§6§f§d§le §7• ";

    @Getter
    private static BoatRace plugin;
    @Getter
    @Setter
    private static SetupManager setupManager;
    @Getter
    @Setter
    private static RaceManager raceManager;
    @Getter
    @Setter
    private static PreGameLobbyManager preGameLobbyManager;
    private Logger log;
    private static final Gson GSON =  new GsonBuilder().setPrettyPrinting().create();


    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        log = getLogger(); // Initialize logger

        sendLogo();
        log.info("Welcome!");
        log.info("Initializing BoatRace...");
        log.info("Creating class instances...");

        raceManager = new RaceManager();
        log.info("Done!");

        log.info("Starting connection to MySQL database...");
        //TODO: Make MySQL Stuff
        log.info("Done!");

        log.info("Loading cup data...");
        raceManager.loadCups();
        log.info("Done! Continuing with the commands...");
        registerCommands();
        log.info("Commands registered.");

        preGameLobbyManager = new PreGameLobbyManager();

        getServer().getPluginManager().registerEvents(new PreGameListener(), this);
    }

    @Override
    public void onDisable() {
        sendLogo();
        log.info("Goodbye!");
        CarManager.deleteAll();
    }

    private void registerCommands() {
        PluginCommand mainCmd = getCommand("boatrace");
        if (mainCmd == null) return;
        mainCmd.setExecutor(new BoatRaceCommand());
        mainCmd.setTabCompleter(new BoatRaceCompletion());
    }


    public static Map loadMap(String map) {
        if (!(new File(getPlugin().getDataFolder(), "maps/" + map + ".json").exists())) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(getPlugin().getDataFolder(), "maps/" + map + ".json")))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            Type mapType = new TypeToken<Map>(){}.getType();
            return GSON.fromJson(jsonStringBuilder.toString(), mapType);
        } catch (IOException e) {
            getPlugin().getLogger().warning(e.getMessage());
            return null;
        }
    }



    public static String getVersion() {
        return "v2.0.0.0";
    }

    private void sendLogo() {
        log.info("""

                  ____              _   _____                       ___ \s
                 |  _ \\            | | |  __ \\                     |__ \\\s
                 | |_) | ___   __ _| |_| |__) |__ _  ___ ___  __   __ ) |
                 |  _ < / _ \\ / _` | __|  _  // _` |/ __/ _ \\ \\ \\ / // /\s
                 | |_) | (_) | (_| | |_| | \\ \\ (_| | (_|  __/  \\ V // /_\s
                 |____/ \\___/ \\__,_|\\__|_|  \\_\\__,_|\\___\\___|   \\_/|____|
                                                                        \s
                                                                         \
                """);
    }
}