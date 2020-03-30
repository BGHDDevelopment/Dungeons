package me.itsjasonn.dungeons.commands;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.itsjasonn.dungeons.main.Plugin;
import me.itsjasonn.dungeons.main.Tools;

public class Dungeons implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = cmd.getName();
		if(command.equalsIgnoreCase("Dungeons")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.isOp()) {
					if(args.length == 0) {
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons" + ChatColor.AQUA + " - Show this help page.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Create" + ChatColor.AQUA + " - Create a new dungeon in the server.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Delete" + ChatColor.AQUA + " - Delete an existing dungeon.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Setlobby" + ChatColor.AQUA + " - Set the lobby spawn location.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Setspawn [Stage]" + ChatColor.AQUA + " - Set a spawn location for a dungeon.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Setgoal [Stage] [Goal]" + ChatColor.AQUA + " - Set the goal for a certain stage in a dungeon.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Goals" + ChatColor.AQUA + " - Get a list of all available goals.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Forcestart <Stage>" + ChatColor.AQUA + " - Start a match of the player's current game.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Parkour SetY [Stage]" + ChatColor.AQUA + " - Set the minimum Y-position for a parkour stage.");
						player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons ShootTargets SetTarget [Stage] [Target]" + ChatColor.AQUA + " - Set a target in the target shooting stage.");
					} else if (args.length == 1) {
						if(args[0].equalsIgnoreCase("Create")) {
							if(!me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().DoesExist()) {
								me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().CreateDungeon();
								player.sendMessage(ChatColor.GREEN + "Dungeon has been created!");
							} else {
								player.sendMessage(ChatColor.RED + "Dungeon does not exist!");
							}
						} else if(args[0].equalsIgnoreCase("Delete")) {
							me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().RemoveDungeon();
							player.sendMessage(ChatColor.GREEN + "Dungeon has been removed!");
						} else if(args[0].equalsIgnoreCase("Setlobby")) {
							if(me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().DoesExist()) {
								me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().SetLobby(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
								player.sendMessage(ChatColor.GREEN + "Lobby location for Dungeon has been set!");
							} else {
								player.sendMessage(ChatColor.RED + "This Dungeon does not exist!");
							}
						} else if(args[0].equalsIgnoreCase("SetSpawn")) {
							player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " [Stage]");
						} else if(args[0].equalsIgnoreCase("Setgoal")) {
							player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " [Stage] [Goal]");
						} else if(args[0].equalsIgnoreCase("Forcestart")) {
							for (Player players : me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().getPlayers()) {
								me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().StartStage(players, 0);
							}
						} else if(args[0].equalsIgnoreCase("Goals")) {
							player.sendMessage(ChatColor.DARK_AQUA + "Available Goals: " + ChatColor.AQUA + me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().getGoalsString());
						} else if(args[0].equalsIgnoreCase("SetY")) {
							player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons Parkour SetY [Stage]" + ChatColor.AQUA + " - Set the minimum Y-position for a parkour level.");
						} else if(args[0].equalsIgnoreCase("ShootTargets")) {
							player.sendMessage(ChatColor.DARK_AQUA + "/Dungeons ShootTargets SetTarget [Stage] [Target]" + ChatColor.AQUA + " - Set a target in the target shooting stage.");
						} else {
							player.sendMessage(ChatColor.RED + "Second parameter '" + args[0] + "' couldn't be recognized!");
						}
					} else if(args.length == 2) {
						if(args[0].equalsIgnoreCase("Setspawn")) {
							if(Tools.isInt(args[1])) {
								int stage = Integer.parseInt(args[1]);
								if(me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().DoesExist()) {
									me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().SetSpawn(stage, player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
									player.sendMessage(ChatColor.GREEN + "Spawn location for Dungeon in Stage '" + stage + "' has been set!");
								} else {
									player.sendMessage(ChatColor.RED + "This Dungeon does not exist!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' is not a number!");
							}
						} else if (args[0].equalsIgnoreCase("Setgoal")) {
							player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " [Stage] [Goal]");
						} else if (args[0].equalsIgnoreCase("Forcestart")) {
							if(Tools.isInt(args[1])) {
								int stage = Integer.parseInt(args[1]);
								if(me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().DoesExist()) {
									for(Player players : me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().getPlayers()) {
										me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().StartStage(players, stage);
									}
								} else {
									player.sendMessage(ChatColor.RED + "This Dungeon does not exist!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' is not a number!");
							}
						} else if(args[0].equalsIgnoreCase("Parkour")) {
							if(args[1].equalsIgnoreCase("SetY")) {
								player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " SetY [Stage]");
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' couldn't be recognized!");
							}
						} else if(args[0].equalsIgnoreCase("ShootTargets")) {
							if(args[1].equalsIgnoreCase("SetTarget")) {
								player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " SetTarget [Stage] [Target]");
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' couldn't be recognized!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "Second parameter '" + args[0] + "' couldn't be recognized!");
						}
					} else if(args.length == 3) {
						if(args[0].equalsIgnoreCase("Parkour")) {
							if(args[1].equalsIgnoreCase("SetY")) {
								if(Tools.isInt(args[2])) {
									int stage = Integer.parseInt(args[2]);

									player.sendMessage(ChatColor.GREEN + "Y-location has been set for Dungeon in Stage '" + stage + "' has been set!");

									File file = new File(Plugin.core.getDataFolder(), "/data/dungeons.yml");
									YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
									config.set("stage-settings." + stage + ".parkour-min-y", player.getLocation().getY());
									try {
										config.save(file);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									player.sendMessage(ChatColor.RED + "Fourth parameter '" + args[2] + "' is not a number!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' couldn't be recognized!");
							}
						} else if(args[0].equalsIgnoreCase("Setgoal")) {
							if(Tools.isInt(args[1])) {
								int stage = Integer.parseInt(args[1]);
								if(me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().getGoals().contains(args[2])) {
									String goal = args[2];
									me.itsjasonn.dungeons.dungeon.Dungeon.getDungeonManager().SetGoal(stage, goal);
									player.sendMessage(ChatColor.GREEN + "The goal '" + goal + "' for Dungeon in Stage '" + stage + "' has been set!");
								} else {
									player.sendMessage(ChatColor.RED + "Fourth parameter '" + args[3] + "' is not a valid goal!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[2] + "' is not a number!");
							}
						} else if(args[0].equalsIgnoreCase("ShootTargets")) {
							if (args[1].equalsIgnoreCase("SetTarget")) {
								player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/Dungeons " + WordUtils.capitalizeFully(args[0]) + " SetTarget [Stage] [Target]");
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' couldn't be recognized!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "Second parameter '" + args[0] + "' couldn't be recognized!");
						}
					} else if(args.length == 4) {
						if(args[0].equalsIgnoreCase("ShootTargets")) {
							if(args[1].equalsIgnoreCase("SetTarget")) {
								if(Tools.isInt(args[2])) {
									int stage = Integer.parseInt(args[2]);
									if(Tools.isInt(args[3])) {
										int target = Integer.parseInt(args[3]);

										player.sendMessage(ChatColor.GREEN + "The target '" + target + "' has been set for Dungeon in Stage '" + stage + "' has been set!");

										File file = new File(Plugin.core.getDataFolder(), "/data/dungeons.yml");
										YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
										config.set("stage-settings." + stage + ".shoottargets-" + target, player.getLocation());
										try {
											config.save(file);
										} catch (IOException e) {
											e.printStackTrace();
										}
									} else {
										player.sendMessage(ChatColor.RED + "Fifth parameter '" + args[4] + "' is not a number!");
									}
								} else {
									player.sendMessage(ChatColor.RED + "Fourth parameter '" + args[3] + "' is not a number!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' couldn't be recognized!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "Second parameter '" + args[0] + "' couldn't be recognized!");
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "You are not permitted to use this command!");
				}
			} else if(sender instanceof ConsoleCommandSender) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "This is a player-only command!");
			}
		}
		return false;
	}
}