package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
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
        User user = Main.getPerms().getPlayerAdapter(Player.class).getUser(player);
        System.out.println(user.data());
        if(user.data().toCollection().contains(Node.builder("autosmelt.on").build())) {
            user.data().add(Node.builder("autosmelt.off").build());
            user.data().add(Node.builder("autosmelt.on").value(false).build());
            Main.getPerms().getUserManager().saveUser(user);
            player.sendMessage(ChatColor.RED + "Autosmelt disabled");
        }else{
            user.data().add(Node.builder("autosmelt.on").build());
            user.data().add(Node.builder("autosmelt.off").value(false).build());
            Main.getPerms().getUserManager().saveUser(user);
            player.sendMessage(ChatColor.GREEN + "Autosmelt enabled");
        }
        return true;
    }
}
