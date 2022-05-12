package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.PlayerDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TokensCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player){
            sender.sendMessage(ChatColor.GREEN + "Tokens: " + DataStore.getPlayerData((Player) sender).tokenBalance);
            return true;
        }
        if(!sender.hasPermission("tokens.edit")){return true;}
        if(args.length < 3){
            sender.sendMessage(ChatColor.RED + command.getUsage());
        }else{
            switch (args[0].toLowerCase()){
                case "set":
                    handleSet(args);
                    break;
                case "add":
                    handleAdd(args);
                    break;
                case "remove":
                    handleRemove(args);
                    break;
                case "clear":
                    handleClear(args);
            }
        }
        return true;
    }

    private void handleSet(String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        DataStore.getPlayerData(player).tokenBalance = Double.parseDouble(args[2]);
        DataStore.getPlayerData(player).savePlayerData(false);
    }
    private void handleAdd(String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        double amount = Double.parseDouble(args[2]);
        DataStore.getPlayerData(player).tokenBalance += amount;
        DataStore.getPlayerData(player).savePlayerData(false);
    }
    private void handleRemove(String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        double amount = Double.parseDouble(args[2]);
        DataStore.getPlayerData(player).tokenBalance -= amount;
        DataStore.getPlayerData(player).savePlayerData(false);
    }
    private void handleClear(String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        DataStore.getPlayerData(player).tokenBalance = 0;
        DataStore.getPlayerData(player).savePlayerData(false);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        if(sender.hasPermission("tokens.edit") && args.length == 0){
            tabComplete.add("add");
            tabComplete.add("set");
            tabComplete.add("remove");
            tabComplete.add("clear");
        }
        return tabComplete;
    }
}