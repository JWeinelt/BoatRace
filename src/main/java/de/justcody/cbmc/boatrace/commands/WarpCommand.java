package de.justcody.cbmc.boatrace.commands;

import com.comphenix.protocol.PacketType;
import de.justcody.cbmc.boatrace.game.map.LocationSection;
import de.justcody.cbmc.boatrace.game.warps.Warp;
import de.justcody.cbmc.boatrace.game.warps.WarpManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static de.justcody.cbmc.boatrace.BoatRace.getPlugin;
import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class WarpCommand implements CommandExecutor {
    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((sender instanceof Player player)) {

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("wands")) {
                    player.getInventory().setItem(0, warpManager.getWand());
                    player.getInventory().setItem(1, warpManager.getWand2());
                }
            }

            if (args.length != 3) {
                sender.sendMessage(prefix + "§cUsage: /warp <create|edit|wands> <Name> <Vector>");
                return false;
            }


            if (args[0].equalsIgnoreCase("create")) {
                String name = args[1];
                Vector vector = createVecFromString(args[2]);
                if (warpManager.getPos1(player.getUniqueId()) == null ||
                        warpManager.getPos2(player.getUniqueId()) == null ||
                        warpManager.getPos3(player.getUniqueId()) == null ||
                        warpManager.getPos4(player.getUniqueId()) == null) {
                    player.sendMessage(prefix + "§cYou need to select both trigger point and end point.");
                    return false;
                }
                Warp warp = new Warp(new LocationSection(warpManager.getPos1(player.getUniqueId()),
                        warpManager.getPos2(player.getUniqueId())), vector, new LocationSection(warpManager.getPos3(player.getUniqueId()),
                        warpManager.getPos4(player.getUniqueId())), name);
                warpManager.createWarpEntry(warp);
                player.sendMessage(prefix+"§bA new warp with name §d"+name+"§b has been created.");
                player.getInventory().clear();

            }
        }
        return false;
    }


    public Vector createVecFromString(String input) {
        double x = Double.parseDouble(input.split(",")[0]);
        double y = Double.parseDouble(input.split(",")[1]);
        double z = Double.parseDouble(input.split(",")[2]);
        return new Vector(x, y, z);
    }
}
