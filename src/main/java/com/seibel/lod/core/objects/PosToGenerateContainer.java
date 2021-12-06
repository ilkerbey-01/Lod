/*
 *    This file is part of the Distant Horizon mod (formerly the LOD Mod),
 *    licensed under the GNU GPL v3 License.
 *
 *    Copyright (C) 2020  James Seibel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, version 3.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.seibel.lod.core.objects;

import com.seibel.lod.core.util.LevelPosUtil;

/**
 * Holds the levelPos that need to be generated.
 * 
 * @author Leonardo Amato
 * @version 9-27-2021
 */
public class PosToGenerateContainer
{
	private final int playerPosX;
	private final int playerPosZ;
	private final byte farMinDetail;
	private int nearSize;
	private int farSize;
	
	// TODO what is the format of these two arrays? [detailLevel][4-children]?
	private final int[][] nearPosToGenerate;
	private final int[][] farPosToGenerate;
	
	
	
	
	public PosToGenerateContainer(byte farMinDetail, int maxDataToGenerate, int playerPosX, int playerPosZ)
	{
		this.playerPosX = playerPosX;
		this.playerPosZ = playerPosZ;
		this.farMinDetail = farMinDetail;
		nearSize = 0;
		farSize = 0;
		nearPosToGenerate = new int[maxDataToGenerate][4];
		farPosToGenerate = new int[maxDataToGenerate][4];
	}
	
	
	
	// TODO what is going on in this method?
	public void addPosToGenerate(byte detailLevel, int posX, int posZ)
	{
		int distance = LevelPosUtil.minDistance(detailLevel, posX, posZ, playerPosX, playerPosZ);
		int index;
		
		if (detailLevel >= farMinDetail)
		{
			// We are introducing a position in the far array
			
			if (farSize < farPosToGenerate.length)
				farSize++;
			
			index = farSize - 1;
			while (index > 0 && LevelPosUtil.compareDistance(distance, farPosToGenerate[index - 1][3]) <= 0)
			{
				farPosToGenerate[index][0] = farPosToGenerate[index - 1][0];
				farPosToGenerate[index][1] = farPosToGenerate[index - 1][1];
				farPosToGenerate[index][2] = farPosToGenerate[index - 1][2];
				farPosToGenerate[index][3] = farPosToGenerate[index - 1][3];
				index--;
			}
			
			
			if (index != farSize - 1 || farSize != farPosToGenerate.length)
			{
				farPosToGenerate[index][0] = detailLevel + 1;
				farPosToGenerate[index][1] = posX;
				farPosToGenerate[index][2] = posZ;
				farPosToGenerate[index][3] = distance;
			}
		}
		else
		{
			//We are introducing a position in the near array
			
			if (nearSize < nearPosToGenerate.length)
				nearSize++;
			
			index = nearSize - 1;
			while (index > 0 && LevelPosUtil.compareDistance(distance, nearPosToGenerate[index - 1][3]) <= 0)
			{
				nearPosToGenerate[index][0] = nearPosToGenerate[index - 1][0];
				nearPosToGenerate[index][1] = nearPosToGenerate[index - 1][1];
				nearPosToGenerate[index][2] = nearPosToGenerate[index - 1][2];
				nearPosToGenerate[index][3] = nearPosToGenerate[index - 1][3];
				index--;
			}
			
			
			if (index != nearSize - 1 || nearSize != nearPosToGenerate.length)
			{
				nearPosToGenerate[index][0] = detailLevel + 1;
				nearPosToGenerate[index][1] = posX;
				nearPosToGenerate[index][2] = posZ;
				nearPosToGenerate[index][3] = distance;
			}
		}
	}
	
	
	
	public int getNumberOfPos()
	{
		return nearSize + farSize;
	}
	
	public int getNumberOfNearPos()
	{
		return nearSize;
	}
	
	public int getNumberOfFarPos()
	{
		return farSize;
	}
	
	// TODO what does getNth mean? could the name be more descriptive or is it just a index?
	public int getNthDetail(int n, boolean near)
	{
		if (near)
			return nearPosToGenerate[n][0];
		else
			return farPosToGenerate[n][0];
	}
	
	public int getNthPosX(int n, boolean near)
	{
		if (near)
			return nearPosToGenerate[n][1];
		else
			return farPosToGenerate[n][1];
	}
	
	public int getNthPosZ(int n, boolean near)
	{
		if (near)
			return nearPosToGenerate[n][2];
		else
			return farPosToGenerate[n][2];
	}
	
	public int getNthGeneration(int n, boolean near)
	{
		if (near)
			return nearPosToGenerate[n][3];
		else
			return farPosToGenerate[n][3];
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('\n');
		builder.append('\n');
		builder.append('\n');
		builder.append("near pos to generate");
		builder.append('\n');
		for (int[] ints : nearPosToGenerate)
		{
			if (ints[0] == 0)
				break;
			builder.append(ints[0] - 1);
			builder.append(" ");
			builder.append(ints[1]);
			builder.append(" ");
			builder.append(ints[2]);
			builder.append(" ");
			builder.append(ints[3]);
			builder.append('\n');
		}
		builder.append('\n');
		
		builder.append("far pos to generate");
		builder.append('\n');
		for (int[] ints : farPosToGenerate)
		{
			if (ints[0] == 0)
				break;
			builder.append(ints[0] - 1);
			builder.append(" ");
			builder.append(ints[1]);
			builder.append(" ");
			builder.append(ints[2]);
			builder.append(" ");
			builder.append(ints[3]);
			builder.append('\n');
		}
		return builder.toString();
	}
}