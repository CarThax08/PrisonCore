package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.prestige.Prestige;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import com.github.carthax08.servercore.util.DataStore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrestigeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        } else {
            Player player = (Player) sender;
            if (player.hasPermission("prestige.canprestige")) {
                ServerPlayer playerData = DataStore.getPlayerData(player);
                if (playerData != null) {
                    Prestige nextPrestige = PrestigeHandler.getPrestigeByIndex(playerData.pindex + 1);
                    assert nextPrestige != null;
                    if (playerData.getMoney() >= nextPrestige.cost) {
                        if(!playerData.removeMoney(nextPrestige.cost)){
                            player.sendMessage(ChatColor.RED + "An unknown error has occurred when trying to take money away. Prestige did not succeed.");
                            return true;
                        }
                        LuckPerms perms = Main.getPerms();
                        User user = perms.getPlayerAdapter(Player.class).getUser(player);
                        user.data().remove(Node.builder("group." + DataStore.lastRankGroup).build());
                        for (String string : nextPrestige.permissions) {
                            user.data().add(Node.builder(string).build());
                        }
                        playerData.pindex += 1;
                        playerData.prestige = nextPrestige;
                        perms.getUserManager().saveUser(user);
                        playerData.savePlayerData(false);
                        player.sendMessage(ChatColor.GREEN + "Successfully prestiged!");
                    } else {
                        player.sendMessage("You do not have enough money to prestige!");
                    }
                }
            }else{
                player.sendMessage("You are not the highest rank!");
            }
        }
        return true;
    }
}
