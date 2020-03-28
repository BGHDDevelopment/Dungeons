package me.itsjasonn.dungeons.listener;

import me.itsjasonn.dungeons.dungeon.Dungeon;
import me.itsjasonn.dungeons.main.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {
	Core core;
	
	public PlayerTeleport(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (Dungeon.getDungeonManager().isInGame(player) && Dungeon.getDungeonManager().getStage() > 0) {
			int stage = Dungeon.currentStage;
			if(Dungeon.getDungeonManager().getGoal(stage).equalsIgnoreCase("Parkour") && event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
				event.setCancelled(true);
			}
		}
	}
}