package com.lucyazalea.cobblemonwildloot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CobblemonWildLoot {
    public static final String MOD_ID = "cobblemonwildloot";
    public static final CobblemonWildLootConfig CONFIG = CobblemonWildLootConfig.load();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Cobblemon Wild Loot Mod.");

        //TutorialModBlocks.initBlocks();
        //TutorialModItems.initItems();
    }
}