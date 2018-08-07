package me.ItsJasonn.Dungeons.Listener;

import me.ItsJasonn.Dungeons.Dungeon.Dungeon;
import me.ItsJasonn.Dungeons.Main.Core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	Core core;

	public PlayerJoin(Core core) {
		this.core = core;
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		Player player = event.getPlayer();
		Dungeon.getDungeonManager().addPlayer(player, true);
		Dungeon.getDungeonManager().TeleportToLobby(player);
		
		if(Dungeon.getDungeonManager().inLobby) {
			Dungeon.getDungeonManager().TeleportToLobby(player);
		} else {
			Dungeon.getDungeonManager().setSpectator(player);
		}
	}
}