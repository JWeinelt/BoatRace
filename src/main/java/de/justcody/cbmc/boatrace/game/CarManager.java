package de.justcody.cbmc.boatrace.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CarManager {
    private static final List<Car> cars = new ArrayList<>();

    public static void createCar(Player player) {
        cars.add(new Car(player));
    }

    public static void deleteAll() {
        for (Car c : cars) c.removeCar();
    }
}