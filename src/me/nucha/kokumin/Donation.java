package me.nucha.kokumin;

import org.bukkit.OfflinePlayer;

public class Donation {

	private static Main plugin = Main.getInstance();

	public static int getDonation(OfflinePlayer p) {
		return plugin.getConfig().getInt(p.getUniqueId() + ".donation");
	}

	public static void addDonation(OfflinePlayer p, int amount) {
		plugin.getConfig().set(p.getUniqueId() + ".donation", (getDonation(p) + amount));
	}

	public static void takeDonation(OfflinePlayer p, int amount) {
		plugin.getConfig().set(p.getUniqueId() + ".donation", (getDonation(p) - amount));
	}

}
