package com.lucyazalea.cobblemonwildloot.mixin;

import com.lucyazalea.cobblemonwildloot.CobblemonWildLoot;
import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.FormData;
import com.lucyazalea.cobblemonwildloot.PokebasketEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.*;

@Mixin(PokemonEntity.class)
public class PokemonEntityTickMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        PokemonEntity entity = (PokemonEntity) (Object) this;
        if (entity == null) return;
        var world = entity.level();
        if (world == null || world.isClientSide) return;

        double dropChance = 12 * CobblemonWildLoot.CONFIG.getDropChance() / CobblemonWildLoot.CONFIG.getDropCheckTicks();
        double randomNumber = Math.random();
        if (randomNumber < dropChance) {
            var pokemon = entity.getPokemon();
            if (pokemon == null) return;

            if (!pokemon.isFainted() && !pokemon.isBattleClone() && !pokemon.isNPCOwned() && !pokemon.isUncatchable()) {
                try {
                    FormData form = pokemon.getForm();
                    DropTable dropTable = form.getDrops();
                    List<DropEntry> drops = dropTable.getDrops(dropTable.getAmount(), pokemon);

                    if (drops.isEmpty()) return;
                    DropEntry drop = drops.get(world.random.nextInt(drops.size()));

                    if (drop instanceof ItemDropEntry itemDropEntry && !CobblemonWildLoot.CONFIG.getItemBlacklist().contains(itemDropEntry.getItem().toString())) {
                        var droppedIntoBasket = false;
                        BlockPos centerPos = entity.blockPosition();
                        for (var pos : BlockPos.betweenClosed(centerPos.offset(-12, -12, -12), centerPos.offset(12, 12, 12))) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity instanceof PokebasketEntity basket) {
                                var item = world.registryAccess().registryOrThrow(Registries.ITEM).get(itemDropEntry.getItem());
                                if (item != null) {
                                    var range = itemDropEntry.getQuantityRange();
                                    out.printf("item: %s", itemDropEntry.getItem().toString());
                                    var quantity = range != null ? world.random.nextInt(range.getStart(), range.getEndInclusive()) : itemDropEntry.getQuantity();
                                    var stack = new ItemStack(item, quantity);
                                    if (basket.addStack(stack)) {
                                        droppedIntoBasket = true;
                                    }
                                }
                            }
                        }
                        if (!droppedIntoBasket) {
                            itemDropEntry.drop((LivingEntity) entity, (ServerLevel) world, entity.position(), null);
                        }
                    }
                } catch (Exception e) {
                    out.println("Error: " + e.toString());
                }
            }
        }
    }
}