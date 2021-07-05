package com.seibel.lod.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.seibel.lod.ModInfo;
import com.seibel.lod.enums.DistanceGenerationMode;
import com.seibel.lod.enums.FogDistance;
import com.seibel.lod.enums.FogDrawOverride;
import com.seibel.lod.enums.LodDetail;
import com.seibel.lod.enums.LodTemplate;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * 
 * @author James Seibel
 * @version 7-5-2021
 */
@Mod.EventBusSubscriber
public class LodConfig
{
	public static class Client
	{
		public ForgeConfigSpec.BooleanValue drawLODs;
		
		public ForgeConfigSpec.EnumValue<FogDistance> fogDistance;
		
		public ForgeConfigSpec.EnumValue<FogDrawOverride> fogDrawOverride;
		
		public ForgeConfigSpec.BooleanValue debugMode;
		
		public ForgeConfigSpec.EnumValue<LodTemplate> lodTemplate;
		
		public ForgeConfigSpec.EnumValue<LodDetail> lodDetail;
		
		public ForgeConfigSpec.EnumValue<DistanceGenerationMode> distanceGenerationMode;
		
		public ForgeConfigSpec.BooleanValue allowUnstableFeatureGeneration;
		
		public ForgeConfigSpec.IntValue numberOfWorldGenerationThreads;
		
		/** this is multiplied by the default view distance
		 * to determine how far out to generate/render LODs */
		public ForgeConfigSpec.IntValue lodChunkRadiusMultiplier;
		
		
		
