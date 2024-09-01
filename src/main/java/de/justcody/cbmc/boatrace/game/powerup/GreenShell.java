package de.justcody.cbmc.boatrace.game.powerup;

import de.codeblocksmc.codelib.ItemBuilder;
import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GreenShell extends PowerUp {

    private final UUID thrower;

    public GreenShell(UUID thrower) {
        super(thrower);
        type = Game.PowerUpItem.GREEN_SHELL;
        this.thrower = thrower;
    }


    @Override
    public void spawn(Location l) {
        Player p = Bukkit.getPlayer(thrower);
        if (!Bukkit.getOfflinePlayer(thrower).isOnline()) return;
        l = l.add(p.getEyeLocation().getDirection().normalize().multiply(2));
        armorStand = l.getWorld().spawn(l, ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);
        armorStand.setAI(false);
        armorStand.setArms(true);

        armorStand.getEquipment().setItemInMainHand(new ItemBuilder(Material.DIAMOND).customModelData(type.modelData).build());
        startTask();
    }

    @Override
    public void startTask() {
        runTask = new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.setVelocity(calculateVelocity());
                for (Entity en : armorStand.getNearbyEntities(1.3, 3, 1.3)) {
                    if (en instanceof Player p) {

                        if (p.getVehicle() != null) {
                            p.sendMessage(BoatRace.prefix+"§bYou have been hit by §c"+ Bukkit.getOfflinePlayer(thrower).getName());

                            new BukkitRunnable() { // Set boat speed to 0

                                int i = 0;
                                @Override
                                public void run() {
                                    p.getVehicle().setVelocity(new Vector(0,0,0));
                                    i++;
                                    if (i>30) {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(BoatRace.getPlugin(), 0, 1);
                            cancel();
                            armorStand.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(BoatRace.getPlugin(), 0, 1);
    }


    @Override
    public Vector calculateVelocity() {
        if (!Bukkit.getOfflinePlayer(thrower).isOnline()) return new Vector(0, 0, 0);
        Vector direction = Bukkit.getPlayer(thrower).getEyeLocation()
                .getDirection(); //The direction to fire the shell
        double speed = 0.9; //The speed to fire it at
        boolean ux = true; //If abs.x(True) or abs.z(False) is smaller
        double x = direction.getX();
        double z = direction.getZ();
        double px = Math.abs(x);  //Make negatives become positive
        double pz = Math.abs(z);
        if (px > pz) {
            ux = false; //Set ux according to sizes
        }

        if (ux) {
            // x is smaller
            // long mult = (long) (pz/speed); - Calculate Multiplier
            x = (x / pz) * speed;
            z = (z / pz) * speed;
        } else {
            // z is smaller
            // long mult = (long) (px/speed);
            x = (x / px) * speed;
            z = (z / px) * speed;
        }
        final double fx = x;
        final double fz = z;
        direction = new Vector(-fx, 0, -fz);
        Location loc = armorStand.getLocation();
        Block toHit = loc.add(direction).getBlock();
        if(!toHit.isEmpty() && !toHit.isLiquid()){
            if(toHit.getType().name().toLowerCase().contains("slab")) { //GreenShells need to climb up steps
                armorStand.setVelocity(direction.setY(0.55));
            } else {
                toHit = armorStand.getLocation().add(direction.setX(0)).getBlock();
                if(!toHit.isEmpty() && !toHit.isLiquid()) {
                    armorStand.setVelocity(direction.setZ(direction.getZ()*-1));
                } else {
                    armorStand.setVelocity(direction.setX(direction.getX()*-1));
                }
            }
        }
        else {
            if(armorStand.getVelocity().getY() > 0.0) { //Straighten up!
                armorStand.setVelocity(direction.setY(0));
            }
            if(armorStand.getLocation().subtract(0,1,0).getBlock().isEmpty()) {
                armorStand.setVelocity(direction.setY(-0.4));
            }
            armorStand.setVelocity(direction);
        }
        return direction;
    }
}
