package me.nucha.kokumin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

public class Premium {

	private static Main plugin;
	private static List<String> premiums;

	public static void init(Main plugin) {
		Premium.plugin = plugin;
		premiums = new ArrayList<>();
		for (String sectionName : plugin.getConfig().getKeys(false)) {
			if (plugin.getConfig().isSet(sectionName + ".premium") && plugin.getConfig().getBoolean(sectionName + ".premium")) {
				premiums.add(sectionName);
			}
		}
	}

	public static void setPremium(OfflinePlayer p, boolean premium) {
		plugin.getConfig().set(p.getUniqueId().toString() + ".premium", premium);
		if (premium) {
			premiums.add(p.getUniqueId().toString());
		} else {
			premiums.remove(p.getUniqueId().toString());
		}
	}

	public static boolean isPremium(OfflinePlayer p) {
		return premiums.contains(p.getUniqueId().toString());
	}

}
