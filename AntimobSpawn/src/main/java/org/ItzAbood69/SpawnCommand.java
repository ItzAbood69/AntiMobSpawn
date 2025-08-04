package com.yourname.nomobspawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpawnCommand implements CommandExecutor {

    private final NoMobSpawn plugin;

    public SpawnCommand(NoMobSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eUsage: /nomobspawn reload - Reload plugin configuration");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("nomobspawn.reload")) {
                plugin.reloadPluginConfig();
                sender.sendMessage("§aPlugin configuration reloaded successfully!");
                return true;
            } else {
                sender.sendMessage("§cYou don't have permission to do that!");
                return true;
            }
        }

        sender.sendMessage("§cUnknown command! Use /nomobspawn reload");
        return true;
    }
}