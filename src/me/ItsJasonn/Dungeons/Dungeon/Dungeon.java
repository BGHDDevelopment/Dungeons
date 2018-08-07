package me.ItsJasonn.Dungeons.Dungeon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import me.ItsJasonn.Dungeons.Listener.EntityStats;
import me.ItsJasonn.Dungeons.Main.Plugin;
import me.ItsJasonn.Dungeons.Main.Tools;
import me.ItsJasonn.Dungeons.Utils.AI;
import me.ItsJasonn.Dungeons.Utils.DateCalculator;
import me.ItsJasonn.Dungeons.Utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Dungeon {
	private File file = new File(Plugin.core.getDataFolder(), "/data/dungeons.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);
	public static ArrayList<Player> playerList = new ArrayList<Player>();
	public static boolean inLobby = true;
	public static int currentStage = 0;
	public static int spectators = 0;
	public static int matchCounter = 0;
	public static int matchID = 0;
	public HashMap<Player, Integer> shootTargetsCount = new HashMap<Player, Integer>();
	public int targetCount = 0;
	private static Dungeon instance = new Dungeon();

	public static Dungeon getDungeonManager() {
		return instance;
	}

	public void CreateDungeon() {
		getConfig().createSection("lobby");
		getConfig().createSection("spawns");
		getConfig().createSection("goals");
		saveFile();
	}

	public void RemoveDungeon() {
		this.file.delete();
	}

	public void SetLobby(World world, double x, double y, double z, float yaw, float pitch) {
		getConfig().set("lobby.world", world.getName());
		getConfig().set("lobby.x", Double.valueOf(x));
		getConfig().set("lobby.y", Double.valueOf(y));
		getConfig().set("lobby.z", Double.valueOf(z));
		getConfig().set("lobby.yaw", Float.valueOf(yaw));
		getConfig().set("lobby.pitch", Float.valueOf(pitch));
		saveFile();
	}

	public void SetSpawn(int stage, World world, double x, double y, double z, float yaw, float pitch) {
		getConfig().set("spawns." + stage + ".world", world.getName());
		getConfig().set("spawns." + stage + ".x", Double.valueOf(x));
		getConfig().set("spawns." + stage + ".y", Double.valueOf(y));
		getConfig().set("spawns." + stage + ".z", Double.valueOf(z));
		getConfig().set("spawns." + stage + ".yaw", Float.valueOf(yaw));
		getConfig().set("spawns." + stage + ".pitch", Float.valueOf(pitch));
		saveFile();
	}

	public void SetGoal(int stage, String goal) {
		getConfig().set("goals." + stage, goal);
		saveFile();
	}

	public void TeleportToHub(Player player) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF("lobby");
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.sendPluginMessage(Plugin.core, "BungeeCord", b.toByteArray());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void resetScoreboard(Player player) {
		Scoreboard sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("timer", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		if(getStage() != 0) {
			obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " ---=[" + getGoal(getStage()) + "]=--- ");
		} else {
			obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " ---=[Lobby]=--- ");
		}
		obj.getScore(ChatColor.GREEN + "         Time left: " + ChatColor.YELLOW + DateCalculator.getMS(matchCounter, false) + "       ").setScore(getPlayers().size());
		if(getStage() > 0 && getGoal(getStage()).equalsIgnoreCase("Shoot_Targets")) {
			TreeMap<Player, Integer> scoreList = Tools.sortByValue(shootTargetsCount);
			int spot = 0;
			for(int i = getPlayers().size() - 1; i >= 0; i--) {
				obj.getScore(ChatColor.GREEN + "         " + ((Player) new ArrayList(scoreList.keySet()).get(i)).getName() + ": " + ChatColor.YELLOW + new ArrayList(scoreList.values()).get(i)).setScore(spot);
				spot++;
			}
		}
		for(Player players : getPlayers()) {
			players.setScoreboard(sb);
		}
	}

	public boolean DoesExist() {
		return this.file.exists();
	}

	public boolean isInGame(Player player) {
		if(playerList.contains(player)) {
			return true;
		}
		return false;
	}

	public void saveFile() {
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getGoals() {
		ArrayList<String> goalList = new ArrayList<String>();
		goalList.add("Parkour");
		goalList.add("Shoot_Targets");
		goalList.add("Boss_Fight");
		goalList.add("PVP");

		return goalList;
	}

	public String getGoalsString() {
		ArrayList<String> goalList = new ArrayList<String>();
		goalList.add("Parkour");
		goalList.add("Shoot_Targets");
		goalList.add("Boss_Fight");
		goalList.add("PVP");

		return goalList.toString().replace("[", "").replace("]", "");
	}

	public void TeleportToLobby(Player player) {
		Location location = new Location(Bukkit.getServer().getWorld(this.config.getString("lobby.world")), this.config.getDouble("lobby.x"), this.config.getDouble("lobby.y"), this.config.getDouble("lobby.z"), (float) this.config.getDouble("lobby.yaw"), (float) this.config.getDouble("lobby.pitch"));
		player.teleport(location);
	}

	public void TeleportToStage(Player player, int stage) {
		Location location = new Location(Bukkit.getServer().getWorld(this.config.getString("spawns." + stage + ".world")), this.config.getDouble("spawns." + stage + ".x"), this.config.getDouble("spawns." + stage + ".y"), this.config.getDouble("spawns." + stage + ".z"), (float) this.config.getDouble("spawns." + stage + ".yaw"), (float) this.config.getDouble("spawns." + stage + ".pitch"));
		player.teleport(location);
	}

	@SuppressWarnings("deprecation")
	public void StartStage(final Player player, int stage) {
		StartCountdown(stage);
		
		inLobby = false;
		
		for(Player players : getPlayers()) {
			if(players != player) {
				players.showPlayer(players);
			}
		}
		currentStage = stage;
		getDungeonManager().resetScoreboard(player);
		String goal = this.config.getString("goals." + stage);

		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.getInventory().clear();

		if(getStage() > 0) {
			TeleportToStage(player, stage);
			Title.sendFullTitle(player, 10, 50, 10, ChatColor.RED + "" + ChatColor.BOLD + "Stage " + stage, ChatColor.GOLD + goal.replace("_", " "));
		
			player.getInventory().clear();
			if(goal.equalsIgnoreCase("Parkour")) {
				player.sendMessage(ChatColor.GOLD + "-----------=========   Parkour   =========-----------");
				player.sendMessage(ChatColor.GOLD + "- Get to the end of the parkour as fast as possible before the timer ends.");
				player.sendMessage(ChatColor.GOLD + "- Only a few player(s) are allowed to get to the next stage.");
				player.sendMessage(ChatColor.GOLD + "- If the timer hits '0' all of the remaining players will be teleported back to the lobby.");
				player.sendMessage(ChatColor.GOLD + "-----------=============================-----------");
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
			} else if(goal.equalsIgnoreCase("Shoot_Targets")) {
				player.sendMessage(ChatColor.GOLD + "-----------========= Shoot Targets =========-----------");
				player.sendMessage(ChatColor.GOLD + "- All of the players will get a bow and arrows to shoot targets.");
				player.sendMessage(ChatColor.GOLD + "- The targets will spawn randomly around the area.");
				player.sendMessage(ChatColor.GOLD + "- The one with the least succesfull shots will be kicked from the match.");
				player.sendMessage(ChatColor.GOLD + "-----------===============================-----------");
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
	
				ItemStack bow = new ItemStack(Material.BOW, 1);
				bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
				bow.addEnchantment(Enchantment.DURABILITY, 3);
	
				player.getInventory().setItem(0, bow);
				player.getInventory().setItem(8, new ItemStack(Material.ARROW));
			} else if(goal.equalsIgnoreCase("Boss_Fight")) {
				player.sendMessage(ChatColor.GOLD + "-----------========= Boss Fight =========-----------");
				player.sendMessage(ChatColor.GOLD + "- Team-up with the remaining players to kill to boss.");
				player.sendMessage(ChatColor.GOLD + "- If the boss has been killed all of the players that are alive will win the match.");
				player.sendMessage(ChatColor.GOLD + "- In this stage all of the players are no enemies of each other anymore.");
				player.sendMessage(ChatColor.GOLD + "-----------=============================-----------");
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
			} else if(goal.equalsIgnoreCase("PVP")) {
				player.sendMessage(ChatColor.GOLD + "-----------=========    PVP    =========-----------");
				player.sendMessage(ChatColor.GOLD + "- Kill all of the remaining players by using the items you will get.");
				player.sendMessage(ChatColor.GOLD + "- All of the players are supposed to kill each other.");
				player.sendMessage(ChatColor.GOLD + "- The last player alive wins.");
				player.sendMessage(ChatColor.GOLD + "-----------============================-----------");
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
	
				ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
				ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
				ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
				ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
	
				sword.addEnchantment(Enchantment.DURABILITY, 3);
				helmet.addEnchantment(Enchantment.DURABILITY, 3);
				chestplate.addEnchantment(Enchantment.DURABILITY, 3);
				leggings.addEnchantment(Enchantment.DURABILITY, 3);
				boots.addEnchantment(Enchantment.DURABILITY, 3);
	
				player.getInventory().setItem(0, sword);
				player.getInventory().setHelmet(helmet);
				player.getInventory().setChestplate(chestplate);
				player.getInventory().setLeggings(leggings);
				player.getInventory().setBoots(boots);
	
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
				if (getPlayers().size() > 1) {
					player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP will start in 10 seconds! Prepare yourself! *");
					EntityStats.antiDamage.add(player);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.core, new Runnable() {
						public void run() {
							EntityStats.antiDamage.remove(player);
							player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP has started! *");
						}
					}, 20 * 10);
				} else {
					FinishPVP();
				}
			}
		}
	}

	public void StopMatch() {
		for(Player players : getPlayers()) {
			getDungeonManager().TeleportToHub(players);
			removePlayer(players, false);
			
			playerList.clear();
			inLobby = true;
			spectators = 0;
			matchCounter = 0;
			matchID = 0;
			shootTargetsCount.clear();
		}
	}

	@SuppressWarnings("deprecation")
	public void addPlayer(Player player, boolean sendMessage) {
		ArrayList<Player> list = playerList;
		list.add(player);
		getDungeonManager().shootTargetsCount.put(player, Integer.valueOf(0));
		
		currentStage = 0;
		
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.getInventory().clear();
		for(Player joinedPlayers : getPlayers()) {
			if(sendMessage) {
				joinedPlayers.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " has joined the game! " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + getPlayers().size() + ChatColor.DARK_AQUA + "/" + ChatColor.AQUA + "5" + ChatColor.DARK_AQUA + ")");
			}
		}
		if(getPlayers().size() == 5) {
			StartStage(player, 0);
		}
		
		if(getPlayers().size() == 1) {
			matchCounter = 30;
		}
		
		resetScoreboard(player);
	}

	@SuppressWarnings("deprecation")
	public void removePlayer(Player player, boolean sendMessage) {
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.getInventory().clear();
		for (PotionEffect effects : player.getActivePotionEffects()) {
			player.removePotionEffect(effects.getType());
		}
		getDungeonManager().shootTargetsCount.remove(player);
		EntityStats.antiDamage.remove(player);

		ArrayList<Player> list = playerList;
		list.remove(player);
		if (getPlayers().size() > 0) {
			for (Player joinedPlayers : getPlayers()) {
				if (sendMessage) {
					joinedPlayers.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " has left the game! " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + getPlayers().size() + ChatColor.DARK_AQUA + "/" + ChatColor.AQUA + "5" + ChatColor.DARK_AQUA + ")");
				}
			}
		}
		
		if(player.getGameMode() == GameMode.SPECTATOR) {
			spectators -= 1;
		}
		
		if(getStage() == 0 && getPlayers().size() == 4) {
			for (Player joinedPlayers : getPlayers()) {
				matchCounter = 31;

				joinedPlayers.sendMessage(ChatColor.GRAY + "Timer stopped! Not enough players!");
				
				if(matchID != 0) {
					Bukkit.getServer().getScheduler().cancelTask(matchID);
				}
				
			}
		}
	}

	public void setSpectator(Player player) {
		spectators += 1;

		player.setGameMode(GameMode.SPECTATOR);

		player.sendMessage(ChatColor.AQUA + "You are now spectating!");
		for (Player players : getPlayers()) {
			if (players != player) {
				players.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " is now spectating!");
			}
		}
	}

	public void StartCountdown(final int stage) {
		if(matchID != 0) {
			Bukkit.getServer().getScheduler().cancelTask(matchID);
		}
		
		if(stage == 0) {
			//Default: 30
			matchCounter = 30;
		} else if(getGoal(stage).equalsIgnoreCase("Parkour")) {
			//Default: 300
			matchCounter = 300;
		} else if(getGoal(stage).equalsIgnoreCase("Shoot_Targets")) {
			//Default: 150
			matchCounter = 150;
		} else if(getGoal(stage).equalsIgnoreCase("Boss_Fight")) {
			//Default: 900
			matchCounter = 900;
		} else if(getGoal(stage).equalsIgnoreCase("PVP")) {
			//Default: 180
			matchCounter = 180;
		}
		this.targetCount = 0;
		
		matchID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.core, new Runnable() {
			@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
			public void run() {
				if (matchCounter > 1) {
					matchCounter--;
					for (Player players : getPlayers()) {
						getDungeonManager().resetScoreboard(players);
					}
					if(getStage() > 0 && getGoal(getStage()).equalsIgnoreCase("Shoot_Targets")) {
						targetCount += 1;
						if (targetCount == 3) {
							Random random = new Random();
							int r = random.nextInt(config.getConfigurationSection("stage-settings." + getStage()).getKeys(false).size()) + 1;
							for (String keys : config.getConfigurationSection("stage-settings." + getStage()).getKeys(false)) {
								if (keys.equalsIgnoreCase("shoottargets-" + r)) {
									Object l = config.get("stage-settings." + getStage() + ".shoottargets-" + r);
									Location location = (Location) l;

									final Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
									zombie.setBaby(false);
									zombie.getEquipment().clear();
									zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 5));
									zombie.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "* Target *");
									zombie.setCustomNameVisible(true);
									AI.getAIManager().removeAI(zombie);
									for (Player players : Bukkit.getOnlinePlayers()) {
										players.playEffect(zombie.getLocation().add(0.0D, -1.0D, 0.0D), Effect.ENDER_SIGNAL, 1);
									}
									Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.core, new Runnable() {
										public void run() {
											zombie.remove();
											for (Player players : Bukkit.getOnlinePlayers()) {
												players.playEffect(zombie.getLocation().add(0.0D, -1.0D, 0.0D), Effect.EXTINGUISH, 1);
											}
										}
									}, 160L);
								}
							}
							targetCount = 0;
						}
					}
					if(matchCounter == 60 || matchCounter == 30 || matchCounter == 10 || matchCounter == 5 || matchCounter == 4 || matchCounter == 3 || matchCounter == 2 || matchCounter == 1) {
						for (Player players : getPlayers()) {
							if (matchCounter > 1) {
								Title.sendFullTitle(players, 10, 40, 10, "", ChatColor.AQUA + "Stage " + (stage + 1) + " will start in " + matchCounter + " seconds...");
							} else {
								Title.sendFullTitle(players, 10, 40, 10, "", ChatColor.AQUA + "Stage " + (stage + 1) + " will start in " + matchCounter + " second...");
							}
						}
					}
				} else {
					Bukkit.getServer().getScheduler().cancelTask(matchID);
					Player p1;
					if (getStage() > 0 && getGoal(stage).equalsIgnoreCase("Shoot_Targets")) {
						if (getPlayers().size() > 2) {
							TreeMap<Player, Integer> scoreList = Tools.sortByValue(getDungeonManager().shootTargetsCount);
							p1 = (Player) new ArrayList(scoreList.keySet()).get(scoreList.size() - 1);
							Player p2 = (Player) new ArrayList(scoreList.keySet()).get(scoreList.size() - 2);

							TeleportToHub(p1);
							TeleportToHub(p2);
							removePlayer(p1, false);
							removePlayer(p2, false);
							p1.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
							p2.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
						} else if (getPlayers().size() > 1) {
							TreeMap<Player, Integer> scoreList = Tools.sortByValue(getDungeonManager().shootTargetsCount);
							p1 = (Player) new ArrayList(scoreList.keySet()).get(scoreList.size() - 1);
							TeleportToHub(p1);
							removePlayer(p1, false);
							p1.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
						}
					}
					for(Player players : getPlayers()) {
						players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
						if(stage == 0) {
							StartStage(players, 1);
						} else if(getGoal(stage).equalsIgnoreCase("Parkour")) {
							if(players.getGameMode() == GameMode.SPECTATOR) {
								StartStage(players, stage + 1);
							} else {
								getDungeonManager().TeleportToHub(players);
								removePlayer(players, false);
								players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
							}
						} else if(getGoal(stage).equalsIgnoreCase("Shoot_Targets")) {
							if(getPlayers().size() == 1) {
								StartStage(players, stage + 1);
								return;
							}
							StartStage(players, stage + 1);
						} else if(getGoal(stage).equalsIgnoreCase("PVP")) {
							FinishPVP();
						}
						if(getPlayers().size() == 0) {
							StopMatch();
						}
					}
				}
			}
		}, 0, 20);
	}

	@SuppressWarnings("deprecation")
	public void FinishPVP() {
		Random random = new Random();
		int i = random.nextInt(getPlayers().size());
		Player winner = (Player) getPlayers().get(i);
		winner.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------------------------------------------");
		winner.sendMessage(ChatColor.GOLD + "You completed and won the Dungeon!");
		winner.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------------------------------------------");
		for(final Player players : getPlayers()) {
			players.getInventory().clear();
			players.setHealth(players.getMaxHealth());
			players.setFoodLevel(20);
			
			if(players != winner) {
				players.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "-------------------------------------------------");
				players.sendMessage(ChatColor.RED + "You lost the Dungeon!");
				players.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "-------------------------------------------------");
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.core, new Runnable() {
				public void run() {
					getDungeonManager().TeleportToHub(players);
					removePlayer(players, false);
					players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

					StopMatch();
				}
			}, 120L);
		}
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> list = new ArrayList<Player>();
		for(Player players : playerList) {
			list.add(players);
		}
		return list;
	}

	public int getStage() {
		return currentStage;
	}

	public String getGoal(int stage) {
		return getConfig().getString("goals." + stage);
	}

	public int getSpectators() {
		return spectators;
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}
}