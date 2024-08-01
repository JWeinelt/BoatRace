package de.justcody.cbmc.boatrace.game;

import de.justcody.cbmc.boatrace.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Car {
    private Player player;
    private Boat boat;
    private Zombie zombie;
    private Husk lakitu;

    public Car(Player player) {
        this.player = player;
        Location l = player.getLocation();
        boat = l.getWorld().spawn(l, Boat.class);
        boat.setBoatType(Boat.Type.OAK);
        boat.setMaxSpeed(0);

        zombie = l.getWorld().spawn(l, Zombie.class);
        zombie.setVisualFire(false);
        zombie.setAdult();
        zombie.setInvulnerable(true);
        zombie.setInvisible(true);
        zombie.setAI(false);
        zombie.setSilent(true);
        zombie.setGravity(false);
        zombie.getEquipment().setItemInMainHand(new ItemBuilder(Material.DIAMOND).customModelData(13).build());

        boat.addPassenger(zombie);
        boat.addPassenger(player);

        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection().normalize();

        Vector leftDirection = playerDirection.crossProduct(new Vector(0, 1, 0)).normalize().multiply(-2);

        Location lakituSpawn = playerLocation.add(leftDirection).add(0, 2, 0);
        lakituSpawn.setYaw(playerLocation.getYaw() + 180);

        lakitu = l.getWorld().spawn(lakituSpawn, Husk.class);
        lakitu.setAI(false);
        lakitu.setGravity(false);
        lakitu.setSilent(true);
        lakitu.setAdult();
        lakitu.setInvulnerable(true);
        lakitu.getEquipment().setItemInMainHand(new ItemBuilder(Material.DIAMOND).customModelData(20).build());

    }


    public void removeCar() {
        zombie.remove();
        boat.remove();
        lakitu.remove();
    }
}