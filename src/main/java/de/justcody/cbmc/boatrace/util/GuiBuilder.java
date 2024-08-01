package de.justcody.cbmc.boatrace.util;

import de.justcody.cbmc.boatrace.BoatRace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiBuilder implements Listener {

    private final Inventory gui;
    private List<UUID> players;
    private final String title;
    private GuiClickCallback callback;
    private final int size;

    public GuiBuilder(int rows, String title) {
        this.title = title;
        gui = Bukkit.createInventory(null, rows*9, title);
        size = rows*9;
    }

    public GuiBuilder players(UUID... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
        return this;
    }

    public GuiBuilder fill(ItemStack item) {
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, item);
        }
        return this;
    }


    public GuiBuilder slots(Material material, int[] slots) {
        for (int i = 0; i < gui.getSize(); i++) {
            for (int element : slots) {
                if (i==element) {
                    gui.setItem(i, new ItemBuilder(material).displayname(" ").build());
                }
            }
        }
        return this;
    }

    public GuiBuilder slots(int[] slots, ItemStack stack) {
        for (int i = 0; i < gui.getSize(); i++) {
            for (int element : slots) {
                if (i==element) {
                    gui.setItem(i, stack);
                }
            }
        }
        return this;
    }

    public GuiBuilder slots(ItemStack stack, int... slots) {
        for (int i = 0; i < gui.getSize(); i++) {
            for (int element : slots) {
                if (i==element) {
                    gui.setItem(i, stack);
                }
            }
        }
        return this;
    }

    public GuiBuilder slot(int slot, ItemStack item) {
        gui.setItem(slot, item);
        return this;
    }

    public Inventory build() {
        return this.gui;
    }

    public GuiBuilder filler(Material material) {
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, new ItemBuilder(material).displayname(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).build());
        }
        return this;
    }

    @EventHandler
    private void onInventory(InventoryClickEvent e) {
        if(!players.contains(e.getWhoClicked().getUniqueId())) return;
        if(!e.getView().getTitle().equals(title)) return;

        if(callback.nullSafe()) {
            if(e.getCurrentItem() == null) return;
            if(e.getClickedInventory() == null) return;

            callback.onClick((Player) e.getWhoClicked(), e.getCurrentItem());
        } else {
            callback.onClick((Player) e.getWhoClicked(), e.getCurrentItem());
        }
    }

    public void listen(GuiClickCallback callback) {
        this.callback = Objects.requireNonNull(callback, "callback can't be null");
        Bukkit.getPluginManager().registerEvents(this, BoatRace.getPlugin());
    }

    public void stop() {

    }

    public int getSize() {
        return size;
    }

    public interface GuiClickCallback {

        void onClick(Player p, ItemStack is);

        default boolean nullSafe() {
            return true;
        }

    }

}