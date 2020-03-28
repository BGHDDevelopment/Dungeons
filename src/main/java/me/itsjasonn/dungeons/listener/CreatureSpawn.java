package me.itsjasonn.dungeons.listener;

import me.itsjasonn.dungeons.main.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {
	Core core;
	
	public CreatureSpawn(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getEntity().getCustomName() == null) {
			event.setCancelled(true);
		}
	}
}