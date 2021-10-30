package com.seibel.lod.wrappers.Chunk;

import java.util.Objects;

import com.seibel.lod.wrappers.Block.BlockPosWrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;


//This class wraps the minecraft ChunkPos class
public class ChunkPosWrapper
{
	private net.minecraft.world.level.ChunkPos chunkPos;
	
	public ChunkPosWrapper(ChunkPos newChunkPos)
	{
		this.chunkPos = newChunkPos;
	}
	
	public ChunkPosWrapper(BlockPos blockPos)
	{
		this.chunkPos = new ChunkPos(blockPos);
	}
	
	
	public ChunkPosWrapper(ChunkPosWrapper newChunkPos)
	{
		this.chunkPos = newChunkPos.chunkPos;
	}
	
	public ChunkPosWrapper(BlockPosWrapper blockPos)
	{
		this.chunkPos = new ChunkPos(blockPos.getBlockPos());
	}
	
	
	
	
	public ChunkPosWrapper(int chunkX, int chunkZ)
	{
		this.chunkPos = new ChunkPos(chunkX, chunkZ);
	}

	public int getX()
	{
		return chunkPos.x;
	}
	
	public int getZ()
	{
		return chunkPos.z;
	}
	
	public int getMinBlockX()
	{
		return chunkPos.getMinBlockX();
	}
	
	public int getMinBlockZ()
	{
		return chunkPos.getMinBlockZ();
	}
	
	public int getRegionX()
	{
		return chunkPos.getRegionX();
	}
	
	public int getRegionZ()
	{
		return chunkPos.getRegionZ();
	}
	
	public ChunkPos getChunkPos()
	{
		return chunkPos;
	}
	
	@Override public boolean equals(Object o)
	{
		return chunkPos.equals(o);
	}
	
	@Override public int hashCode()
	{
		return Objects.hash(chunkPos);
	}

	public BlockPosWrapper getWorldPosition()
	{
		return null;
	}
	
}