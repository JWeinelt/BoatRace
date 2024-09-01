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

    private final File dataDir;


    public BoatRaceCompletion() {
        dataDir = new File(BoatRace.getPlugin().getDataFolder(), "maps");
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("game", "admin", "setup", "cup", "totem", "spawncar"), completions);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setup")) {
                complete(completions, args[1], "<Name>");
            } else if (args[0].equalsIgnoreCase("game")) {
                complete(completions, args[1], "join", "start");
            } else if (args[0].equalsIgnoreCase("admin")) {
                complete(completions, args[1], "cups", "settime");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("game")) {
                if (args[1].equalsIgnoreCase("create")) {
                    complete(completions, args[2], getAvailableMaps());
                } else if (args[1].equalsIgnoreCase("join")) {
                    complete(completions, args[2], BoatRace.getRaceManager().getCups());
                }
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (args[1].equalsIgnoreCase("settime")) {
                    complete(completions, args[2], BoatRace.getRaceManager().getAvailableMaps());
                }
            }
        }

        return completions;
    }


    private void complete(List<String> list, String arg, String... completions) {
        list.clear();
        StringUtil.copyPartialMatches(arg, Arrays.asList(completions), list);
    }
    private void complete(List<String> list, String arg, List<String> completions) {
        list.clear();
        StringUtil.copyPartialMatches(arg, completions, list);
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
