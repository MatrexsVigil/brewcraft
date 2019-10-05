package com.pam.brewcraft.gui;

import com.pam.brewcraft.tileentities.TileEntityDistiller;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int GUIID_DISTILLER = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if(ID == GUIID_DISTILLER) {
			return new ContainerDistiller(player.inventory, (TileEntityDistiller) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if(ID == GUIID_DISTILLER) {
			return new GuiDistiller(player.inventory, (TileEntityDistiller) tileEntity);
		}
		return null;
	}

}
