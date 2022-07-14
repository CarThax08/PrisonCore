package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.commands.SellCommand;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.BlocksFileHandler;
import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.util.DataStore;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.Range;

import java.text.NumberFormat;
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
            if(!playerData.autosell) {
                event.getPlayer().sendMessage(ChatColor.RED + "Your backpack is full! Use /sellall to sell!");
            }else{
                SellCommand.sellItems(playerData.backpack, playerData.player);
            }
        }
        playerData.blocksBroken++;
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
        Location loc = BukkitAdapter.adapt(event.getBlock().getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (!query.testState(loc, localPlayer, Flags.BLOCK_BREAK)) {
            event.setCancelled(true);
            return;
        }
        List<ItemStack> drops = new ArrayList<>(event.getBlock().getDrops());
        for(ItemStack item : drops) {
            item.setAmount(item.getAmount() * (event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1));
            playerData.addItemToBackpack(item);
        }
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);

        NumberFormat format = NumberFormat.getNumberInstance();
        event.getPlayer().spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                                .replace("%amount%", format.format(playerData.getItemsInBackpack()))
                                .replace("%max%", format.format(playerData.backpackSize))
                        )
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
