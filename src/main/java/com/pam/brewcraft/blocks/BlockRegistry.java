package com.pam.brewcraft.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pam.brewcraft.brewcraft;
import com.pam.brewcraft.blocks.blocks.DistillerBlock;
import com.pam.brewcraft.item.ItemRegistry;

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
	
	public static final String distillerItemName = "distiller";
	public static DistillerBlock distiller;
	public static ItemBlock distillerItemBlock;

    private static boolean initialized = false;

    public static void initBlockRegistry() {
    	registerDistiller();
        initialized = true;
    }
    private static void registerDistiller() {
    	distiller = new DistillerBlock();
		distillerItemBlock = new ItemBlock(distiller);
		ItemRegistry.items.put(DistillerBlock.registryName, distillerItemBlock);
		registerBlock(DistillerBlock.registryName, distillerItemBlock, distiller);
    }
    
    public static void registerBlock(String registerName, ItemBlock itemBlock, Block block) {
        block.setRegistryName(registerName);
        block.setUnlocalizedName(registerName);
        block.setCreativeTab(brewcraft.tabBrewcraft);
        blocks.add(block);

        if (itemBlock != null)
        {
        itemBlock.setRegistryName(registerName);
        itemBlock.setUnlocalizedName(registerName);
        ItemRegistry.itemlist.add(itemBlock);
        }
        return;
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
