package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.util.DataStore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MultiplierCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof ConsoleCommandSender)){
            sender.sendMessage(ChatColor.RED + "Nice try there. You have to run that as console.");
            return true;
        }
        if(args.length < 2){
            sender.sendMessage(ChatColor.RED + "Not enough arguments! Are you sure you provided a player and amount");
            return true;
        }

        double multi = Double.parseDouble(args[1]);
        Player player = Bukkit.getPlayer(args[0]);
        assert player != null;
        DataStore.getPlayerData(player).sellMultiplier += multi;
        sender.sendMessage(ChatColor.GREEN + "Success!");
        player.sendMessage(ChatColor.GREEN + "Your multiplier has been increased by " + multi + ".");

        return true;
    }
}
