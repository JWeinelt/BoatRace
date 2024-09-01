package de.justcody.cbmc.boatrace.game.powerup;

import de.justcody.cbmc.boatrace.BoatRace;
import de.justcody.cbmc.boatrace.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class BlueShell extends PowerUp {
    private UUID target;

    public BlueShell(UUID thrower) {
        super(thrower);
        type = Game.PowerUpItem.RED_SHELL;
    }

    public BlueShell setTarget(UUID uuid) {
        target = uuid;
        return this;
    }

    @Override
    public void startTask() {
        runTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (target == null || !Bukkit.getOfflinePlayer(target).isOnline()) {
                    cancel();
                    return;
                }
                armorStand.setVelocity(calculateVelocity());

                Location aL = armorStand.getLocation();
                Player p = Bukkit.getPlayer(target);
                // p can't be null
                if (p == null) return;
                Location pL = p.getLocation();
                if (aL.getX() < pL.getX() + 1 && aL.getX() > pL.getX() -1 &&
                        aL.getY() < pL.getY() + 4 && aL.getY() > pL.getY() -1 &&
                        aL.getZ() < pL.getZ() + 1 && aL.getZ() > pL.getZ() -1) {
                    if (p.getVehicle() != null) {
                        p.sendMessage(BoatRace.prefix+"§bYou have been hit by §c"+Bukkit.getOfflinePlayer(thrower).getName());

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
        }.runTaskTimer(BoatRace.getPlugin(), 0, 1);
    }

    @Override
    public Vector calculateVelocity() {
        if (target == null || !Bukkit.getOfflinePlayer(target).isOnline()) return new Vector(0,0,0);
        Location shellLoc = armorStand.getLocation();
        double speed = 1.5;
        final Player pTarget = Bukkit.getPlayer(target);
        if (pTarget == null) return new Vector(0,0,0);

        Location targetLoc = null;


        targetLoc = pTarget.getLocation();

        double x = targetLoc.getX() - shellLoc.getX();
        double z = targetLoc.getZ() - shellLoc.getZ();
        boolean ux = true;
        double px = Math.abs(x);
        double pz = Math.abs(z);
        if (px > pz) {
            ux = false;
        }
        if (ux) {
            // x is smaller
            // long mult = (long) (pz/speed);
            x = (x / pz) * speed;
            z = (z / pz) * speed;
        } else {
            // z is smaller
            // long mult = (long) (px/speed);
            x = (x / px) * speed;
            z = (z / px) * speed;
        }

        Location loc = armorStand.getLocation();

        //Bouncing off walls
        Vector dir = new Vector(x,0,z);
        if(Math.abs(dir.getX()) > 1) {
            dir.setX(dir.getX()/Math.abs(dir.getX()));
        }
        if(Math.abs(dir.getZ()) > 1) {
            dir.setZ(dir.getZ()/Math.abs(dir.getZ()));
        }
        Block toHit = loc.add(dir).getBlock();
        Vector vel = null;
        if(!toHit.isEmpty() && !toHit.isLiquid()){
            if(toHit.getType().name().toLowerCase().contains("slab")) { //RedShells need to climb up steps
                vel = new Vector(x,0,z);
            } else {
                toHit = armorStand.getLocation().add(dir.setX(0)).getBlock();
                if(!toHit.isEmpty() && !toHit.isLiquid()) {
                    vel =  new Vector(x,0,-z);
                } else {
                    vel = new Vector(-x,0,z);
                }
            }
        } else {
            vel = new Vector(x, 0, z);
        }

        //Y-Course (move with track on Y-Axis)
        int height = getHeight(loc);
        vel.add(new Vector(0,0,0)); 									//Correct gravity of BlueShell
        if(height > 3 && loc.getY() > targetLoc.getY()) { 			  	//"Smooth" Y-Axis Movement
            vel.subtract(new Vector(0,0.15,0));
            if(height > 4) {
                vel.subtract(new Vector(0,0.2,0));
            }
            if(height > 5) {
                vel.subtract(new Vector(0,0.25,0));
            }
        } else if(height < 3) {
            vel.add(new Vector(0,0.15,0));
        }

        return vel;
    }


    static int getHeight(Location loc) {
        boolean blockFound = false;
        int height = 0;
        while(!blockFound) {
            if(!loc.getBlock().isEmpty()) {
                blockFound = true;
            } else {
                loc.subtract(0,1,0);
                height++;
            }
        }
        return height;
    }


}
