package de.justcody.cbmc.boatrace.game.powerup;

import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PowerUp {
    public ArmorStand armorStand;
    public Game.PowerUpItem type;

    public BukkitTask runTask;

    public final UUID thrower;

    public PowerUp(UUID thrower) {
        this.thrower = thrower;
    }

    public void spawn(Location l) {
        armorStand = l.getWorld().spawn(l, ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);
        armorStand.setAI(false);
        armorStand.setArms(true);

        armorStand.getEquipment().setItemInMainHand(new ItemBuilder(Material.DIAMOND).customModelData(type.modelData).build());
        startTask();
    }

    public void startTask() {

    }

    public Vector calculateVelocity() {
        return new Vector(0, 0, 0);
    }
}
