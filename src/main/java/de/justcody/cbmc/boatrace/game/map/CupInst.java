package de.justcody.cbmc.boatrace.game.map;


import lombok.Getter;
import org.bukkit.Material;

@Getter
public class CupInst {
    private final String name;
    private Material displayMaterial;

    private String track1;
    private String track2;
    private String track3;
    private String track4;

    public CupInst(String name) {
        this.name = name;
    }

    public CupInst(String name, Material displayMaterial) {
        this.name = name;
        this.displayMaterial = displayMaterial;
    }

    public CupInst(String name, Material displayMaterial, String track1, String track2, String track3, String track4) {
        this.name = name;
        this.displayMaterial = displayMaterial;
        this.track1 = track1;
        this.track2 = track2;
        this.track3 = track3;
        this.track4 = track4;
    }
}
