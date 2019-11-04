package com.pam.brewcraft.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.pam.brewcraft.item.DistillerRecipes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class TileEntityDistiller extends TileEntity implements ITickable, IInventory {
	public short cookTime;

	@CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEMS_CAP;
	
    private int distillerBurnTime;
    private int currentItemBurnTime;
    private int totalCookTime;
    private String distillerCustomName;
    private NonNullList<ItemStack> distillerItemStacks = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);
    private final ItemStackHandler itemstackhandler = new ItemStackHandler(5);
    private final RangedWrapper top = new RangedWrapper(itemstackhandler, 0, 2);
    
    private final RangedWrapper bottom = new RangedWrapper(itemstackhandler, 3, 4)
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
		this.distillerBurnTime = compound.getInteger("BurnTime");
		this.currentItemBurnTime = getItemBurnTime(this.itemstackhandler.getStackInSlot(2));
		super.readFromNBT(compound);
	}

	
	public int getCookTime(ItemStack stack)
    {
        return 200;
    }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setShort("CookTime", cookTime);
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
	
	private boolean canRun() {
		if(itemstackhandler.getStackInSlot(0).isEmpty())
			return false;
		
		if(itemstackhandler.getStackInSlot(1).isEmpty())
			return false;
		
		if(itemstackhandler.getStackInSlot(2).isEmpty())
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

	@Override
	public String getName() {
		return this.hasCustomName() ? this.distillerCustomName : "container.distiller";
	}

	@Override
	public boolean hasCustomName() {
		return this.distillerCustomName != null && !this.distillerCustomName.isEmpty();
	}

	@Override
	public int getSizeInventory()
    {
        return this.distillerItemStacks.size();
    }

	@Override
	public boolean isEmpty() 
	{
        for (ItemStack itemstack : this.distillerItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

	@Override
	public ItemStack getStackInSlot(int index) 
	{
        return this.distillerItemStacks.get(index);
    }

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
        return ItemStackHelper.getAndSplit(this.distillerItemStacks, index, count);
    }

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
        return ItemStackHelper.getAndRemove(this.distillerItemStacks, index);
    }

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
        ItemStack itemstack = this.distillerItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.distillerItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }
	
	public boolean isBurning()
    {
        return this.distillerBurnTime > 0;
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}
	
	public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOOL))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.CARPET))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
    }

	public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
            ItemStack itemstack = this.distillerItemStacks.get(2);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        
	}

	@Override
	public int getField(int id) {
		switch (id)
        {
            case 0:
                return this.distillerBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
	}

	@Override
	public void setField(int id, int value) {
		switch (id)
        {
            case 0:
                this.distillerBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = (short) value;
                break;
            case 3:
                this.totalCookTime = value;
        }
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public void clear() {
		this.distillerItemStacks.clear();
		
	}
}