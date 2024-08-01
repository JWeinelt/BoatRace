package de.einfachcody.boatrace.game.achieve;

import org.bukkit.Color;

public enum Driver {
    MARIO("", "Mario", Color.RED),
    LUIGI("", "Luigi", Color.GREEN),
    PEACH("", "Peach", Color.fromRGB(255,0,255)),
    DAISY("", "Daisy", Color.fromRGB(255,159,0)),
    WALUIGI("", "Waluigi", Color.PURPLE),
    WARIO("", "Wario", Color.YELLOW),
    TOAD("", "Toad", Color.RED),
    YOSHI("", "Yoshi", Color.LIME),
    ROSALINA("", "Rosalina", Color.fromRGB(86,156,206)),
    BOWSER("", "Bowser", Color.YELLOW),
    DONKEY_KONG("", "Donkey Kong", Color.fromRGB(0,0,0)),
    TOADETTE("", "Toadette", Color.fromRGB(0,0,0)),
    BOWSER_JR("", "Bowser Jr.", Color.YELLOW),

    ;

    public final String skinValue;
    public final String displayName;
    public final Color color;

    Driver(String skinValue, String displayName, Color color) {
        this.skinValue = skinValue;
        this.displayName = displayName;
        this.color = color;
    }
}
