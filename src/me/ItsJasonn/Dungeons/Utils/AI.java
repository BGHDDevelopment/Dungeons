package me.ItsJasonn.Dungeons.Utils;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;

import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class AI {
	private static AI instance = new AI();

	public static AI getAIManager() { 
		return instance;
	}

	public void removeAI(org.bukkit.entity.Entity entity) {
		net.minecraft.server.v1_11_R1.Entity nmsEntity = ((CraftEntity)entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.c(tag);
		tag.setInt("NoAI", 1);
		nmsEntity.f(tag);
	}

	public void setAI(org.bukkit.entity.Entity entity) { net.minecraft.server.v1_11_R1.Entity nmsEntity = ((CraftEntity)entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.c(tag);
		tag.setInt("NoAI", 0);
		nmsEntity.f(tag);
	}
}