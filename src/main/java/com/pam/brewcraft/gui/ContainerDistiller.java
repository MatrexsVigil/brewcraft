package com.pam.brewcraft.gui;

import javax.annotation.Nullable;

import com.pam.brewcraft.item.DistillerRecipes;
import com.pam.brewcraft.tileentities.TileEntityDistiller;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerDistiller extends Container {
	
	private TileEntityDistiller entityDistiller;
	private int lastCookTime = 0;
	
	public ContainerDistiller(InventoryPlayer inventory, TileEntityDistiller entityDistiller) {
        this.entityDistiller = entityDistiller;
        
        addSlotToContainer(new SlotPamDistiller(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 0, 18, 17));
        addSlotToContainer(new SlotPamDistiller(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 1, 37, 17));
        addSlotToContainer(new SlotPamDistiller(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 2, 56, 17));
        addSlotToContainer(new SlotPamBottle(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 3, 18, 53));
        addSlotToContainer(new SlotPamFuel(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 4, 56, 53));
        addSlotToContainer(new SlotPamResult(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 5, 116, 35));
        addSlotToContainer(new SlotPamResult(entityDistiller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 6, 135, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inventory, i, i * 18 + 8, 142));
        }
    }


	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener crafting : listeners) {
            if (lastCookTime == entityDistiller.cookTime) continue;
            crafting.sendWindowProperty(this, 0, entityDistiller.cookTime);
        }
        lastCookTime = entityDistiller.cookTime;
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
        	entityDistiller.cookTime = (short) data;
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getStack() != ItemStack.EMPTY) {
            final ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index == 1 || index == 2) {
                if (!this.mergeItemStack(slotStack, 3, 39, true)) return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, itemStack);
            } else if (index >= 3) {
                if (DistillerRecipes.getDistillerResult(slotStack) != null) {
                    if (!mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!mergeItemStack(slotStack, 30, 39, false)) return ItemStack.EMPTY;
                } else if (index >= 30 && index < 39){
                    if (!mergeItemStack(slotStack, 3, 30, false)) return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 3, 39, false)) return ItemStack.EMPTY;

            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack.getCount() == slotStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(playerIn, slotStack);
        }

        return itemStack;
    }
	
	

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	
}
