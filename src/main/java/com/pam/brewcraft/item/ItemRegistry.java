package com.pam.brewcraft.item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pam.brewcraft.brewcraft;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class ItemRegistry {
	public static final List<Item> itemlist = new ArrayList<Item>();
    public static final HashMap<String, Item> items = new HashMap<String, Item>();
    
    public static Item beachrumitem;
    public static Item cherryvodkaitem;
    public static Item coconutrumitem;
    public static Item handmadevodkaitem;
    public static Item hardapplecideritem;
    public static Item hardlemonadeitem;
    public static Item honeymeaditem;
    public static Item pinkmoscatoitem;
    public static Item pmaxrieslingitem;
    public static Item rennalgolditem;
    public static Item riverchardonnayitem;
    public static Item sakeitem;
    public static Item savannazinfandelitem;
    public static Item spicedrumitem;
    public static Item swampmerlotitem;
    public static Item tiagapinotnoiritem;
    public static Item totalvodkaitem;
    public static Item uglyavocadoginitem;
    
    
	
    public static boolean initialized = false;

    public static void registerItems() {
        registerDrinkItems();
        initialized = true;
    }
    
    private static void registerDrinkItems() {
    	beachrumitem = registerGenericItem("beachrumitem");
        cherryvodkaitem = registerGenericItem("cherryvodkaitem");
        coconutrumitem = registerGenericItem("coconutrumitem");
        handmadevodkaitem = registerGenericItem("handmadevodkaitem");
        hardapplecideritem = registerGenericItem("hardapplecideritem");
        hardlemonadeitem = registerGenericItem("hardlemonadeitem");
        honeymeaditem = registerGenericItem("honeymeaditem");
        pinkmoscatoitem = registerGenericItem("pinkmoscatoitem");
        pmaxrieslingitem = registerGenericItem("pmaxrieslingitem");
        rennalgolditem = registerGenericItem("rennalgolditem");
        riverchardonnayitem = registerGenericItem("riverchardonnayitem");
        sakeitem = registerGenericItem("sakeitem");
        savannazinfandelitem = registerGenericItem("savannazinfandelitem");
        spicedrumitem = registerGenericItem("spicedrumitem");
        swampmerlotitem = registerGenericItem("swampmerlotitem");
        tiagapinotnoiritem = registerGenericItem("tiagapinotnoiritem");
        totalvodkaitem = registerGenericItem("totalvodkaitem");
        uglyavocadoginitem = registerGenericItem("uglyavocadoginitem");
    }

    private static Item registerGenericItem(String registryName) {
        final Item item = new Item();
        return registerItem(item, registryName);
    }


    public static Item registerItem(Item item, String registryName) {
        item.setCreativeTab(brewcraft.tabBrewcraft);
        item.setRegistryName(registryName);
        item.setUnlocalizedName(registryName);
        itemlist.add(item);
        return item;
    }
    
    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> reg = e.getRegistry();
        reg.registerAll(itemlist.toArray(new Item[0]));
        //GeneralOreRegistry.initOreRegistry();
    }
    
   
}