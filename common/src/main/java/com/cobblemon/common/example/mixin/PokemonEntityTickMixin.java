package com.cobblemon.common.example.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;

@Mixin(PokemonEntity.class)
public class PokemonEntityTickMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        PokemonEntity self = (PokemonEntity) (Object) this;
        var pokemon = self.getPokemon();
        self.remove(Entity.RemovalReason.KILLED);
    }
}