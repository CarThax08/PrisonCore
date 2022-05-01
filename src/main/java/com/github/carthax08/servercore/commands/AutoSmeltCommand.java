package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoSmeltCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){sender.sendMessage(ChatColor.RED + "You must be a player to run this command!"); return true;}
        Player player = (Player) sender;
        if(!player.hasPermission("autosmelt.toggle")){
            sender.sendMessage(ChatColor.RED + "You do not have the ability to run this command!");
            return true;
        }
        if(DataStore.getPlayerData(player).autosmelt) {
            DataStore.getPlayerData(player).autosmelt = false;
            player.sendMessage(ChatColor.RED + "Autosmelt disabled");
        }else{
            DataStore.getPlayerData(player).autosmelt = true;
            player.sendMessage(ChatColor.GREEN + "Autosmelt enabled");
        }
        return true;
    }
}
