package de.justcody.cbmc.boatrace.game.warps;

import de.codeblocksmc.codelib.ItemBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static de.justcody.cbmc.boatrace.BoatRace.prefix;

public class WarpManager implements Listener {

    private Warp currentWarpSetup;

    private final HashMap<UUID, Location> pos1 = new HashMap<>();
    private final HashMap<UUID, Location> pos2 = new HashMap<>();
    private final HashMap<UUID, Location> pos3 = new HashMap<>();
    private final HashMap<UUID, Location> pos4 = new HashMap<>();

    @Getter
    private final ItemStack wand = new ItemBuilder(Material.DIAMOND_AXE).displayname("§aWarp wand (trigger point)").build();
    @Getter
    private final ItemStack wand2 = new ItemBuilder(Material.STONE_AXE).displayname("§aWarp wand (End point)").build();

    private final List<Warp> warps = new ArrayList<>();

    public void createWarpEntry(Warp warp) {
        warps.add(warp);
    }

    public Location getPos1(UUID uuid) {
        return pos1.getOrDefault(uuid, null);
    }

    public Location getPos2(UUID uuid) {
        return pos2.getOrDefault(uuid, null);
    }

    public Location getPos3(UUID uuid) {
        return pos3.getOrDefault(uuid, null);
    }

    public Location getPos4(UUID uuid) {
        return pos4.getOrDefault(uuid, null);
    }

    public List<String> getWarpNames() {
        List<String> warps = new ArrayList<>();
        for (Warp w : this.warps) {
            warps.add(w.getId());
        }
        return warps;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getEquipment().getItemInMainHand().equals(wand)) {
            e.getPlayer().sendMessage(prefix+"§bSelected first position.");
            pos1.put(e.getPlayer().getUniqueId(), e.getBlock().getLocation());
        }
        if (e.getPlayer().getEquipment().getItemInMainHand().equals(wand2)) {
            e.getPlayer().sendMessage(prefix+"§bSelected first position.");
            pos3.put(e.getPlayer().getUniqueId(), e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().equals(wand)) {
            if (e.getAction().isRightClick()) {
                if (e.getClickedBlock() == null) pos2.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
                else pos2.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());

                e.getPlayer().sendMessage(prefix + "§bSelected second position.");

            } else if (e.getAction().isLeftClick()) {
                e.getPlayer().sendMessage(prefix+"§bSelected first position.");
                pos1.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        if (e.getItem().equals(wand2)) {
            if (e.getAction().isRightClick()) {
                if (e.getClickedBlock() == null) pos4.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
                else pos4.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());

                e.getPlayer().sendMessage(prefix + "§bSelected second position.");

            } else if (e.getAction().isLeftClick()) {
                e.getPlayer().sendMessage(prefix+"§bSelected first position.");
                pos3.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

    }
}