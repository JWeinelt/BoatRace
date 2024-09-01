package de.justcody.cbmc.boatrace.game;

import de.codeblocksmc.codelib.GuiBuilder;
import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class CupManager implements Listener {
    private final HashMap<UUID, CupInv> inventoryState = new HashMap<>();
    private final HashMap<UUID, Cup> cupSelection = new HashMap<>();
    private final HashMap<UUID, List<String>> selectedMaps = new HashMap<>();

    public void createInstance(Player player) {
        inventoryState.put(player.getUniqueId(), CupInv.CUP_CREATION);
        cupSelection.put(player.getUniqueId(), new Cup());
        GuiBuilder b = new GuiBuilder("§aCreate Cup", 3);
        b.slot(11, new ItemBuilder(Material.MAP).displayname("§aMaps").lore("","§7§oSelect which map should be in this cup"));
        b.slot(13, new ItemBuilder(Material.PAPER).displayname("§aIcon").lore("", "§7§oSelect which icon should be used.",
                "","§7§o>> Hold the item in your hand!"));
        b.slot(15, new ItemBuilder(Material.NAME_TAG).displayname("§aName").lore("", "§7§oSelect a name for this cup.",
                "§c§oAllowed: a-z"));
        b.slot(18, new ItemBuilder(Material.LIME_DYE).displayname("§aSave Cup"));

        b.openForPlayer(player);
    }

    public void createMapSelection(Player player) {
        List<String> allMaps = BoatRace.getRaceManager().getAvailableMaps();
        inventoryState.put(player.getUniqueId(), CupInv.CUP_MAPS);
        int size = 2+allMaps.size()/9;
        GuiBuilder b = new GuiBuilder("§aCreate Cup - Maps", size);
        int i = 0;
        for (String m : allMaps) {
            b.slot(i, new ItemBuilder(Material.PAPER).displayname("§e"+m).lore("", "§7§oClick to select this map"));
            if (i>=size*9) break;
            i++;
        }
        //b.slot(size*2-1, new ItemBuilder(Material.ENDER_EYE).displayname("§cSequence the maps").lore("","§cSelect 4 maps!"));

        b.openForPlayer(player);
    }

    public void createNameInv(Player player) {
        inventoryState.put(player.getUniqueId(), CupInv.CUP_NAME);
        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, Component.text("§aCreate Cup - Name"));
        inv.setItem(0, new ItemBuilder(Material.PAPER).build());
        inv.setItem(2, new ItemBuilder(Material.PAPER).displayname("Cup Name").build());
        player.openInventory(inv);
    }

    public void createRangeInventory(Player player) {
        inventoryState.put(player.getUniqueId(), CupInv.CUP_MAP_RANGE);
        GuiBuilder b = new GuiBuilder("Create Cup - Map Sequence", 1);
        if (selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).size() != 4) return;
        b.slot(0, new ItemBuilder(Material.PAPER).displayname(selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).get(0)));
        b.slot(1, new ItemBuilder(Material.PAPER).displayname(selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).get(1)));
        b.slot(2, new ItemBuilder(Material.PAPER).displayname(selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).get(2)));
        b.slot(3, new ItemBuilder(Material.PAPER).displayname(selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).get(3)));
        b.slot(8, new ItemBuilder(Material.LIME_DYE).displayname("§aSave").lore("", "§7§oDrag the maps in the order you","§7§owant them to be."));

        b.openForPlayer(player);
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getViewers().isEmpty()) {
            return;
        }

        // Optionally, check if it's the correct inventory based on its title or other conditions
        if (e.getView().title().equals(Component.text("§aCreate Cup - Name"))) {
            ItemStack firstItem = inv.getItem(0);
            if (firstItem != null && firstItem.getType() == Material.PAPER) {
                String renameText = e.getInventory().getRenameText(); // Gets the text the player typed in
                ItemStack resultItem = new ItemBuilder(Material.PAPER).displayname(renameText).build();
                Cup cup = cupSelection.getOrDefault(e.getViewers().get(0).getUniqueId(), new Cup());
                cup.setName(renameText);
                cupSelection.put(e.getViewers().get(0).getUniqueId(), cup);
                e.setResult(resultItem);
            }
        }
    }

    @EventHandler
    public void onInvInteract(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (inventoryState.getOrDefault(e.getWhoClicked().getUniqueId(), CupInv.NONE).equals(CupInv.CUP_CREATION)) {
            e.setCancelled(true);
            switch (e.getRawSlot()) {
                case 11 -> {
                    createMapSelection(player);
                }
                case 13 -> {
                    if (!player.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {

                        Cup cup = cupSelection.getOrDefault(player.getUniqueId(), new Cup());
                        cup.setDisplayMaterial(
                                player.getEquipment().getItemInMainHand().getType()
                        );
                        cupSelection.put(player.getUniqueId(), cup);
                        player.sendMessage(prefix+"§aIcon has been set.");
                    } else {
                        player.sendMessage(prefix+"§cPlease hold an item in your hand.");
                    }
                }
                case 15 -> {
                    createNameInv(player);
                }
                case 18 -> {
                    Cup cup = cupSelection.getOrDefault(player.getUniqueId(), new Cup());
                    if (cup.getName() == null) {
                        cup.setName("Test");
                    }
                    BoatRace.getRaceManager().addCup(cup);
                    BoatRace.getRaceManager().saveCups();
                    // Cleaning up
                    cupSelection.remove(player.getUniqueId());
                    inventoryState.remove(player.getUniqueId());
                    selectedMaps.remove(player.getUniqueId());
                }
            }
        } else if (inventoryState.getOrDefault(e.getWhoClicked().getUniqueId(), CupInv.NONE).equals(CupInv.CUP_NAME)) {
            if (e.getRawSlot() == 2) {
                player.sendMessage(prefix+"§aSet the name of the cup to §e"+e.getCurrentItem().getItemMeta().getDisplayName());
                Cup cup = cupSelection.getOrDefault(player.getUniqueId(), new Cup());
                cup.setName(e.getCurrentItem().getItemMeta().getDisplayName());
                cupSelection.put(player.getUniqueId(), cup);
                createInstance(player);
            }
            e.setCancelled(true);

        } else if (inventoryState.getOrDefault(e.getWhoClicked().getUniqueId(), CupInv.NONE).equals(CupInv.CUP_MAPS)) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta() == null) return;
                if (e.getCurrentItem().getItemMeta().displayName() == null) return;
                if (e.getCurrentItem().getType().equals(Material.PAPER)) {
                    String mapName = PlainTextComponentSerializer.plainText().serialize(e.getCurrentItem().getItemMeta().displayName());
                    List<String> lore = e.getCurrentItem().getLore();
                    if (lore == null) return;
                    if (lore.contains("§b§o>> Selected")) { // Already in list
                        player.sendMessage("sel");
                        List<String> maps = selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>());
                        maps.remove(mapName);
                        selectedMaps.put(player.getUniqueId(), maps);
                        ItemStack stack = e.getCurrentItem();
                        ItemMeta meta = stack.getItemMeta();
                        lore.clear();
                        lore.add("");
                        lore.add("§7§oClick to select this map");
                        stack.setItemMeta(meta);
                        e.getInventory().setItem(e.getRawSlot(), stack);
                        e.setCancelled(true);
                    } else { // Not selected yet
                        List<String> maps = selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>());
                        maps.add(mapName);
                        selectedMaps.put(player.getUniqueId(), maps);
                        player.sendMessage("sel2");
                        ItemStack stack = e.getCurrentItem();
                        ItemMeta meta = stack.getItemMeta();
                        lore.clear();
                        lore.add("");
                        lore.add("§b§o>> Selected");
                        stack.setItemMeta(meta);
                        e.getInventory().setItem(e.getRawSlot(), stack);
                        e.setCancelled(true);
                    }
                    if (selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).size() == 4) {
                        e.getInventory().setItem(e.getInventory().getSize()-1,
                                new ItemBuilder(Material.ENDER_EYE).displayname("§aSequence the maps").lore("","§7§oSet the maps in the range you wish"
                                , "", "§b§oSelected: " + selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).size()).build());
                    } else {
                        e.getInventory().setItem(e.getInventory().getSize()-1,
                                new ItemBuilder(Material.ENDER_EYE).displayname("§cSequence the maps").lore("","§cSelect 4 maps!"
                                        , "", "§b§oSelected: " + selectedMaps.getOrDefault(player.getUniqueId(), new ArrayList<>()).size()).build());
                    }
                } else if (e.getCurrentItem().getType().equals(Material.ENDER_EYE)) {
                    createRangeInventory(player);
                    e.setCancelled(true);
                }
            }
        } else if (inventoryState.getOrDefault(e.getWhoClicked().getUniqueId(), CupInv.NONE).equals(CupInv.CUP_MAP_RANGE)) {
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType().equals(Material.LIME_DYE)) {
                if (e.getInventory().getItem(0) == null || e.getInventory().getItem(1) == null ||
                        e.getInventory().getItem(2) == null || e.getInventory().getItem(3) == null) return;
                String map1 = PlainTextComponentSerializer.plainText().serialize(e.getInventory().getItem(0).getItemMeta().displayName());
                String map2 = PlainTextComponentSerializer.plainText().serialize(e.getInventory().getItem(1).getItemMeta().displayName());
                String map3 = PlainTextComponentSerializer.plainText().serialize(e.getInventory().getItem(2).getItemMeta().displayName());
                String map4 = PlainTextComponentSerializer.plainText().serialize(e.getInventory().getItem(3).getItemMeta().displayName());
                map1 = map1.replace("§e", "");
                map2 = map2.replace("§e", "");
                map3 = map3.replace("§e", "");
                map4 = map4.replace("§e", "");
                Cup cup = cupSelection.getOrDefault(player.getUniqueId(), new Cup());
                cup.setTrack1(map1);
                cup.setTrack2(map2);
                cup.setTrack3(map3);
                cup.setTrack4(map4);
                cupSelection.put(player.getUniqueId(), cup);
                createInstance(player);
                player.sendMessage(prefix+"§aMaps have been set:");
                player.sendMessage("§61.: " + map1);
                player.sendMessage("§62.: " + map2);
                player.sendMessage("§63.: " + map3);
                player.sendMessage("§64.: " + map4);
            }
        }
        if (!e.getView().getTitle().startsWith("§aCreate Cup")) e.setCancelled(false);
    }


    enum CupInv {
        CUP_CREATION,
        CUP_NAME,
        CUP_MAPS,
        CUP_MAP_RANGE,
        NONE;
    }
}
