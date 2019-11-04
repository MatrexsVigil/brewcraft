package com.pam.brewcraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pam.brewcraft.blocks.CropRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DistillerRecipes {
    private static final Map<ItemStack, ItemStack[]> distillingList = new HashMap<ItemStack, ItemStack[]>();

    static {
    	
        registerItemRecipe(ItemRegistry.preparedbeachrummixitem, ItemRegistry.beachrumitem, ItemRegistry.beachrumitem);
        registerItemRecipe(ItemRegistry.preparedcherryvodkamixitem, ItemRegistry.cherryvodkaitem, ItemRegistry.cherryvodkaitem);
        registerItemRecipe(ItemRegistry.preparedcoconutrummixitem, ItemRegistry.coconutrumitem, ItemRegistry.coconutrumitem);
        registerItemRecipe(ItemRegistry.preparedhandmadevodkamixitem, ItemRegistry.handmadevodkaitem, ItemRegistry.handmadevodkaitem);
        registerItemRecipe(ItemRegistry.preparedhardapplecidermixitem, ItemRegistry.hardapplecideritem, ItemRegistry.hardapplecideritem);
        registerItemRecipe(ItemRegistry.preparedhardlemonademixitem, ItemRegistry.hardlemonadeitem, ItemRegistry.hardlemonadeitem);
        registerItemRecipe(ItemRegistry.preparedhoneymeadmixitem, ItemRegistry.honeymeaditem, ItemRegistry.honeymeaditem);
        registerItemRecipe(ItemRegistry.preparedpinkmoscatomixitem, ItemRegistry.pinkmoscatoitem, ItemRegistry.pinkmoscatoitem);
        registerItemRecipe(ItemRegistry.preparedpmaxrieslingmixitem, ItemRegistry.pmaxrieslingitem, ItemRegistry.pmaxrieslingitem);
        registerItemRecipe(ItemRegistry.preparedrennalgoldmixitem, ItemRegistry.rennalgolditem, ItemRegistry.rennalgolditem);
        registerItemRecipe(ItemRegistry.preparedriverchardonnaymixitem, ItemRegistry.riverchardonnayitem, ItemRegistry.riverchardonnayitem);
        registerItemRecipe(ItemRegistry.preparedsakemixitem, ItemRegistry.sakeitem, ItemRegistry.sakeitem);
        registerItemRecipe(ItemRegistry.preparedsavannazinfandelmixitem, ItemRegistry.savannazinfandelitem, ItemRegistry.savannazinfandelitem);
        registerItemRecipe(ItemRegistry.preparedspicedrummixitem, ItemRegistry.spicedrumitem, ItemRegistry.spicedrumitem);
        registerItemRecipe(ItemRegistry.preparedswampmerlotmixitem, ItemRegistry.swampmerlotitem, ItemRegistry.swampmerlotitem);
        registerItemRecipe(ItemRegistry.preparedtiagapinotnoirmixitem, ItemRegistry.tiagapinotnoiritem, ItemRegistry.tiagapinotnoiritem);
        registerItemRecipe(ItemRegistry.preparedtotalvodkamixitem, ItemRegistry.totalvodkaitem, ItemRegistry.totalvodkaitem);
        registerItemRecipe(ItemRegistry.prepareduglyavocadoginmixitem, ItemRegistry.uglyavocadoginitem, ItemRegistry.uglyavocadoginitem);

        
        
       
        
        //registerBlockRecipe(Blocks.LOG2, Items.PAPER, Items.PAPER);
        
        
    }

    private static void registerItemRecipe(Item input, Item leftItem, Item rightItem) {
        final ItemStack outputLeft = leftItem != null ? new ItemStack(leftItem) : ItemStack.EMPTY;
        final ItemStack outputRight = rightItem != null ? new ItemStack(rightItem) : ItemStack.EMPTY;

        makeItemStackRecipe(new ItemStack(input, 1, 32767), outputLeft, outputRight);
    }

    private static void registerBlockRecipe(Block input, Item leftItem, Item rightItem) {
        registerItemRecipe(Item.getItemFromBlock(input), leftItem, rightItem);
    }

    private static void makeItemStackRecipe(ItemStack input, ItemStack outputLeft, ItemStack outputRight) {
        final ItemStack[] outputs = new ItemStack[] {outputLeft, outputRight};
        distillingList.put(input, outputs);
    }

    public static ItemStack[] getDistillingResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack[]> entry : distillingList.entrySet()) {
            if (isSameItem(input, entry.getKey())) return entry.getValue();
        }

        return null;
    }

    private static boolean isSameItem(ItemStack stack, ItemStack stack2) {
        return stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == 32767 || stack2.getItemDamage() == stack.getItemDamage());
    }
}
