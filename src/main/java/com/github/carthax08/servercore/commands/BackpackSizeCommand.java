package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
                    return true;
                }
                ServerPlayer playerData = DataStore.getPlayerData(player);
                switch (args[1]){
                    case "add":
                        playerData.backpackSize += Integer.parseInt(args[2]);
                        break;
                    case "set":
                        playerData.backpackSize = Integer.parseInt(args[2]);
                        break;
                    case "remove":
                        playerData.backpackSize -= Integer.parseInt(args[2]);
                        break;
                }
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent(
                                ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                                        .replace("%amount%", String.valueOf(playerData.getItemsInBackpack()))
                                        .replace("%max%", String.valueOf(playerData.backpackSize))
                                )
                        )
                );
            }
        }
        return true;
    }
}
