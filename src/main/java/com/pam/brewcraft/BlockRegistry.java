package com.pam.brewcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class BlockRegistry {
	public static final List<Block> blocks = new ArrayList<Block>();

    private static boolean initialized = false;

    public static void initBlockRegistry() {

        initialized = true;
    }

    
    public static void registerBlock(String registerName, ItemBlock itemBlock, Block block) {
        block.setRegistryName(registerName);
        block.setUnlocalizedName(registerName);
        block.setCreativeTab(brewcraft.tabBrewcraft);
        blocks.add(block);
        //GameRegistry.register(block);

        itemBlock.setRegistryName(registerName);
        itemBlock.setUnlocalizedName(registerName);
        //GameRegistry.register(itemBlock);
    }

    public static void registerBlock(String registerName, Block block) {
        final ItemBlock itemBlock = new ItemBlock(block);
        registerBlock(registerName, itemBlock, block);
    }

    
    @SubscribeEvent
    public void onBlockRegistry(RegistryEvent.Register<Block> e) {
        IForgeRegistry<Block> reg = e.getRegistry();
        reg.registerAll(blocks.toArray(new Block[0]));        
    }
}
