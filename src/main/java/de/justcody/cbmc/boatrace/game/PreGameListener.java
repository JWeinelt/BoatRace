package de.justcody.cbmc.boatrace.game;

import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.database.BoatRacePlayer;
import de.justcody.cbmc.boatrace.database.DataManager;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.geysermc.floodgate.api.FloodgateApi;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class PreGameListener implements Listener {
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getUniqueId().equals(UUID.fromString("014637d6-dbe7-4820-bf37-f37a60025a3c"))) {
            // This is the villager at start

        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (FloodgateApi.getInstance().isFloodgatePlayer(e.getPlayer().getUniqueId()) || e.getPlayer().getName().startsWith(".")) {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Component.text("§cThis game mode is only for Minecraft: Java Edition! Sorry!"));
        } else e.allow();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.teleport(new Location(Bukkit.getWorld("world"), 17.7, 33, -139, 0, 0));
        e.joinMessage(Component.empty());
        if (player.getProtocolVersion() <= 765)
            player.setResourcePack("http://node1.api.codeblocksmc.com:23363/download/BoatRace", hexStringToByteArray("d496d9bd88850f431ffbe60e78467d4729359413"),
                    Component.text("This game requires a resource pack to play."), true);
        else player.kick(Component.text("§cMinecraft Version 1.21.x is not supported."));
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent e) {
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
            genericJoin(e.getPlayer());
        } else if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) ||
                e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) ||
                e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_RELOAD) ||
                e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DISCARDED))
            e.getPlayer().kick(Component.text("You can't play without a resource pack!"));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DataManager.quitPlayer(e.getPlayer());

        e.quitMessage(Component.empty());
        e.getPlayer().clearResourcePacks();
    }

    @EventHandler
    public void onHotBarClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().getItemMeta() == null) return;
        if (e.getItem().getType().equals(Material.DIAMOND) && e.getItem().getItemMeta().getCustomModelData() == 50) { //Joining race mode
            PAFPlayer p = PAFPlayerManager.getInstance().getPlayer(e.getPlayer().getUniqueId()).getPAFPlayer();
            PlayerParty party = PartyManager.getInstance().getParty(p);
            if (party == null) {
                sendPlayerToRound(e.getPlayer());
            } else {
                if (party.isLeader(p)) {
                    for (PAFPlayer p2 : party.getAllPlayers()) {
                        Player player = Bukkit.getPlayer(p2.getUniqueId());
                        if (player == null) continue;
                        sendPlayerToRound(player);
                    }
                } else {
                    e.getPlayer().sendMessage(prefix+"§cYou can't join a game by your own while in a party.");
                }
            }
        } else if (e.getItem().getType().equals(Material.DIAMOND) && e.getItem().getItemMeta().getCustomModelData() == 51) { //Joining battle mode
            e.getPlayer().sendMessage(prefix+"§cWork in progress");
            return;
            /*e.getPlayer().sendTitle("", "\uE000", 10, 20, 10);
            e.getPlayer().getInventory().clear();
            new BukkitRunnable() {

                @Override
                public void run() {
                    BoatRace.getRaceManager().joinGame(e.getPlayer(), GameType.BATTLE);
                }
            }.runTaskLater(BoatRace.getPlugin(), 15);*/
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (LocUtil.getBlocksAround(player.getLocation()).contains(Material.CHERRY_BUTTON)) {
            player.getVehicle()
                    .setVelocity(player.getVehicle().getVelocity().add(new Vector(0,0.5,0)));
        }
    }

    private void sendPlayerToRound(Player player) {
        player.sendTitle("", "\uE000", 10, 20, 10);
        player.getInventory().clear();
        List<String> cups = BoatRace.getRaceManager().getCups();
        SecureRandom random = new SecureRandom();
        String selectedCup = cups.get(random.nextInt(cups.size()-1));
        BoatRace.getRaceManager().createNewGame(GameType.RACE, selectedCup);
        new BukkitRunnable() {
            @Override
            public void run() {
                BoatRace.getRaceManager().joinGame(player, GameType.RACE);
            }
        }.runTaskLater(BoatRace.getPlugin(), 15);
    }

    public void genericJoin(Player player) {

        DataManager.loadPlayer(player.getUniqueId());

        //player.teleport(new Location(Bukkit.getWorld("world"), -80.5, 33, -127.5, 0, 0));

        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND).customModelData(50).displayname("§a§lNormal Race").build());
        //player.getInventory().setItem(2, new ItemBuilder(Material.DIAMOND).customModelData(51).displayname("§a§lBattle Mode").build());
        player.getInventory().setItem(4, new ItemBuilder(Material.BUNDLE).displayname("§dDrivers & Cars").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.TOTEM_OF_UNDYING).customModelData(2).displayname("§6§lSeason: §c§oComing soon").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.TOTEM_OF_UNDYING).customModelData(1).displayname("§6§lStatistics").build());

        BoatRacePlayer bP = DataManager.getPlayer(player.getUniqueId());
        player.setLevel(bP.getLevel());
    }
}