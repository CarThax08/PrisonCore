package com.github.carthax08.servercore;

import org.bukkit.Material;

public enum CustomEnchantment {
    EXPLOSIVE(12, 1000, 1, "Explosive", Material.TNT),
    GREED(13, 1500, 10, "Greed", Material.GOLD_INGOT);

    public final int slot, price, maxLevel;
    public final String displayName;
    public final Material displayMaterial;

    CustomEnchantment(int slot, int price, int maxLevel, String displayName, Material displayMaterial){
        this.slot = slot;
        this.price = price;
        this.maxLevel = maxLevel;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
    }
}
