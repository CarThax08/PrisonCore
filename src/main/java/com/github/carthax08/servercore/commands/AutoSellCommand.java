package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AutoSellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("autosell.toggle")){
            player.sendMessage(ChatColor.RED + "You don't have the proper permissions for that!");
            return true;
        }
        if(!DataStore.getPlayerData(player).autosell) {
            DataStore.getPlayerData(player).autosell = true;
            player.sendMessage(ChatColor.GREEN + "Autosell enabled!");
        }else{
            DataStore.getPlayerData(player).autosell = false;
            player.sendMessage(ChatColor.GREEN + "Autosell disabled!");
        }
        return true;
    }
}
