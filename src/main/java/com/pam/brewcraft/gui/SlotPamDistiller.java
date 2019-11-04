package com.pam.brewcraft.gui;

import com.pam.brewcraft.item.DistillerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotPamDistiller extends SlotItemHandler {
    
    private final int slot;
    
    public SlotPamDistiller(IItemHandler inventory, int index, int xPos, int yPos) {
        super(inventory, index, xPos, yPos);
        this.slot = index;
    }

    @Override
	public boolean isItemValid(ItemStack stack) {
		if(DistillerRecipes.getDistillingResult(stack) != null)
			return true;
		return false;
	}
}
