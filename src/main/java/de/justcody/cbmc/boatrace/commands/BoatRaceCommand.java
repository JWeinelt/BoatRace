package de.justcody.cbmc.boatrace.commands;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.CarManager;
import de.justcody.cbmc.boatrace.game.Game;
import de.justcody.cbmc.boatrace.game.GameType;
import de.justcody.cbmc.boatrace.game.map.SetupManager;
import de.justcody.cbmc.boatrace.util.EffectUtil;
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
        if (label.equalsIgnoreCase("boatrace")) {
            if (!player.hasPermission("boatrace.command")) {
                player.sendMessage(prefix + "§cYou don't have the permission to use this command.");
                return false;
            }

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
                    if (BoatRace.getRaceManager().getAvailableMaps().contains(args[1])) {
                        // Map already exists; must be continued.
                        BoatRace.setSetupManager(new SetupManager(args[1], true));
                        BoatRace.getPlugin().getServer().getPluginManager().registerEvents(BoatRace.getSetupManager(), BoatRace.getPlugin());
                        BoatRace.getSetupManager().startSetup(player);
                        BoatRace.getSetupManager().teleportPlayerToMap(player);
                        player.sendMessage(prefix+"§bContinuing setup for §d"+args[1]);
                    } else {
                        BoatRace.setSetupManager(new SetupManager(args[1], false));
                        BoatRace.getPlugin().getServer().getPluginManager().registerEvents(BoatRace.getSetupManager(), BoatRace.getPlugin());
                        BoatRace.getSetupManager().startSetup(player);
                    }
                }
            } else if (args[0].equalsIgnoreCase("cup")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("add")) {
                        BoatRace.getCupManager().createInstance(player);
                    }
                }
            } else if (args[0].equalsIgnoreCase("totem") && args.length == 2) {
                new EffectUtil().playTotemAnimation(player, Integer.parseInt(args[1]));
            } else if (args[0].equalsIgnoreCase("game")) {
                if (args.length == 1) {
                    player.sendMessage(prefix + "§cIncomplete command.");
                    return false;
                }
                if (args[1].equalsIgnoreCase("join")) {
                    //BoatRace.getRaceManager().joinGame(player, GameType.RACE);
                    if (args.length == 2) {
                        player.sendMessage(prefix + "§cPlease specify a cup.");
                        return false;
                    }
                    BoatRace.getRaceManager().createNewGame(GameType.RACE, args[2]);
                    BoatRace.getRaceManager().joinGame(player, GameType.RACE);
                } else if (args[1].equalsIgnoreCase("joinmap")) {
                    BoatRace.getRaceManager().createNewGameMap(GameType.RACE, args[2]);
                    BoatRace.getRaceManager().joinGame(player, GameType.RACE);
                } else if (args[1].equalsIgnoreCase("start")) {
                    Game game = BoatRace.getRaceManager().getPlayerGame(player.getUniqueId());
                    if (game == null) {
                        player.sendMessage(prefix + "§cYou are not in a game.");
                        return false;
                    }
                    game.prepareForStart();
                }
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("cups")) {
                        player.sendMessage(prefix + "§cWork in progress");
                    }
                } else if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("settime")) {
                        long time = player.getLocation().getWorld().getTime();
                        BoatRace.getRaceManager().setTimeZone(args[2], time);
                        BoatRace.getRaceManager().saveTimeZones();
                        player.sendMessage(prefix+"§bThe time for map §e"+args[2]+"§b has been set to §e"+time);
                    }
                }
            }
        }
        return false;
    }
}