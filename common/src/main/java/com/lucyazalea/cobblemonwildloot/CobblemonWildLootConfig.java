package com.lucyazalea.cobblemonwildloot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CobblemonWildLootConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "config/CobblemonWildLoot.json";

    private int dropCheckTicks;
    private double dropChance;
    private String[] itemBlacklist;

    private CobblemonWildLootConfig() {
        this.dropCheckTicks = Defaults.DROP_CHECK_TICKS;
        this.dropChance = Defaults.DROP_CHANCE;
        this.itemBlacklist = Defaults.ITEM_BLACKLIST;
    }

    public static class Defaults {
        public static final int DROP_CHECK_TICKS = 1200;
        public static final double DROP_CHANCE = 0.1;
        public static final String[] ITEM_BLACKLIST = new String[]{
        };
        public static final boolean LEGACY_FLATTEN_ITEM_QUANTITY = false;
    }

    public int getDropCheckTicks() {
        return dropCheckTicks;
    }

    public double getDropChance() {
        return dropChance;
    }

    public String[] getItemBlacklist() {
        return itemBlacklist;
    }

    public static CobblemonWildLootConfig load() {
        CobblemonWildLootConfig config = new CobblemonWildLootConfig(); // Set defaults

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            config.dropCheckTicks = json.get("tick_per_minute").getAsInt();
            config.dropChance = json.get("drop_chance_per_minute").getAsFloat();
            config.itemBlacklist = GSON.fromJson(json.get("item_blacklist"), String[].class);
        } catch (IOException e) {
            config = new CobblemonWildLootConfig();
            config.save();
        }

        return config;
    }

    private void save() {
        JsonObject json = new JsonObject();
        json.addProperty("tick_per_minute", dropCheckTicks);
        json.addProperty("drop_chance_per_minute", dropChance);
        json.add("item_blacklist", GSON.toJsonTree(itemBlacklist));
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {

        }
    }
}
