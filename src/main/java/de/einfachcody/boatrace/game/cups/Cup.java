package de.einfachcody.boatrace.game.cups;

public enum Cup {
    DIAMOND(Course.MAPLE_TREE_WAY, Course.MARIO_CIRCUIT,
            Course.DRY_DRY_DESERT, Course.BOWSERS_CASTLE_1),
    GOLD(Course.RIGGITY_JUNGLE, Course.MUSHROOM_CASTLE,
            Course.CRYSTAL_MINES, Course.GRUMBLE_VOLCANO),
    EMERALD(Course.PALM_BEACH, Course.GHOST_VALLEY_1,
            Course.COCONUT_MALL, Course.RAINBOW_ROAD_1),
    REDSTONE(Course.BLOCK_BLOCK_CLOUDTOPS, Course.GOATY_CIRCUIT,
            Course.SPOOKY_PLANKWALK, Course.ROYAL_RACEWAY),
    GLOWSTONE(Course.SHERBET_LAND, Course.MUSHROOM_GORGE,
            Course.DAISY_CIRCUIT, Course.DK_SUMMIT),
    LAPISLAZULI(Course.GCN_YOSHI_CIRCUIT, Course.ICE_OUTPOST,
            Course.PEACH_GARDENS, Course.AIRSHIP_FORTRESS),
    IRON(Course.LUIGIS_CIRCUIT, Course.BERLIN_BYWAY,
            Course.MOO_MOO_MEADOWS, Course.BIG_TREE),
    COPPER(Course.DONUT_PLAINS, Course.TOADS_TURNPIKE,
            Course.SUNSHINE_AIRPORT, Course.BOWSERS_CASTLE_2);

    public final Course firstTrack;
    public final Course secondTrack;
    public final Course thirdTrack;
    public final Course forthTrack;

    Cup(Course firstTrack, Course secondTrack, Course thirdTrack, Course forthTrack) {
        this.firstTrack = firstTrack;
        this.secondTrack = secondTrack;
        this.thirdTrack = thirdTrack;
        this.forthTrack = forthTrack;
    }
}
