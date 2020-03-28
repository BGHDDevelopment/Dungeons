package me.itsjasonn.dungeons.listener;

import java.util.ArrayList;
import me.itsjasonn.dungeons.dungeon.Dungeon;
import me.itsjasonn.dungeons.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class EntityStats implements Listener {
	Core core;
	
	public EntityStats(Core core) {
		this.core = core;
	}
	
	public static ArrayList<Player> antiDamage = new ArrayList<Player>();
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
	    if(event.getEntity() instanceof Player) {
	    	Player player = (Player)event.getEntity();
	    	if(Dungeon.getDungeonManager().isInGame(player)) {
	    		if(antiDamage.contains(player)) {
	    			event.setCancelled(true);
	    			return;
	    		}
	    		if(Dungeon.getDungeonManager().getStage() > 0 && Dungeon.getDungeonManager().getGoal(Dungeon.getDungeonManager().getStage()).equalsIgnoreCase("PVP")) {
	    			event.setCancelled(false);
	    		} else {
	    			event.setCancelled(true);
	    		}
	    	}
	    }
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Zombie) {
			Zombie zombie = (Zombie)event.getEntity();
			if(event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow)event.getDamager();
				if(arrow.getShooter() instanceof Player) {
					Player player = (Player)arrow.getShooter();
					if(Dungeon.getDungeonManager().isInGame(player)) {
						arrow.remove();
						zombie.remove();
						
						for(Player players : Bukkit.getOnlinePlayers()) {
							players.playSound(zombie.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_WEAK, 12.0F, 2.0F);
						}
						Dungeon.getDungeonManager().shootTargetsCount.put(player, Integer.valueOf(((Integer)Dungeon.getDungeonManager().shootTargetsCount.get(player)).intValue() + 1));
						for (Player players : Dungeon.getDungeonManager().getPlayers()) {
							Dungeon.getDungeonManager().resetScoreboard(players);
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
  
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(Dungeon.getDungeonManager().isInGame(player)) {
				event.setCancelled(true);
				player.setFoodLevel(20);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Dungeon.getDungeonManager().isInGame(player)) {
			event.setCancelled(true);
		}
	}
  
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Dungeon.getDungeonManager().isInGame(player)) {
			event.setCancelled(true);
		}
	}
  
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(Dungeon.getDungeonManager().isInGame(player)) {
				event.getDrops().clear();
				player.getInventory().clear();
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				if(Dungeon.getDungeonManager().getGoal(Dungeon.getDungeonManager().getStage()).equalsIgnoreCase("PVP")) {
					Dungeon.getDungeonManager().removePlayer(player, false);
					Dungeon.getDungeonManager().TeleportToHub(player);
					
					Dungeon.getDungeonManager().FinishPVP();
				}
			}
		}
	}
}