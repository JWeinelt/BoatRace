package de.justcody.cbmc.boatrace.game.map;


import lombok.Getter;

@Getter
public class CupInst {
    private final String name;
    private final String displayMaterial;

    public CupInst(String name, String displayMaterial) {
        this.name = name;
        this.displayMaterial = displayMaterial;
    }
}
