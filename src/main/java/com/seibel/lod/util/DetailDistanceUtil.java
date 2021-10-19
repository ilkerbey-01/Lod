package com.seibel.lod.util;

import com.seibel.lod.config.LodConfig;
import com.seibel.lod.enums.DistanceGenerationMode;
import com.seibel.lod.enums.HorizontalQuality;
import com.seibel.lod.enums.HorizontalResolution;
import com.seibel.lod.wrappers.MinecraftWrapper;

public class DetailDistanceUtil
{
	private static final double genMultiplier = 1.0;
	private static final double treeGenMultiplier = 1.0;
	private static final double treeCutMultiplier = 1.0;
	private static int minGenDetail = LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel;
	private static int minDrawDetail = Math.max(LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel, LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel);
	private static final int maxDetail = LodUtil.REGION_DETAIL_LEVEL + 1;
	private static final int minDistance = 0;
	private static int minDetailDistance = (int) (MinecraftWrapper.INSTANCE.getRenderDistance()*16 * 1.42f);
	private static int maxDistance = LodConfig.CLIENT.graphics.qualityOption.lodChunkRenderDistance.get() * 16 * 2;
	
	
	private static final HorizontalResolution[] lodGenDetails = {
			HorizontalResolution.BLOCK,
			HorizontalResolution.TWO_BLOCKS,
			HorizontalResolution.FOUR_BLOCKS,
			HorizontalResolution.HALF_CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK,
			HorizontalResolution.CHUNK };
	
	
	
	public static void updateSettings()
	{
		minDetailDistance = (int) (MinecraftWrapper.INSTANCE.getRenderDistance()*16 * 1.42f);
		minGenDetail = LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel;
		minDrawDetail = Math.max(LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel, LodConfig.CLIENT.graphics.qualityOption.drawResolution.get().detailLevel);
		maxDistance = LodConfig.CLIENT.graphics.qualityOption.lodChunkRenderDistance.get() * 16 * 8;
	}
	
	public static int baseDistanceFunction(int detail)
	{
		if (detail <= minGenDetail)
			return minDistance;
		if (detail >= maxDetail)
			return maxDistance;
		
		int distanceUnit = LodConfig.CLIENT.graphics.qualityOption.horizontalScale.get().distanceUnit;
		if (LodConfig.CLIENT.graphics.qualityOption.horizontalQuality.get() == HorizontalQuality.LOWEST)
			return (detail * distanceUnit);
		else
		{
			double base = LodConfig.CLIENT.graphics.qualityOption.horizontalQuality.get().quadraticBase;
			return (int) (Math.pow(base, detail) * distanceUnit);
		}
	}
	
	public static int getDrawDistanceFromDetail(int detail)
	{
		return baseDistanceFunction(detail);
	}
	
	public static byte baseInverseFunction(int distance, int minDetail, boolean useRenderMinDistance)
	{
		int detail;
		if (distance == 0)
			return (byte) minDetail;
		if (distance < minDetailDistance && useRenderMinDistance)
			return (byte) minDetail;
		int distanceUnit = LodConfig.CLIENT.graphics.qualityOption.horizontalScale.get().distanceUnit;
		if (LodConfig.CLIENT.graphics.qualityOption.horizontalQuality.get() == HorizontalQuality.LOWEST)
			detail = (byte) Math.floorDiv(distance, distanceUnit);
		else
		{
			double base = LodConfig.CLIENT.graphics.qualityOption.horizontalQuality.get().quadraticBase;
			double logBase = Math.log(base);
			detail = (byte) (Math.log(Math.floorDiv(distance, distanceUnit)) / logBase);
		}
		return (byte) LodUtil.clamp(minDetail, detail, maxDetail - 1);
	}
	
	public static byte getDrawDetailFromDistance(int distance)
	{
		return baseInverseFunction(distance, minDrawDetail, false);
	}
	
	public static byte getGenerationDetailFromDistance(int distance)
	{
		return baseInverseFunction((int) (distance * genMultiplier), minGenDetail, true);
	}
	
	public static byte getTreeCutDetailFromDistance(int distance)
	{
		
		return baseInverseFunction((int) (distance * treeCutMultiplier), minGenDetail, true);
	}
	
	
	public static byte getTreeGenDetailFromDistance(int distance)
	{
		
		return baseInverseFunction((int) (distance * treeGenMultiplier), minGenDetail, true);
	}
	
	public static DistanceGenerationMode getDistanceGenerationMode(int detail)
	{
		return LodConfig.CLIENT.worldGenerator.distanceGenerationMode.get();
	}
	
	public static byte getLodDrawDetail(int detail)
	{
		if (detail < minDrawDetail)
		{
			if (LodConfig.CLIENT.graphics.advancedOption.alwaysDrawAtMaxQuality.get())
				return getLodGenDetail(minDrawDetail).detailLevel;
			else
				return (byte) minDrawDetail;
		}
		else
		{
			if (LodConfig.CLIENT.graphics.advancedOption.alwaysDrawAtMaxQuality.get())
				return getLodGenDetail(detail).detailLevel;
			else
				return (byte) detail;
		}
	}
	
	public static HorizontalResolution getLodGenDetail(int detail)
	{
		if (detail < minGenDetail)
		{
			return lodGenDetails[minGenDetail];
		}
		else
		{
			return lodGenDetails[detail];
		}
	}
	
	
	public static byte getCutLodDetail(int detail)
	{
		if (detail < minGenDetail)
		{
			return lodGenDetails[minGenDetail].detailLevel;
		}
		else if (detail == maxDetail)
		{
			return LodUtil.REGION_DETAIL_LEVEL;
		}
		else
		{
			return lodGenDetails[detail].detailLevel;
		}
	}
	
	public static int getMaxVerticalData(int detail)
	{
		return LodConfig.CLIENT.graphics.qualityOption.verticalQuality.get().maxVerticalData[LodUtil.clamp(minGenDetail, detail, LodUtil.REGION_DETAIL_LEVEL)];
	}
	
}
