package de.justcody.cbmc.boatrace.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarManager {
    private static final List<Car> cars = new ArrayList<>();

    public static void createCar(Player player) {
        cars.add(new Car(player));
    }

    public static void deleteAll() {
        for (Car c : cars) c.removeCar();
    }

    public static void delete(UUID player) {
        for (Car c : cars) if (c.getPlayer().getUniqueId().equals(player)) c.removeCar();
    }

    public static Car getCar(UUID player) {
        for (Car c : cars) if (c.getPlayer().getUniqueId().equals(player)) return c;
        return null;
    }
}