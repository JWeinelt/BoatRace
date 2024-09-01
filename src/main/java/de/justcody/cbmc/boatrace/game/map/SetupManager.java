package de.justcody.cbmc.boatrace.game.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.codeblocksmc.codelib.GuiBuilder;
import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.GameType;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class SetupManager implements Listener {
    private final Gson GSON;
    private final File dataDir;


    @Getter
    private final Map map;
    private final ItemStack battleMode = new ItemBuilder(Material.LIME_DYE).displayname("§6Current mode: §aBattle Mode").build();
    private final ItemStack raceMode = new ItemBuilder(Material.ORANGE_DYE).displayname("§6Current mode: §aRace Mode").build();
    private final ItemStack itemBox = new ItemBuilder(Material.END_CRYSTAL).displayname("§aItem Box").build();
    private final ItemStack wand = new ItemBuilder(Material.IRON_AXE).displayname("§aLocation Wand").build();
    private final ItemStack book = new ItemBuilder(Material.BOOK).displayname("§aSetup GUI").build();

    private final List<UUID> itemBoxes = new ArrayList<>();

    @Getter
    private Location pos1;
    @Getter
    private Location pos2;

    private final HashMap<UUID, Integer> selectedPaths = new HashMap<>();
    private final List<LocationSection> deathAreas = new ArrayList<>();

    private CameraIntroPath path1;
    private CameraIntroPath path2;
    private CameraIntroPath path3;


    public SetupManager(String name, boolean exists) {
        if (!exists) {
            map = new Map();
            map.setMapName(name);

            GSON = new GsonBuilder().setPrettyPrinting().create();
            dataDir = new File(BoatRace.getPlugin().getDataFolder(), "maps");
            if (dataDir.mkdirs()) BoatRace.getPlugin().getLogger().info("Creating needed folders");
        } else {
            map = BoatRace.loadMap(name);
            GSON = new GsonBuilder().setPrettyPrinting().create();
            dataDir = new File(BoatRace.getPlugin().getDataFolder(), "maps");
        }
    }

    public void teleportPlayerToMap(Player player) {
        player.teleport(LocUtil.fromWrapper(map.getStartingPlaces().get(0)));
    }


    public void startSetup(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(8, raceMode);
        player.getInventory().setItem(7, itemBox);
        player.getInventory().setItem(5, wand);
        player.getInventory().setItem(0, book);
    }


    public void saveMap() {
        map.setDeathAreas(deathAreas);
        if (dataDir.mkdirs()) BoatRace.getPlugin().getLogger().info("Creating needed folders");
        String json = GSON.toJson(map);

        try (FileWriter writer = new FileWriter(new File(dataDir, map.getMapName()+".json"))) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            BoatRace.getPlugin().getLogger().warning(e.getMessage());
        }
    }

    public void abort() {
        for (UUID u : itemBoxes) {
            Entity en = Bukkit.getEntity(u);
            if (en == null) continue;
            en.remove();
        }
        for (Player p : Bukkit.getOnlinePlayers()) p.getInventory().clear();
        HandlerList.unregisterAll(this);
    }




    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().equals(battleMode)) {
            e.getPlayer().getInventory().setItem(8, raceMode);
            e.getPlayer().sendMessage(prefix + "§aMap mode changed to: §eRace Mode");
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
            map.setGameType(GameType.RACE);

        } else if (e.getItem().equals(raceMode)){
            e.getPlayer().getInventory().setItem(8, battleMode);
            e.getPlayer().sendMessage(prefix + "§aMap mode changed to: §eBattle Mode");
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
            map.setGameType(GameType.BATTLE);

        } else if (e.getItem().equals(itemBox)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock() == null) return;
                map.addItemBox(e.getClickedBlock().getLocation().add(new Vector(0,1,0)));
                e.getPlayer().sendMessage(prefix+"§aA new item box has been added.");

                EnderCrystal crystal = e.getPlayer().getWorld().spawn(e.getClickedBlock().getLocation().add(new
                        Vector(0,1,0)), EnderCrystal.class);
                itemBoxes.add(crystal.getUniqueId());

            }
        } else if (e.getItem().equals(wand)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                pos2 = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(prefix+"§eSet the second postion");
            }
            if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                pos1 = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(prefix+"§eSet the first postion");
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock() == null) return;
                pos2 = e.getClickedBlock().getLocation();
                e.getPlayer().sendMessage(prefix+"§eSet the second postion");
                e.setCancelled(true);
            }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (e.getClickedBlock() == null) return;
                pos1 = e.getClickedBlock().getLocation();
                e.getPlayer().sendMessage(prefix+"§eSet the first postion");
                e.setCancelled(true);
            }
        } else if (e.getItem().equals(book)) {
            e.getPlayer().openInventory(getMainGUI());
        } else if (e.getItem().getType().equals(Material.ENDER_CHEST)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix+"§aSet end position of the path.");
                switch (selectedPaths.getOrDefault(e.getPlayer().getUniqueId(), 1)) {
                    case 1 -> {
                        path1.setEnPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                    case 2 -> {
                        path2.setEnPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                    case 3 -> {
                        path3.setEnPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                }
            } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix+"§aSet start position of the path.");
                switch (selectedPaths.getOrDefault(e.getPlayer().getUniqueId(), 1)) {
                    case 1 -> {
                        path1.setStartPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                    case 2 -> {
                        path2.setStartPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                    case 3 -> {
                        path3.setStartPos(LocUtil.fromBukkit(e.getPlayer().getLocation()));
                    }
                }
            }

            switch (selectedPaths.getOrDefault(e.getPlayer().getUniqueId(), 1)) {
                case 1 -> {
                    if (path1.getEnPos() != null && path1.getStartPos() != null) {
                        selectedPaths.remove(e.getPlayer().getUniqueId());
                        e.getPlayer().openInventory(getPathsMenu());
                        e.getPlayer().sendMessage(prefix+"§ePath 1 is now§a completed.");
                        e.getPlayer().getInventory().clear(2);
                    }
                }
                case 2 -> {
                    if (path2.getEnPos() != null && path2.getStartPos() != null) {
                        selectedPaths.remove(e.getPlayer().getUniqueId());
                        e.getPlayer().openInventory(getPathsMenu());
                        e.getPlayer().sendMessage(prefix+"§ePath 2 is now§a completed.");
                        e.getPlayer().getInventory().clear(2);
                    }
                }
                case 3 -> {
                    if (path3.getEnPos() != null && path3.getStartPos() != null) {
                        selectedPaths.remove(e.getPlayer().getUniqueId());
                        e.getPlayer().openInventory(getPathsMenu());
                        e.getPlayer().sendMessage(prefix+"§ePath 3 is now§a completed.");
                        e.getPlayer().getInventory().clear(2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getView().getTitle().equalsIgnoreCase("§aMap setup")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType().equals(Material.END_PORTAL_FRAME)) {
                if (pos1 == null || pos2 == null) {
                    player.sendMessage(prefix+"§cPlease make sure to select a region.");
                    player.closeInventory();
                    return;
                }
                LocationSection sel = new LocationSection(pos1, pos2);
                map.addCheckpoint(sel);
                player.sendMessage(prefix+"§aA new checkpoint has been added for region: §b" +
                        pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ()+
                        "§a - §e" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", "+ pos2.getBlockZ());
                player.closeInventory();

            } else if (e.getCurrentItem().getType().equals(Material.DIAMOND)) {
                switch (e.getCurrentItem().getItemMeta().getCustomModelData()) {
                    case 21 -> {
                        if (pos1 == null || pos2 == null) {
                            player.sendMessage(prefix+"§cPlease make sure to select a region.");
                            player.closeInventory();
                            return;
                        }

                        LocationSection sel = new LocationSection(pos1, pos2);
                        map.setStartLine(sel);
                        player.sendMessage(prefix+"§aThe start line has been set to §b" +
                                pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ()+
                                "§a - §e" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", "+ pos2.getBlockZ());
                        player.closeInventory();
                    }
                    case 22 -> {
                        if (pos1 == null || pos2 == null) {
                            player.sendMessage(prefix+"§cPlease make sure to select a region.");
                            player.closeInventory();
                            return;
                        }

                        LocationSection sel = new LocationSection(pos1, pos2);
                        map.setCustomLap2(sel);
                        player.sendMessage(prefix+"§aThe custom lap 2 line has been set to §b" +
                                pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ()+
                                "§a - §e" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", "+ pos2.getBlockZ());
                        player.closeInventory();
                    }
                    case 23 -> {
                        if (pos1 == null || pos2 == null) {
                            player.sendMessage(prefix+"§cPlease make sure to select a region.");
                            player.closeInventory();
                            return;
                        }

                        LocationSection sel = new LocationSection(pos1, pos2);
                        map.setCustomLap3(sel);
                        player.sendMessage(prefix+"§aThe custom lap 3 line has been set to §b" +
                                pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ()+
                                "§a - §e" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", "+ pos2.getBlockZ());
                        player.closeInventory();
                    }
                    case 24 -> {
                        if (pos1 == null || pos2 == null) {
                            player.sendMessage(prefix+"§cPlease make sure to select a region.");
                            player.closeInventory();
                            return;
                        }

                        LocationSection sel = new LocationSection(pos1, pos2);
                        map.setCustomEnd(sel);
                        player.sendMessage(prefix+"§aThe custom end line has been set to §b" +
                                pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ()+
                                "§a - §e" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", "+ pos2.getBlockZ());
                        player.closeInventory();
                    }
                }

            } else if (e.getCurrentItem().getType().equals(Material.EMERALD)) {
                map.setCameraPath1(path1);
                map.setCameraPath2(path2);
                map.setCameraPath3(path3);
                saveMap();
                player.sendMessage(prefix + "§aThe map has been saved.");
                player.getInventory().clear();
                player.closeInventory();
                HandlerList.unregisterAll(this);
                BoatRace.setSetupManager(null);

                for (UUID u : itemBoxes) {
                    Entity en = Bukkit.getEntity(u);
                    if (en == null) continue;
                    en.remove();
                }
            } else if (e.getCurrentItem().getType().equals(Material.NOTE_BLOCK)) {
                GuiBuilder b = new GuiBuilder("§aSelect a song", 3);
                b.slot(0, new ItemBuilder(Material.TURTLE_EGG)
                        .displayname("§aBeach")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(1, new ItemBuilder(Material.MAGMA_BLOCK)
                        .displayname("§aBowser")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(2, new ItemBuilder(Material.OAK_LEAVES)
                        .displayname("§aCircuit")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(3, new ItemBuilder(Material.QUARTZ_BLOCK)
                        .displayname("§aCity")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(4, new ItemBuilder(Material.SAND)
                        .displayname("§aDesert")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(5, new ItemBuilder(Material.RED_SAND)
                        .displayname("§aDesert2")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());

                b.slot(6, new ItemBuilder(Material.RED_STAINED_GLASS)
                        .displayname("§aRainbow Road")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());


                b.slot(7, new ItemBuilder(Material.BLUE_STAINED_GLASS)
                        .displayname("§aRainbow")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());


                b.slot(8, new ItemBuilder(Material.SNOW_BLOCK)
                        .displayname("§aSummit")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());


                b.slot(13, new ItemBuilder(Material.OAK_LOG)
                        .displayname("§aTreeway")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());
                b.slot(14, new ItemBuilder(Material.GHAST_SPAWN_EGG)
                        .displayname("§aGhosts")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());
                b.slot(12, new ItemBuilder(Material.RAW_GOLD)
                        .displayname("§aGoldmine")
                        .lore("", "§7§oLeft click to select,","§7§oright click to preview")
                        .build());



                b.slot(18, new ItemBuilder(Material.BARRIER)
                        .displayname("§cStop preview")
                        .build());


                b.slot(26, new ItemBuilder(Material.IRON_DOOR)
                        .displayname("§cBack to main gui")
                        .build());

                player.openInventory(b.build());
            } else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
                for (UUID u : itemBoxes) {
                    Entity en = Bukkit.getEntity(u);
                    if (en == null) continue;
                    en.remove();
                }
                player.sendMessage(prefix+"§c§oSetup process canceled");
                player.closeInventory();
                HandlerList.unregisterAll(this);
            } else if (e.getCurrentItem().getType().equals(Material.FIREWORK_ROCKET)) {
                player.closeInventory();
                map.addStartingPlace(LocUtil.fromBukkit(player.getLocation()));
                player.sendMessage(prefix+"§aStarting place has been added.");
            } else if (e.getCurrentItem().getType().equals(Material.ENDER_EYE)) {
                player.openInventory(getPathsMenu());
            } else if (e.getCurrentItem().getType().equals(Material.SKELETON_SKULL)) {
                player.closeInventory();
                deathAreas.add(new LocationSection(pos1, pos2));
                player.sendMessage(prefix+"§aReset zone has been added.");
            }
        } else if (e.getView().getTitle().equalsIgnoreCase("§aSelect a song")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getItemMeta() == null) return;
            String songID = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", "")
                    .replace(" ", "_").toLowerCase();

            if (e.getRawSlot() == 26) {
                player.openInventory(getMainGUI());
                return;
            }
            if (e.getRawSlot() == 18) {
                player.stopAllSounds();
                return;
            }
            if (e.isRightClick()) {
                previewSong(songID, player);
            }
            if (e.isLeftClick()) {
                map.setMusic("boatrace:mk.music."+songID);
                player.openInventory(getMainGUI());
                player.sendMessage(prefix+"§aSet the song file to §e"+songID);
            }
        } else if (e.getView().getTitle().equals("§aMap Setup - Camera Paths")) {
            e.setCancelled(true);
            switch (e.getRawSlot()) {
                case 9 -> { // Set path 1
                    path1 = new CameraIntroPath();
                    player.getInventory().setItem(2, new ItemBuilder(Material.ENDER_CHEST).displayname("§aSet camera path positions").build());
                    player.sendMessage("§a§lSetting camera path positions");
                    player.sendMessage("§7Use left click for the first position and right click for second position." +
                            "§lNote: Your head properties (yaw and pitch) will also be saved!");
                    selectedPaths.put(player.getUniqueId(), 1);
                    player.closeInventory();
                }
                case 10 -> {
                    BoatRace.getRaceManager().previewCameraPath(path1, player, GameMode.CREATIVE);
                }
                case 11 -> {
                    path1 = new CameraIntroPath();
                    player.sendMessage(prefix + "§cThe path §e1 §chas been reset.");
                }
                case 12 -> { // Set path 2
                    path2 = new CameraIntroPath();
                    player.getInventory().setItem(2, new ItemBuilder(Material.ENDER_CHEST).displayname("§aSet camera path positions").build());
                    player.sendMessage("§a§lSetting camera path positions");
                    player.sendMessage("§7Use left click for the first position and right click for second position." +
                            "§lNote: Your head properties (yaw and pitch) will also be saved!");
                    selectedPaths.put(player.getUniqueId(), 2);
                    player.closeInventory();
                }
                case 13 -> {
                    BoatRace.getRaceManager().previewCameraPath(path2, player, GameMode.CREATIVE);
                }
                case 14 -> {
                    path2 = new CameraIntroPath();
                    player.sendMessage(prefix + "§cThe path §e2 §chas been reset.");
                }
                case 15 -> { // Set path 2
                    path3 = new CameraIntroPath();
                    player.getInventory().setItem(2, new ItemBuilder(Material.ENDER_CHEST).displayname("§aSet camera path positions").build());
                    player.sendMessage("§a§lSetting camera path positions");
                    player.sendMessage("§7Use left click for the first position and right click for second position." +
                            "§lNote: Your head properties (yaw and pitch) will also be saved!");
                    selectedPaths.put(player.getUniqueId(), 3);
                    player.closeInventory();
                }
                case 16 -> {
                    BoatRace.getRaceManager().previewCameraPath(path3, player, GameMode.CREATIVE);
                }
                case 17 -> {
                    path3 = new CameraIntroPath();
                    player.sendMessage(prefix + "§cThe path §e3 §chas been reset.");
                }
                case 18 -> {
                    player.openInventory(getMainGUI());
                }
                case 22 -> {
                    BoatRace.getRaceManager().runMapIntro(path1, path2, path3, player, map, GameMode.CREATIVE);
                }
            }
        }
    }

    private Inventory getMainGUI() {
        GuiBuilder g = new GuiBuilder("§aMap setup", 3);
        g.slot(9, new ItemBuilder(Material.END_PORTAL_FRAME).displayname("§aAdd Checkpoint").lore("§7§oClick here to add a"
                , "§7§oCheckpoint to this map", "§7§ofor your §a§n§ocurrent wand locations").build());
        g.slot(11, new ItemBuilder(Material.DIAMOND).customModelData(21).displayname("§aSet start line")
                .lore("§7§oRegister the selected region", "§7§oas the start line.").build());
        g.slot(3, new ItemBuilder(Material.DIAMOND).customModelData(22).displayname("§aSet custom lap 2 line")
                .lore("§7§oRegister the selected region", "§7§oas the start line.").build());
        g.slot(4, new ItemBuilder(Material.DIAMOND).customModelData(23).displayname("§aSet custom lap 3 line")
                .lore("§7§oRegister the selected region", "§7§oas the start line.").build());
        g.slot(5, new ItemBuilder(Material.DIAMOND).customModelData(24).displayname("§aSet custom finish")
                .lore("§7§oRegister the selected region", "§7§oas the start line.").build());
        g.slot(13, new ItemBuilder(Material.NOTE_BLOCK).displayname("§aSelect song")
                .lore("§7§oChoose which song should be", "§7§oplayed during game.").build());
        g.slot(15, new ItemBuilder(Material.FIREWORK_ROCKET).displayname("§aAdd start position").build());
        g.slot(16, new ItemBuilder(Material.SKELETON_SKULL).displayname("§aAdd reset zone").build());
        g.slot(17, new ItemBuilder(Material.ENDER_EYE).displayname("§aCamera Paths").lore("", "§7§oDefine camera path for start here.").build());
        g.slot(18, new ItemBuilder(Material.EMERALD).displayname("§aSave map").lore("§7§oClick here to save this map in storage.").build());

        g.slot(19, new ItemBuilder(Material.BARRIER).displayname("§cAbort setup").lore("§7§oCancel the setup process").build());

        return g.build();
    }

    private Inventory getPathsMenu() {
        GuiBuilder g = new GuiBuilder("§aMap Setup - Camera Paths", 3);

        g.slot(1, new ItemBuilder(Material.ENDER_PEARL).displayname("§aPath 1").lore("","§7§oUse the items below to"
                ,"§7§oconfigure path 1.","","§aPath time: §64 seconds").build());
        g.slot(9, new ItemBuilder(Material.LIME_DYE).displayname("§aSet positions").build());
        g.slot(10, new ItemBuilder(Material.ENDER_EYE).displayname("§aPreview camera path").build());
        g.slot(11, new ItemBuilder(Material.RED_DYE).displayname("§cReset camera path").build());

        g.slot(4, new ItemBuilder(Material.ENDER_PEARL).displayname("§aPath 1").lore("","§7§oUse the items below to"
                ,"§7§oconfigure path 2.","","§aPath time: §64 seconds").build());
        g.slot(12, new ItemBuilder(Material.LIME_DYE).displayname("§aSet positions").build());
        g.slot(13, new ItemBuilder(Material.ENDER_EYE).displayname("§aPreview camera path").build());
        g.slot(14, new ItemBuilder(Material.RED_DYE).displayname("§cReset camera path").build());

        g.slot(7, new ItemBuilder(Material.ENDER_PEARL).displayname("§aPath 1").lore("","§7§oUse the items below to"
                ,"§7§oconfigure path 3.","","§aPath time: §64 seconds").build());
        g.slot(15, new ItemBuilder(Material.LIME_DYE).displayname("§aSet positions").build());
        g.slot(16, new ItemBuilder(Material.ENDER_EYE).displayname("§aPreview camera path").build());
        g.slot(17, new ItemBuilder(Material.RED_DYE).displayname("§cReset camera path").build());

        g.slot(22, new ItemBuilder(Material.ENDER_PEARL).displayname("§aPlay full intro").build());
        g.slot(18, new ItemBuilder(Material.DARK_OAK_DOOR).displayname("§cBack").build());

        return g.build();
    }

    private void previewSong(String song, Player player) {
        player.playSound(player.getLocation(), "boatrace:mk.music."+song, 1, 1);
    }
}