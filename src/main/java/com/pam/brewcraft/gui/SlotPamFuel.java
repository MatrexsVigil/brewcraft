package com.pam.brewcraft.gui;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotPamFuel extends SlotItemHandler {

	public SlotPamFuel(IItemHandler tileEntityDistiller, int index, int x, int y) {
		super(tileEntityDistiller, index, x, y);
	}
	
	
	public boolean isItemValid(ItemStack stack)
    {
        return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return SlotFurnaceFuel.isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }


}
