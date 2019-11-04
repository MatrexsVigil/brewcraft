package com.pam.brewcraft.tileentities;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.pam.brewcraft.blocks.blocks.DistillerBlock;
import com.pam.brewcraft.item.DistillerRecipes;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class TileEntityDistiller extends TileEntity implements ITickable{
	public short cookTime;
	public int heat;
	@CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEMS_CAP;

    private final ItemStackHandler itemstackhandler = new ItemStackHandler(7);
    private final RangedWrapper top = new RangedWrapper(itemstackhandler, 0, 4);
    
    private final RangedWrapper bottom = new RangedWrapper(itemstackhandler, 5, 6)
    {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            return stack;
        }
    };
    
    public ItemStackHandler getInventory()
    {
        return itemstackhandler;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == ITEMS_CAP)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == ITEMS_CAP)
        {
            if (facing == EnumFacing.UP) return (T)top;
            if (facing == EnumFacing.DOWN) return (T)bottom;
            if (facing != null) return (T)top;
            return (T)itemstackhandler;
        }

        return super.getCapability(capability, facing);
    }

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		ITEMS_CAP.readNBT(itemstackhandler, null, compound.getTag("Items"));
		cookTime = compound.getShort("CookTime");
		this.heat = compound.getInteger("Heat");
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setShort("CookTime", cookTime);
		compound.setInteger("Heat", (short)this.heat);
		compound.setTag("Items", ITEMS_CAP.writeNBT(itemstackhandler, null));

		return super.writeToNBT(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@SideOnly(value = Side.CLIENT)
	public int getCookProgressScaled(int scale) {
		return cookTime * scale / 125;
	}

	@Override
	public void update() {
		boolean needsUpdate = false;

		if(world.isRemote)
			return;

		if(canRun()) {
			++cookTime;

			if(cookTime >= 125) {
				cookTime = 0;
				distillBooze();
				needsUpdate = true;
			}
		}
		else {
			cookTime = 0;
		}

		if(needsUpdate != cookTime > 0) {
			needsUpdate = true;
		}

		if(needsUpdate) {
			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}
	
	public int getHeat() {
        final BlockPos posBelow = pos.add(0, -1, 0);
        final IBlockState stateBelow = getWorld().getBlockState(posBelow);

        if (stateBelow == Blocks.AIR.getDefaultState()) {
            return heat = 0;
        }
        
        if (stateBelow == Blocks.LAVA.getDefaultState()) {
            return heat = 7;
        }
        
        if (stateBelow == Blocks.MAGMA.getDefaultState()) {
            return heat = 3;
        }
        
        if (stateBelow == Blocks.TORCH.getDefaultState()) {
            return heat = 1;
        }
        

        return heat = 0;
}
	
	public boolean isBurning()
    {
        return this.heat > 0;
    }
	
	private boolean canRun() {
		if(itemstackhandler.getStackInSlot(0).isEmpty())
			return false;
		
		if(itemstackhandler.getStackInSlot(1).isEmpty())
			return false;
		
		if(itemstackhandler.getStackInSlot(2).isEmpty())
			return false;
		
		if(getHeat() == 0)
			return false;


		final ItemStack[] results = DistillerRecipes.getDistillingResult(itemstackhandler.getStackInSlot(0));
		if(results == null)
			return false;

		if(!itemstackhandler.getStackInSlot(3).isEmpty()) {
			if(!itemstackhandler.getStackInSlot(3).isItemEqual(results[0]))
				return false;
			if(itemstackhandler.getStackInSlot(3).getCount() + results[0].getCount() > itemstackhandler
					.getStackInSlot(3).getMaxStackSize())
				return false;
		}

		if(results[1] != null && !results[1].isEmpty() && !itemstackhandler.getStackInSlot(4).isEmpty()) {
			if(!itemstackhandler.getStackInSlot(4).isItemEqual(results[1]))
				return false;
			if(itemstackhandler.getStackInSlot(4).getCount() + results[1].getCount() > itemstackhandler
					.getStackInSlot(4).getMaxStackSize())
				return false;
		}

		return true;
	}
	
	private void distillBooze() {

		if(!canRun())
			return;

		final ItemStack[] results = DistillerRecipes.getDistillingResult(itemstackhandler.getStackInSlot(0));
		if(results == null)
			return;

		if(itemstackhandler.getStackInSlot(3).isEmpty()) {

			itemstackhandler.setStackInSlot(3, results[0].copy());
		}
		else if(itemstackhandler.getStackInSlot(3).getCount() + results[0].getCount() <= results[0].getMaxStackSize()) {
			itemstackhandler.getStackInSlot(3)
					.setCount(itemstackhandler.getStackInSlot(3).getCount() + results[0].getCount());
		}

		if(!results[1].isEmpty()) {
			if(itemstackhandler.getStackInSlot(4).isEmpty()) {
				itemstackhandler.setStackInSlot(4, results[1].copy());
			}
			else if(itemstackhandler.getStackInSlot(4).isItemEqual(results[1])) {
				itemstackhandler.getStackInSlot(4)
						.setCount(itemstackhandler.getStackInSlot(4).getCount() + results[1].getCount());
			}
		}

		itemstackhandler.getStackInSlot(0).shrink(1);
		if(itemstackhandler.getStackInSlot(0).getCount() <= 0) {
			itemstackhandler.getStackInSlot(0).isEmpty();
		}
		
		itemstackhandler.getStackInSlot(1).shrink(1);
		if(itemstackhandler.getStackInSlot(1).getCount() <= 0) {
			itemstackhandler.getStackInSlot(1).isEmpty();
		}
		
		
	}
	
	

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);

		return new SPacketUpdateTileEntity(getPos(), 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	  public boolean shouldRefresh(World world, BlockPos pos,@Nonnull IBlockState oldState,@Nonnull IBlockState newState) {
	    return oldState.getBlock() != newState.getBlock();
	  }

	public String getGuiID() {
		return "brewcraft:distiller";
	}
}