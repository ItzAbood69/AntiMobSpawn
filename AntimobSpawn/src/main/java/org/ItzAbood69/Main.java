package com.yourname.nomobspawn;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class NoMobSpawn extends JavaPlugin implements Listener {

    private List<World> disabledWorlds;
    private List<EntityType> allowedMobs;

    @Override
    public void onEnable() {
        // Initialize lists
        disabledWorlds = new ArrayList<>();
        allowedMobs = new ArrayList<>();

        // Load configuration
        saveDefaultConfig();
        loadConfig();

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Register command
        getCommand("nomobspawn").setExecutor(new SpawnCommand(this));

        getLogger().info("§aNoMobSpawn plugin enabled successfully!");
    }

    private void loadConfig() {
        // Load disabled worlds
        for (String worldName : getConfig().getStringList("disabled-worlds")) {
            World world = getServer().getWorld(worldName);
            if (world != null) {
                disabledWorlds.add(world);
            } else {
                getLogger().warning("World '" + worldName + "' not found!");
            }
        }

        // Load allowed mobs
        for (String mobName : getConfig().getStringList("allowed-mobs")) {
            try {
                EntityType type = EntityType.valueOf(mobName.toUpperCase());
                allowedMobs.add(type);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid mob type: " + mobName);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        World world = event.getLocation().getWorld();

        if (disabledWorlds.contains(world)) {
            if (allowedMobs.isEmpty() || allowedMobs.contains(event.getEntityType())) {
                // Allow spawning
                event.setCancelled(false);
            } else {
                // Prevent spawning
                event.setCancelled(true);
            }
        }
    }

    public void reloadPluginConfig() {
        reloadConfig();
        disabledWorlds.clear();
        allowedMobs.clear();
        loadConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("§cNoMobSpawn plugin disabled!");
    }
}