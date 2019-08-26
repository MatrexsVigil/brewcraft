package com.pam.brewcraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    	BlockRegistry.initBlockRegistry();
        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
        ItemRegistry.registerItems();
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        //RecipeRegistry.registerRecipes();
        
    }

    public void init(FMLInitializationEvent e) {
		onBlocksAndItemsLoaded();
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    public void onBlocksAndItemsLoaded() {

    }
}
