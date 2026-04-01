package com.cobblemon.common.example.mixin;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static java.lang.System.*;

@Mixin(PokemonEntity.class)
public class PokemonEntityTickMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        // 20% drop rate per minute.
        double dropChance = 1.0 / 1200.0 / 20.0;
        double randomNumber = Math.random();
        if (randomNumber < 0.001) {
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

                    int dropCount = (int)(Math.random() * drops.size());
                    if (dropCount == 0) return;
                    DropEntry drop = drops.get(dropCount);
                    if (drop instanceof ItemDropEntry itemDropEntry) {
                        Item item = world.registryAccess().registryOrThrow(Registries.ITEM).get(itemDropEntry.getItem());
                        if (item != null) {
                            ItemStack stack = new ItemStack(item, 1);
                            world.addFreshEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), stack));
                            out.println("Dropped item.");
                        }
                    }
                } catch (Exception e) {
                    out.println("Error while dropping loot.");
                }
            }
        }
    }
}