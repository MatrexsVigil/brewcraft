package com.pam.brewcraft.item;

import java.util.Map;

import com.pam.brewcraft.brewcraft;
import com.pam.brewcraft.blocks.CropRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SeedDropRegistry {

    public static void getSeedDrops() {
        for (Map.Entry<String, Boolean> entry : brewcraft.config.seedDropFromGrass.entrySet()) {
            if (entry.getValue()) {
                final Item item = CropRegistry.getSeed(entry.getKey());

                if (item == null) continue;

                MinecraftForge.addGrassSeed(new ItemStack(item, 1, 0), brewcraft.config.seedrarity);
            }
        }
    }
}


