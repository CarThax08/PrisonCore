package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't see vote links as the console!");
            return true;
        }
        Player player = (Player) sender;

        for (String string : Main.getInstance().getConfig().getStringList("votelinks")){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        }

        return true;
    }
}
