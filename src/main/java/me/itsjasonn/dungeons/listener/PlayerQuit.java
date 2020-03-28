package me.itsjasonn.dungeons.listener;

import me.itsjasonn.dungeons.dungeon.Dungeon;
import me.itsjasonn.dungeons.main.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
	Core core;
	
	public PlayerQuit(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Player player = event.getPlayer();
		if(Dungeon.getDungeonManager().isInGame(player)) {
			Dungeon.getDungeonManager().TeleportToHub(player);
			Dungeon.getDungeonManager().removePlayer(player, true);
			if(Dungeon.getDungeonManager().getPlayers().size() == 0) {
				Dungeon.getDungeonManager().StopMatch();
			}
		}
	}
}