package com.pam.brewcraft.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pam.brewcraft.blocks.CropRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ConfigHandler {

    private final Configuration config;

    private static final String CATEGORY_GENERAL = "general";
    

    /**
     * Defaults
     */
    private static final int defaultCropFoodRestore = 1;
    private static final double defaultSaturationSmall = 0.6D;
    private static final double defaultCropGrowthSpeed = 0.0D;

    /**
     * Config
     */

    public int cropfoodRestore;
    public float cropsaturationRestore;
    public static boolean cropsdropSeeds;
    
    public static float cropGrowthSpeed;

    public int seedrarity;



    public final HashMap<String, Boolean> seedDropFromGrass = new HashMap<String, Boolean>();


    public ConfigHandler(Configuration config) {
        this.config = config;

        initSettings();
    }

    private void initSettings() {
        config.load();
        initCropSettings();
        initSeedDropSettings();
        

        if (config.hasChanged()) {
            config.save();
        }
    }


    private void initCropSettings() {
        cropfoodRestore = config.get(CATEGORY_GENERAL, "cropfoodRestore", defaultCropFoodRestore).getInt();
        cropsaturationRestore = (float) config.get(CATEGORY_GENERAL, "cropsaturationRestore", defaultSaturationSmall).getDouble();
        cropsdropSeeds = config.get(CATEGORY_GENERAL, "cropsdropSeeds", false).getBoolean();
        cropGrowthSpeed = (float) config.get(CATEGORY_GENERAL, "cropGrowthSpeed", defaultCropGrowthSpeed, "Default: 0.0, This number is added/subtracted from normal fertile crop growth (3.0) and adjacent fertile crop growth (4.0).").getDouble();
    }


    

    private void initSeedDropSettings() {
        seedrarity = config.get(CATEGORY_GENERAL, "seedrarity", 1).getInt();

        
        
        initSeedDropFromGrassSetting("hopsseeddropfromgrass", CropRegistry.HOPS);
       

    
    }


    private void initSeedDropFromGrassSetting(String key, String item) {
        boolean doDrop = config.get(CATEGORY_GENERAL, key, true).getBoolean();

        seedDropFromGrass.put(item, doDrop);
    }


}
