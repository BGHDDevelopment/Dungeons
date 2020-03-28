package me.itsjasonn.dungeons.listener;

import java.io.File;
import java.util.ArrayList;
import me.itsjasonn.dungeons.dungeon.Dungeon;
import me.itsjasonn.dungeons.main.Core;
import me.itsjasonn.dungeons.main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
	Core core;

	public PlayerMove(Core core) {
		this.core = core;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		File file = new File(Plugin.core.getDataFolder(), "/data/dungeons.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (Dungeon.getDungeonManager().isInGame(player) && Dungeon.getDungeonManager().getStage() > 0) {
			Dungeon.getDungeonManager();
			int stage = Dungeon.currentStage;
			if (Dungeon.getDungeonManager().getGoal(stage).equalsIgnoreCase("Parkour")) {
				int minY = config.getInt("stage-settings." + stage + ".parkour-min-y");
				if (player.getLocation().getY() <= minY) {
					Dungeon.getDungeonManager().TeleportToStage(player, stage);
				}
				if (player.getGameMode() != GameMode.SPECTATOR && player.getLocation().getBlock().getType() == Material.PORTAL) {
					if (Dungeon.getDungeonManager().getPlayers().size() == 1) {
						Dungeon.getDungeonManager().StartStage(player, stage + 1);
						return;
					}
					player.setGameMode(GameMode.SPECTATOR);
					
					int spectators = Dungeon.spectators;
					if (spectators + 2 < Dungeon.getDungeonManager().getPlayers().size()) {
						Dungeon.getDungeonManager().setSpectator(player);
					} else {
						ArrayList<Player> playerList = new ArrayList<Player>();
						for (Player players : Dungeon.getDungeonManager().getPlayers()) {
							playerList.add(players);
						}
						for (Player players : playerList) {
							if (players.getGameMode() != GameMode.SPECTATOR) {
								Dungeon.getDungeonManager().TeleportToHub(players);
								Dungeon.getDungeonManager().removePlayer(players, false);
								players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
							} else {
								Dungeon.getDungeonManager().StartStage(players, stage + 1);
							}
						}
					}
				}
			}
		}
	}
}