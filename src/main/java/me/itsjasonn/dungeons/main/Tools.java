package me.itsjasonn.dungeons.main;

import java.util.HashMap;
import java.util.TreeMap;
import org.bukkit.entity.Player;

public class Tools {
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static TreeMap<Player, Integer> sortByValue(HashMap<Player, Integer> shootTargetsCount) { java.util.Map<Player, Integer> map = shootTargetsCount;
		java.util.Comparator<Player> bvc = new ValueComparator(map);
		TreeMap<Player, Integer> sorted_map = new TreeMap<Player, Integer>(bvc);
		
		sorted_map.putAll(map);
		return sorted_map;
	}
}