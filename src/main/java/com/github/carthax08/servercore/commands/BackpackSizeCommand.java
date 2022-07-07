package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackpackSizeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender || sender.hasPermission("backpack.changesize")){
            if(args.length < 3){
                sender.sendMessage(ChatColor.RED + "Not enough arguments were provided! Make sure you provide and player, operation, and an amount!");
            }else{
                Player player = Bukkit.getPlayer(args[0]);
                if(player == null){
                    sender.sendMessage(ChatColor.RED + "Invalid player!");
                }
                switch (args[1]){
                    case "add":
                        DataStore.getPlayerData(player).backpackSize += Integer.parseInt(args[2]);
                        break;
                    case "set":
                        DataStore.getPlayerData(player).backpackSize = Integer.parseInt(args[2]);
                        break;
                    case "remove":
                        DataStore.getPlayerData(player).backpackSize -= Integer.parseInt(args[2]);
                        break;
                }
            }
        }
        return true;
    }
}
