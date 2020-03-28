package me.itsjasonn.dungeons.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.itsjasonn.dungeons.commands.Hub;
import me.itsjasonn.dungeons.listener.EntityStats;
import me.itsjasonn.dungeons.listener.PlayerJoin;
import me.itsjasonn.dungeons.listener.ProjectileHit;

public class Core extends JavaPlugin implements PluginMessageListener {
	public static HashMap<Player, String> chatState = new HashMap<Player, String>();
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "-----------=======  Dungeons  =======-----------");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- All the files in this plugin are 'read-only'. This means that you are not allowed to open, remove, change, replace the file in any case!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- Before using any Dungeon, first place the .schematic file in the /plugins/Dungeons/Schematics/ folder with the Dungeon index as file name.");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- If an error occurs you are supposed to contact the developer and DO NOT try out things yourself.");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- Report bugs to the developer if there are any.");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "-----------======================-----------");
		MetricsLite metrics = new MetricsLite(this);

		getCommand("Dungeons").setExecutor(new me.itsjasonn.dungeons.commands.Dungeons());
		getCommand("Hub").setExecutor(new Hub());

		new Plugin(this);
		Bukkit.getServer().getPluginManager().registerEvents(new EntityStats(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.itsjasonn.dungeons.listener.PlayerMove(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.itsjasonn.dungeons.listener.PlayerQuit(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new me.itsjasonn.dungeons.listener.PlayerTeleport(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ProjectileHit(this), this);
		
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(ChatColor.RED + "You have been disconnected from the server to avoid any problems during the reload process. After this process you will be able to join again.");
			me.itsjasonn.dungeons.utils.Server server = new me.itsjasonn.dungeons.utils.Server("lobby");
			server.sendPlayerToServer(players);
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (World worlds : Bukkit.getServer().getWorlds()) {
					worlds.setStorm(false);
					worlds.setThundering(false);
				}
			}
		}, 0L, 8L);
	}
	
	public void onDisable() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			if(me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().isInGame(players)) {
				me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().removePlayer(players, false);
			}
		}
	}
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equals("BungeeCord")) {
			return;
		}
	}
}