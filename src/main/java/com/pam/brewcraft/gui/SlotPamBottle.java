package com.pam.brewcraft.gui;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotPamBottle extends SlotItemHandler {

	public SlotPamBottle(IItemHandler tileEntityDistiller, int index, int x, int y) {
		super(tileEntityDistiller, index, x, y);
	}
	
	
	public boolean isItemValid(ItemStack stack)
    {
        if (stack.getItem() == Items.POTIONITEM && PotionUtils.getPotionFromItem(stack) == PotionTypes.WATER)
			return true;
			
			return false;
    }


}
