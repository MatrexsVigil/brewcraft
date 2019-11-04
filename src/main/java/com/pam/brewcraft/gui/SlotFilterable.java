package com.pam.brewcraft.gui;

import java.util.function.Predicate;

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

public class SlotFilterable extends Slot {
    private final Predicate<ItemStack> filter;

    public SlotFilterable(IInventory inventory, int id, int x, int y, Predicate<ItemStack> filter) {
        super(inventory, id, x, y);
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return filter.test(stack);
    }
}
