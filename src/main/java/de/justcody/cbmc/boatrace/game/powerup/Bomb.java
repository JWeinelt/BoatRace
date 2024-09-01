package de.justcody.cbmc.boatrace.game.powerup;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.Game;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Bomb extends PowerUp {
    private final Location loc;

    public Bomb(UUID thrower, Location location) {
        super(thrower);
        this.loc = location;
        type = Game.PowerUpItem.BOMB;
    }

    @Override
    public void startTask() {
        for (Entity en : loc.getNearbyEntities(5, 5, 5)) {
            if (en instanceof Player p) {
                p.playSound(p, Sound.ENTITY_TNT_PRIMED, 1, 1);
            }
        }
        runTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity e : armorStand.getNearbyEntities(5, 5, 5)) {
                    if (e instanceof Player p) {
                        if (p.isInsideVehicle()) {
                            new BukkitRunnable() {
                                int i = 0;

                                @Override
                                public void run() {
                                    p.getVehicle().setVelocity(new Vector(0, 0, 0));
                                    i++;
                                    if (i > 40) {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(BoatRace.getPlugin(), 0, 1);

                            armorStand.remove();
                        }
                        p.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    }
                }
                loc.createExplosion(2, false, false);
            }
        }.runTaskLater(BoatRace.getPlugin(), 60);
    }
}
