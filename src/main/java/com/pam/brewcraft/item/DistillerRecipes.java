package com.pam.brewcraft.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DistillerRecipes {
    private static final Map<ItemStack, ItemStack[]> distillingList = new HashMap<ItemStack, ItemStack[]>();

    static {
    	//Bee Stuff
    	//registerItemRecipe(ItemRegistry.waxcombItem, ItemRegistry.beeswaxItem, ItemRegistry.beeswaxItem);
    	//registerItemRecipe(ItemRegistry.honeycombItem, ItemRegistry.honeyItem, ItemRegistry.beeswaxItem);
        
        registerItemRecipe(Items.REEDS, Items.REEDS, Items.REEDS, ItemRegistry.beachrumitem, Items.SUGAR);
    }

    private static void registerItemRecipe(Item input, Item input2, Item input3, Item leftItem, Item rightItem) {
        final ItemStack outputLeft = leftItem != null ? new ItemStack(leftItem) : ItemStack.EMPTY;
        final ItemStack outputRight = rightItem != null ? new ItemStack(rightItem) : ItemStack.EMPTY;

        makeItemStackRecipe(new ItemStack(input, 1, 32767), new ItemStack(input2, 1, 32767), new ItemStack(input3, 1, 32767), outputLeft, outputRight);
    }

    private static void registerBlockRecipe(Block input, Block input2, Block input3, Item leftItem, Item rightItem) {
        registerItemRecipe(Item.getItemFromBlock(input), Item.getItemFromBlock(input2), Item.getItemFromBlock(input3), leftItem, rightItem);
    }

    private static void makeItemStackRecipe(ItemStack input, ItemStack input2, ItemStack input3, ItemStack outputLeft, ItemStack outputRight) {
        final ItemStack[] outputs = new ItemStack[] {outputLeft, outputRight};
        distillingList.put(input, outputs);
    }

    public static ItemStack[] getDistillerResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack[]> entry : distillingList.entrySet()) {
            if (isSameItem(input, entry.getKey())) return entry.getValue();
        }

        return null;
    }

    private static boolean isSameItem(ItemStack stack, ItemStack stack2) {
        return stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == 32767 || stack2.getItemDamage() == stack.getItemDamage());
    }
}
