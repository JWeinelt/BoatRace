package de.justcody.cbmc.boatrace.game;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class Cup {
    private final String name;
    private final Material displayMaterial;

    public Cup(String name, Material displayMaterial) {
        this.name = name;
        this.displayMaterial = displayMaterial;
    }
}