		Client(ForgeConfigSpec.Builder builder)
		{
	        builder.comment(ModInfo.MODNAME + " configuration settings").push("client");
	        
	        drawLODs = builder
	        		.comment("\n\n"
	        				+ " If false LODs will not be drawn, \n"
	        				+ " however they will still be generated \n"
	        				+ " and saved to file for later use. \n")
	        		.define("drawLODs", true);
	        
	        fogDistance = builder
	                .comment("\n\n"
	                		+ " At what distance should Fog be drawn on the LODs? \n"
	                		+ " If the fog cuts off ubruptly or you are using Optifine's \"fast\" \n"
	                		+ " fog option set this to " + FogDistance.NEAR.toString() + " or " + FogDistance.FAR.toString() + ". \n")
	                .defineEnum("fogDistance", FogDistance.NEAR_AND_FAR);
	        
	        fogDrawOverride = builder
	                .comment("\n\n"
	                		+ " When should fog be drawn? \n"
	                		+ " " + FogDrawOverride.USE_OPTIFINE_FOG_SETTING.toString() + ": Use whatever Fog setting Optifine is using. If Optifine isn't installed this defaults to " + FogDrawOverride.ALWAYS_DRAW_FOG_FANCY.toString() + ". \n"
	                		+ " " + FogDrawOverride.NEVER_DRAW_FOG.toString() + ": Never draw fog on the LODs \n"
            				+ " " + FogDrawOverride.ALWAYS_DRAW_FOG_FAST.toString() + ": Always draw fast fog on the LODs \n"
	                		+ " " + FogDrawOverride.ALWAYS_DRAW_FOG_FANCY.toString() + ": Always draw fancy fog on the LODs (if your graphics card supports it) \n")
	                .defineEnum("fogDrawOverride", FogDrawOverride.USE_OPTIFINE_FOG_SETTING);
	        
	        debugMode = builder
	                .comment("\n\n"
	                		+ " If false the LODs will draw with their normal world colors. \n"
	                		+ " If true they will draw as a black and white checkerboard. \n"
	                		+ " This can be used for debugging or imagining you are playing a \n"
	                		+ " giant game of chess ;) \n")
	                .define("drawCheckerBoard", false);
	        
	        lodTemplate = builder
	                .comment("\n\n"
	                		+ " How should the LODs be drawn? \n"
	                		+ " " + LodTemplate.CUBIC.toString() + ": LOD Chunks are drawn as rectangular prisms (boxes). \n"
	                		+ " " + LodTemplate.TRIANGULAR.toString() + ": LOD Chunks smoothly transition between other. \n"
	                		+ " " + LodTemplate.DYNAMIC.toString() + ": LOD Chunks smoothly transition between other, \n"
	                		+ " " + "         unless a neighboring chunk is at a significantly different height. \n")
	                .defineEnum("lodTemplate", LodTemplate.CUBIC);
	        
	        lodDetail = builder
	                .comment("\n\n"
	                		+ " How detailed should the LODs be? \n"
	                		+ " " + LodDetail.SINGLE.toString() + ": render 1 LOD for each Chunk. \n"
            				+ " " + LodDetail.DOUBLE.toString() + ": render 4 LODs for each Chunk. \n"
            				+ " " + LodDetail.QUAD.toString() +   ": render 16 LODs for each Chunk. \n"
            				+ " " + LodDetail.HALF.toString() +   ": render 64 LODs for each Chunk. \n")
	                .defineEnum("lodGeometryQuality", LodDetail.DOUBLE);
	        
	        lodChunkRadiusMultiplier = builder
	                .comment("\n\n"
	                		+ " This is multiplied by the default view distance \n"
	                		+ " to determine how far out to generate/render LODs. \n"
	                		+ " A value of 2 means that there is 1 render distance worth \n"
	                		+ " of LODs in each cardinal direction. \n")
	                .defineInRange("lodChunkRadiusMultiplier", 6, 2, 32);
	        
	        distanceGenerationMode = builder
	                .comment("\n\n"
	                		+ " Note: The times listed here are the amount of time it took \n"
	                		+ "       the developer's PC to generate 1 chunk, \n"
	                		+ "       and are included so you can compare the \n"
	                		+ "       different generation options. Your mileage may vary. \n"
	                		+ "\n"
	                		
	                		+ " " + DistanceGenerationMode.BIOME_ONLY.toString() + " \n"
	                		+ " Only generate the biomes and use biome \n"
	                		+ " grass/foliage color, water color, or ice color \n"
	                		+ " to generate the color. \n"
	                		+ " Doesn't generate height, everything is shown at sea level. \n"
	                		+ " Multithreaded - Fastest (2-5 ms) \n"
	                		
	                		+ "\n"
							+ " " + DistanceGenerationMode.BIOME_ONLY_SIMULATE_HEIGHT.toString() + " \n"
							+ " Same as BIOME_ONLY, except instead \n"
							+ " of always using sea level as the LOD height \n"
							+ " different biome types (mountain, ocean, forest, etc.) \n"
							+ " use predetermined heights to simulate having height data. \n"
							+ " Multithreaded - Fastest (2-5 ms) \n"
							
							+ "\n"
							+ " " + DistanceGenerationMode.SURFACE.toString() + " \n"
							+ " Generate the world surface, \n"
							+ " this does NOT include caves, trees, \n"
							+ " or structures. \n"
							+ " Multithreaded - Faster (10-20 ms) \n"
							
							+ "\n"
							+ " " + DistanceGenerationMode.FEATURES.toString() + " \n"
							+ " Generate everything except structures. \n"
							+ " WARNING: This may cause world generation bugs or instability, \n"
							+ "	since some features cause concurrentModification exceptions. \n"
							+ " Multithreaded - Fast (15-20 ms) \n"
							
							+ "\n"
							+ " " + DistanceGenerationMode.SERVER.toString() + " \n"
							+ " Ask the server to generate/load each chunk. \n"
							+ " This is the most compatible, but causes server/simulation lag. \n"
							+ " This will also show player made structures if you \n"
							+ " are adding the mod to a pre-existing world. \n"
							+ " Singlethreaded - Slow (15-50 ms, with spikes up to 200 ms) \n")
	                .defineEnum("distanceBiomeOnlyGeneration", DistanceGenerationMode.SURFACE);
	        
	        allowUnstableFeatureGeneration = builder
	                .comment("\n\n"
	                		+ " When using the " + DistanceGenerationMode.FEATURES.toString() + "generation mode \n"
	                		+ " some features may not be thread safe, which could \n"
	                		+ " cause instability and crashes. \n"
	                		+ " By default (false) those features are skipped, \n"
	                		+ " improving stability, but decreasing how many features are \n"
	                		+ " actually generated. \n"
	                		+ " (for example: tree generation is a unstable feature, \n"
	                		+ "               so trees may not be generated.) \n"
	                		+ " By setting this to true, all features will be generated, \n"
	                		+ " but your game will be more unstable and crashes may occur. \n"
	                		+ " \n"
	                		+ " I would love to remove this option and always generate everything, \n"
	                		+ " but I'm not sure how to do that. \n"
	                		+ " If you are a Java wizard, check out the git issue here: \n"
	                		+ " https://gitlab.com/jeseibel/minecraft-lod-mod/-/issues/35 \n")
	                .define("allowUnstableFeatureGeneration", false);
	        
	        numberOfWorldGenerationThreads = builder
	                .comment("\n\n"
	                		+ " This is how many threads are used when generating terrain. \n"
	                		+ " If you experience stuttering when generating terrain, decrease \n"
	                		+ " this number. If you want to increase LOD generation speed, \n"
	                		+ " increase the number. \n"
	                		+ " The max is the number of processors on your CPU. \n"
	                		+ "\n"
	                		+ " Requires a restart to take effect. \n"
	                		)
	                .defineInRange("numberOfWorldGenerationThreads", Runtime.getRuntime().availableProcessors(), 1, Runtime.getRuntime().availableProcessors());
	        
	        
	        builder.pop();
	    }
	}
	
	

    /**
     * {@link Path} to the configuration file of this mod
     */
    private static final Path CONFIG_PATH =
            Paths.get("config", ModInfo.MODID + ".toml");

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
        
        // setup the config file
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH)
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
        clientSpec.setConfig(config);
    }
    
    
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent)
    {
        LogManager.getLogger().debug(ModInfo.MODNAME, "Loaded forge config file {}", configEvent.getConfig().getFileName());
    }
    
    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent)
    {
        LogManager.getLogger().debug(ModInfo.MODNAME, "Forge config just got changed on the file system!");
    }
    
    
    
    
}
