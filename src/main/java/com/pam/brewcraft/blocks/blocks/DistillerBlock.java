package com.pam.brewcraft.blocks.blocks;

import com.pam.brewcraft.ItemStackUtils;
import com.pam.brewcraft.brewcraft;
import com.pam.brewcraft.gui.GuiHandler;
import com.pam.brewcraft.tileentities.TileEntityDistiller;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class DistillerBlock extends BlockContainerRotating {

	public static final String registryName = "distiller";
	private static boolean keepInventory;
	

	public DistillerBlock() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setCreativeTab(brewcraft.tabBrewcraft);
		this.setHardness(1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDistiller();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// keepInventory = true;
		// if(!keepInventory) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileEntityDistiller) {
			ItemStackUtils.dropInventoryItems(worldIn, pos,
					tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		// }

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if(!(te instanceof TileEntityDistiller)) {
			return false;
		}
		player.openGui(brewcraft.instance, GuiHandler.GUIID_DISTILLER, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	

}
