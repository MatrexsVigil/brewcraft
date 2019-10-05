package com.pam.brewcraft.gui;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotPamResult extends SlotItemHandler {

	public SlotPamResult(IItemHandler tileEntityDistiller, int index, int x, int y) {
		super(tileEntityDistiller, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
