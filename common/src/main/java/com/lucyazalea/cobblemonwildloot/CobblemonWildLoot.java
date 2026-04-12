package com.lucyazalea.cobblemonwildloot;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Supplier;

public final class CobblemonWildLoot {
    public static final String MOD_ID = "cobblemonwildloot";
    public static final CobblemonWildLootConfig CONFIG = CobblemonWildLootConfig.load();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CobblemonWildLoot.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(CobblemonWildLoot.MOD_ID, Registries.BLOCK);

    public static RegistrySupplier<Item> POKEBASKET_ITEM;
    public static RegistrySupplier<Block> POKEBASKET_BLOCK;

    public static void init() {
        LOGGER.info("Initializing Cobblemon Wild Loot Mod.");

        POKEBASKET_BLOCK = BLOCKS.register("pokebasket", () -> new Block(Block.Properties.of()));
        POKEBASKET_ITEM = ITEMS.register("pokebasket", () -> new BlockItem(POKEBASKET_BLOCK.get(), new Item.Properties()));
        ITEMS.register();
        BLOCKS.register();
    }
}