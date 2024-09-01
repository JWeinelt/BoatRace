package de.justcody.cbmc.boatrace.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
public class Cup {
    private String name;
    private Material displayMaterial;

    private String track1;
    private String track2;
    private String track3;
    private String track4;

    public Cup() {}

    public Cup(String name) {
        this.name = name;
    }

    public Cup(String name, Material displayMaterial) {
        this.name = name;
        this.displayMaterial = displayMaterial;
    }

    public Cup(String name, Material displayMaterial, String track1, String track2, String track3, String track4) {
        this.name = name;
        this.displayMaterial = displayMaterial;
        this.track1 = track1;
        this.track2 = track2;
        this.track3 = track3;
        this.track4 = track4;
    }
}
