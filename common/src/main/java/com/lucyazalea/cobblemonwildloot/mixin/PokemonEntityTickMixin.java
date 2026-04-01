package com.lucyazalea.cobblemonwildloot.mixin;

import com.lucyazalea.cobblemonwildloot.CobblemonWildLootConfig;
import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.FormData;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.*;

@Mixin(PokemonEntity.class)
public class PokemonEntityTickMixin {
    @Unique
    private static final CobblemonWildLootConfig CONFIG = CobblemonWildLootConfig.load();

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        // 10% drop rate per minute with default config.
        double dropChance = CONFIG.getDropChance() / CONFIG.getDropCheckTicks();
        double randomNumber = Math.random();
        if (randomNumber < dropChance) {
            PokemonEntity entity = (PokemonEntity) (Object) this;
            if (entity == null) return;
            var pokemon = entity.getPokemon();
            if (pokemon == null) return;
            var world = entity.level();

            if (!pokemon.isFainted() && !pokemon.isBattleClone() && !pokemon.isNPCOwned() && !pokemon.isUncatchable()) {
                try {
                    FormData form = pokemon.getForm();
                    DropTable dropTable = form.getDrops();
                    List<DropEntry> drops = dropTable.getDrops(dropTable.getAmount(), pokemon);
                    if (drops.isEmpty()) return;

                    DropEntry drop = drops.get(world.random.nextInt(drops.size()));
                    if (drop instanceof ItemDropEntry itemDropEntry && !Arrays.asList(CONFIG.getItemBlacklist()).contains(itemDropEntry.getItem().toString())) {
                        Item item = world.registryAccess().registryOrThrow(Registries.ITEM).get(itemDropEntry.getItem());
                        if (item != null) {
                            ItemStack stack = new ItemStack(item, 1);
                            world.addFreshEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), stack));
                            // TODO logging
                        }
                    }
                } catch (Exception e) {
                    // TODO logging
                }
            }
        }
    }
}