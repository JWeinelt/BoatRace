package de.justcody.cbmc.boatrace.game.powerup;

import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Banana extends PowerUp {
    public Banana(UUID thrower) {
        super(thrower);
        type = Game.PowerUpItem.BANANA;
    }

    @Override
    public void spawn(Location l) {

        armorStand = l.getWorld().spawn(l.add(0, -1.5, 0), ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);
        armorStand.setAI(false);
        armorStand.setArms(true);
        armorStand.setRightArmPose(new EulerAngle(0, 0, 0));

        armorStand.getEquipment().setItemInMainHand(new ItemBuilder(Material.DIAMOND).customModelData(type.modelData).build());
        startTask();
    }

    @Override
    public void startTask() {
        runTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity e : armorStand.getNearbyEntities(1, 1, 1)) {
                    if (e instanceof Player p) {
                        if (p.isInsideVehicle()) {
                            new BukkitRunnable() {
                                int i = 0;

                                @Override
                                public void run() {
                                    p.getVehicle().setVelocity(new Vector(0, 0, 0));
                                    i++;
                                    if (i > 20) {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(BoatRace.getPlugin(), 0, 1);

                            armorStand.remove();
                            cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(BoatRace.getPlugin(), 0, 1);
    }
}
