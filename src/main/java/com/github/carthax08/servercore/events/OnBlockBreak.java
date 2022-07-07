package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.Util;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        ServerPlayer playerData = DataStore.getPlayerData(event.getPlayer());
        if(playerData.getItemsInBackpack() >= playerData.backpackSize){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Your backpack is full! Use /sellall to sell!");
            return;
        }
        playerData.blocksBroken++;
        RegionContainer manager = WorldGuardPlugin.inst().getRegionContainer();
        RegionQuery query = manager.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(event.getBlock().getLocation());
        if(set.size() > 0){
            if (query.queryState(event.getBlock().getLocation(), WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), DefaultFlag.BLOCK_BREAK).equals(StateFlag.State.DENY)){
                return;
            }
        }
        List<ItemStack> drops = new ArrayList<>();
        if(playerData.autosmelt){
            for (ItemStack item : event.getBlock().getDrops()) {
                ItemStack result = attemptSmelt(item);
                drops.add(result != null ? result : item);
            }
        }
        Random random = new Random();
        if(random.nextInt(1000) >= 990){
            int tokens = Math.max(10, random.nextInt(50));
            playerData.tokenBalance += tokens;
            playerData.savePlayerData(false);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You randomly found " + tokens + " tokens!");

        }
        if(drops.isEmpty()){
            drops.addAll(event.getBlock().getDrops());
        }
        for(ItemStack item : drops) {
            playerData.addItemToBackpack(item);
        }
        event.getBlock().setType(Material.AIR);

        event.getPlayer().spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(Main.backpackBarFormat
                        .replace("%amount%", String.valueOf(playerData.getItemsInBackpack()))
                        .replace("%max%", String.valueOf(playerData.backpackSize))
                )
        );

    }

    private ItemStack attemptSmelt(ItemStack item) {
        ItemStack result = null;
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (!(recipe instanceof FurnaceRecipe)) continue;
            FurnaceRecipe frecipe = (FurnaceRecipe) recipe;
            if ((frecipe.getInput().getType() != item.getType())) continue;
            result = frecipe.getResult();
            break;
        }
        return result;
    }
}
