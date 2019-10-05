package com.pam.brewcraft.proxy;

import com.pam.brewcraft.Reference;
import com.pam.brewcraft.brewcraft;
import com.pam.brewcraft.blocks.BlockRegistry;
import com.pam.brewcraft.blocks.CropRegistry;
import com.pam.brewcraft.gui.GuiHandler;
import com.pam.brewcraft.item.ItemRegistry;
import com.pam.brewcraft.item.SeedDropRegistry;
import com.pam.brewcraft.tileentities.TileEntityDistiller;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    	CropRegistry.registerCrops();
    	BlockRegistry.initBlockRegistry();
        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
        ItemRegistry.registerItems();
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        
        NetworkRegistry.INSTANCE.registerGuiHandler(brewcraft.instance, new GuiHandler());
        //RecipeRegistry.registerRecipes();
        
    }

    public void init(FMLInitializationEvent e) {
		onBlocksAndItemsLoaded();
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    public void onBlocksAndItemsLoaded() {
    	SeedDropRegistry.getSeedDrops();
    	
    	GameRegistry.registerTileEntity(TileEntityDistiller.class,  "distiller");
    	
    }
}
