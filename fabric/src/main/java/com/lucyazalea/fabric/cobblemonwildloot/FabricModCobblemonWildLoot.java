package com.lucyazalea.fabric.cobblemonwildloot;

import com.lucyazalea.cobblemonwildloot.CobblemonWildLoot;
import net.fabricmc.api.ModInitializer;

public class FabricModCobblemonWildLoot implements ModInitializer {
    @Override
    public void onInitialize() {
        CobblemonWildLoot.init();
    }
}
