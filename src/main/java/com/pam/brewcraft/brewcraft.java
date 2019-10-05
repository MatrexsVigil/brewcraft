package com.pam.brewcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.pam.brewcraft.config.ConfigHandler;
import com.pam.brewcraft.item.ItemRegistry;
import com.pam.brewcraft.proxy.CommonProxy;

@Mod(modid = Reference.MODID, name = "Pam's BrewCraft", version = Reference.VERSION)

public class brewcraft
{
	@Instance(Reference.MODID)
    public static brewcraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    
    public static CreativeTabs tabBrewcraft = new CreativeTabs("tabBrewcraft") 
	{

    	@Override
    	@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemRegistry.rennalgolditem);
		}
	};
	
	public static ConfigHandler config;
	public static Logger log;
    
	  @EventHandler
	  public void preInit(FMLPreInitializationEvent event) 
	  {
		  log = event.getModLog();
			config = new ConfigHandler(new Configuration(event.getSuggestedConfigurationFile()));
		
		proxy.preInit(event);	
			
	  }
	  
	  @EventHandler
	  public void init(FMLInitializationEvent event) 
	  {
		  proxy.init(event);
		  
        
	}
	  
	  @EventHandler
	    public void postInit(FMLPostInitializationEvent event) {
	        proxy.postInit(event);
	    }
}