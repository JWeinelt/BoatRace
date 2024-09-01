package de.justcody.cbmc.boatrace.commands;

import de.justcody.cbmc.boatrace.game.warps.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpCompleter implements TabCompleter {
    private final WarpManager warpManager;

    public WarpCompleter(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            complete(completions, args[0], "create", "edit");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                complete(completions, args[1], "<Name>");
            } else if (args[0].equalsIgnoreCase("edit")) {
                complete(completions, args[1], warpManager.getWarpNames());
            }
        } else if (args.length == 3) {
            complete(completions, args[2], "0,1,0", "0,0.3,0");
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
}
