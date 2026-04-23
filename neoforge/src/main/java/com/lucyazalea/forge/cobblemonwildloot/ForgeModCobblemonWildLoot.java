package com.lucyazalea.forge.cobblemonwildloot;

import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.google.gson.GsonBuilder;
import com.lucyazalea.cobblemonwildloot.CobblemonWildLoot;
import com.lucyazalea.cobblemonwildloot.PokebasketEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Mod(CobblemonWildLoot.MOD_ID)
public class ForgeModCobblemonWildLoot {
    public ForgeModCobblemonWildLoot() {
        CobblemonWildLoot.init();

        CobblemonWildLoot.POKEBASKET_ENTITY = CobblemonWildLoot.registerBlockEntity("pokebasket", () -> new BlockEntityType<>(PokebasketEntity::new, Set.of(CobblemonWildLoot.POKEBASKET_BLOCK.get()), null));
        CobblemonWildLoot.registerBlockEntities();

        NeoForge.EVENT_BUS.register(this);
    }

    public static class SpeciesData {
        public SpeciesData(String _id, int _dex, String _type1, String _type2, DropTable _drops) {
            id = _id;
            dex = _dex;
            primaryType = _type1;
            secondaryType = _type2;
            drops = _drops;
        }

        public String id;
        public int dex;
        public String primaryType;
        public String secondaryType;
        public DropTable drops;
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        // This runs after datapacks are fully loaded
        var speciesList = PokemonSpecies.getSpecies();
        var speciesData = new ArrayList<SpeciesData>();

        System.out.println("Total species found: " + speciesList.size());
        for (var species : speciesList) {
            var primaryType = species.getPrimaryType().getShowdownId();
            var secondaryType = species.getSecondaryType() != null ? species.getSecondaryType().getShowdownId() : null;
            speciesData.add(new SpeciesData(species.showdownId(), species.getNationalPokedexNumber(), primaryType, secondaryType, species.getDrops()));
        }

        try (FileWriter writer = new FileWriter("species_drop_data.json")) {
            new GsonBuilder().setPrettyPrinting().create().toJson(speciesData, writer);
        } catch (IOException e) {/**/}
    }
}
