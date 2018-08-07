package me.ItsJasonn.Dungeons.Listener;

import me.ItsJasonn.Dungeons.Dungeon.Dungeon;
import me.ItsJasonn.Dungeons.Main.Core;
import me.ItsJasonn.Dungeons.Main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHit implements Listener {
	Core core;
	
	public ProjectileHit(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			final Arrow arrow = (Arrow)event.getEntity();
			if(arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
				Player player = (Player)arrow.getShooter();
				if(Dungeon.getDungeonManager().isInGame(player)) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.core, new Runnable() {
						public void run() {
							arrow.remove();
						}
					}, 1);
				}
			}
		}
	}
}