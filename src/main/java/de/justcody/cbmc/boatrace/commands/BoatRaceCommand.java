package de.justcody.cbmc.boatrace.commands;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.CarManager;
import de.justcody.cbmc.boatrace.game.Game;
import de.justcody.cbmc.boatrace.game.GameType;
import de.justcody.cbmc.boatrace.game.map.SetupManager;
import de.justcody.cbmc.boatrace.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class BoatRaceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This command is player-only.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(prefix + "§cIncomplete command.");
            player.sendMessage(prefix + "§eRunning BoatRace Version " + BoatRace.getVersion());
            return false;
        }
        if (args[0].equalsIgnoreCase("spawncar")) {
            CarManager.createCar(player);
        } else if (args[0].equalsIgnoreCase("setup")) {
            // SETUP
            if (args.length == 2) {
                //TODO: Make option to continue setup
                BoatRace.setSetupManager(new SetupManager(args[1]));
                BoatRace.getPlugin().getServer().getPluginManager().registerEvents(BoatRace.getSetupManager(), BoatRace.getPlugin());
                BoatRace.getSetupManager().startSetup(player);
            }
        } else if (args[0].equalsIgnoreCase("cup")) {
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("add")) {
                    String name = args[2].replace("_", " ");
                    Material display = Material.getMaterial(args[3]);
                    if (display == null) {
                        player.sendMessage(prefix+"§cThe display item must be a valid item from list.");
                        return false;
                    }
                    BoatRace.getRaceManager().addCup(name, display);
                    BoatRace.getRaceManager().saveCups();
                    player.sendMessage(prefix+"§aSaved the cup §e"+name+"§a with display item §e"+display);
                }
            }
        } else if (args[0].equalsIgnoreCase("totem") && args.length == 2) {
            new EffectUtil().playTotemAnimation(player, Integer.parseInt(args[1]));
        } else if (args[0].equalsIgnoreCase("game")) {
            if (args[1].equalsIgnoreCase("join")) {
                BoatRace.getRaceManager().joinGame(player, GameType.RACE);
            } else if (args[1].equalsIgnoreCase("start")) {
                Game game = BoatRace.getRaceManager().getPlayerGame(player.getUniqueId());
                if (game == null) {
                    player.sendMessage(prefix+"§cYou are not in a game.");
                    return false;
                }
                game.prepareForStart();
            }
        }
        return false;
    }
}