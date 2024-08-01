package de.einfachcody.boatrace.game.cups;


@SuppressWarnings("unused")
public enum Course {
    MAPLE_TREE_WAY(Cup.DIAMOND, 1, "treeway"),
    MARIO_CIRCUIT(Cup.DIAMOND, 2, "mario_circuit_1"),
    DRY_DRY_DESERT(Cup.DIAMOND, 3, "dry_dry_desert"),
    BOWSERS_CASTLE_1(Cup.DIAMOND, 4, "bowsers_castle_1"),

    PALM_BEACH(Cup.EMERALD, 1, "palm_beach"),
    GHOST_VALLEY_1(Cup.EMERALD, 2, "ghosts_1"),
    COCONUT_MALL(Cup.EMERALD, 3, "coconut_mall"),
    RAINBOW_ROAD_1(Cup.EMERALD, 4, "rainbow_1"),

    LUIGIS_CIRCUIT(Cup.IRON, 1, "luigi_circuit"),
    BERLIN_BYWAY(Cup.IRON, 2, "berlin_byway_1"),
    MOO_MOO_MEADOWS(Cup.IRON, 3, "moo_moo_meadows"),
    BIG_TREE(Cup.IRON, 4, "big_tree"),

    RIGGITY_JUNGLE(Cup.GOLD, 1, "riggity_jungle"),
    MUSHROOM_CASTLE(Cup.GOLD, 2, "mushroom_castle"),
    CRYSTAL_MINES(Cup.GOLD, 3, "crystal_mines"),
    GRUMBLE_VOLCANO(Cup.GOLD, 4, "grumble_volcano"),

    BLOCK_BLOCK_CLOUDTOPS(Cup.REDSTONE, 1, "block_block_cloud_tops"),
    GOATY_CIRCUIT(Cup.REDSTONE, 2, "goaty_circuit"),
    SPOOKY_PLANKWALK(Cup.REDSTONE, 3, "spooky_plankwalk"),
    ROYAL_RACEWAY(Cup.REDSTONE, 4, "royal_raceway"),

    DONUT_PLAINS(Cup.COPPER, 1, "donut_plains"),
    TOADS_TURNPIKE(Cup.COPPER, 2, "toads_turnpike"),
    SUNSHINE_AIRPORT(Cup.COPPER, 3, "sunshine_airport"),
    BOWSERS_CASTLE_2(Cup.COPPER, 4, "bowsers_castle_2"),

    GCN_YOSHI_CIRCUIT(Cup.LAPISLAZULI, 1, "gcn_yoshi_circuit"),
    ICE_OUTPOST(Cup.LAPISLAZULI, 2, "ice_outpost"),
    PEACH_GARDENS(Cup.LAPISLAZULI, 3, "peach_gardens"),
    AIRSHIP_FORTRESS(Cup.LAPISLAZULI, 4, "airship_fortress"),

    SHERBET_LAND(Cup.GLOWSTONE, 1, "sherbet_land"),
    MUSHROOM_GORGE(Cup.GLOWSTONE, 2, "mushroom_gorge"),
    DAISY_CIRCUIT(Cup.GLOWSTONE, 3, "daisy_circuit"),
    DK_SUMMIT(Cup.GLOWSTONE, 4, "dk_summit");

    public final Cup cup;
    public final int placeInCup;
    public final String courseID;

    Course(Cup cup, int placeInCup, String courseID) {
        this.cup = cup;
        this.placeInCup = placeInCup;
        this.courseID = courseID;
    }
}
