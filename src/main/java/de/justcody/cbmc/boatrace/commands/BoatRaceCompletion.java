package de.justcody.cbmc.boatrace.commands;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.GameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoatRaceCompletion implements TabCompleter {

    private final List<String> completions = new ArrayList<>();
    private final File dataDir;


    public BoatRaceCompletion() {
        dataDir = new File(BoatRace.getPlugin().getDataFolder(), "maps");
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("game", "admin", "setup", "cup", "totem", "spawncar"), completions);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setup")) {
                complete(args[1], "<Name>");
            } else if (args[0].equalsIgnoreCase("game")) {
                complete(args[1], "join", "start");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("game")) {
                if (args[1].equalsIgnoreCase("create")) {
                    complete(args[2], getAvailableMaps());
                } else if (args[1].equalsIgnoreCase("join")) {
                    complete(args[2], List.of("RACE", "BATTLE"));
                }
            }
        }

        return completions;
    }


    private void complete(String arg, String... completions) {
        this.completions.clear();
        StringUtil.copyPartialMatches(arg, Arrays.asList(completions), this.completions);
    }
    private void complete(String arg, List<String> completions) {
        this.completions.clear();
        StringUtil.copyPartialMatches(arg, completions, this.completions);
    }
    private List<String> getAvailableMaps() {
        List<String> maps = new ArrayList<>();
        if (!dataDir.isDirectory()) return maps;
        for (File f : dataDir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".json")) {
                maps.add(f.getName().replace(".json", ""));
            }
        }
        return maps;
    }
}
