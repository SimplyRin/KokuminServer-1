package me.nucha.kokumin.coin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.events.CoinChangeEvent;

public class Coin {

	private static Main plugin;
	private static List<CoinBooster> boosters;

	public static void init(Main plugin) {
		Coin.plugin = plugin;
		boosters = new ArrayList<CoinBooster>();
		if (plugin.getConfig().isSet("boosters")) {
			for (String id : plugin.getConfig().getConfigurationSection("boosters").getKeys(false)) {
				OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString("boosters." + id + ".owner")));
				Date expire = new Date(plugin.getConfig().getLong("boosters." + id + ".expire"));
				double amplifier = plugin.getConfig().getDouble("boosters." + id + ".amplifier");
				CoinBooster booster = new CoinBooster(amplifier, expire, owner);
				booster.activate();
			}
		}
	}

	public static void shutdown() {
		plugin.getConfig().set("boosters", null);
		int index = 0;
		for (CoinBooster booster : boosters) {
			if (booster.isActive()) {
				plugin.getConfig().set("boosters." + index + ".owner", booster.getOwner().getUniqueId().toString());
				plugin.getConfig().set("boosters." + index + ".expire", booster.getExpire().getTime());
				plugin.getConfig().set("boosters." + index + ".amplifier", booster.getAmplifier());
				index++;
			}
		}
		plugin.saveConfig();
	}

	public static int getCoin(OfflinePlayer p) {
		return plugin.getConfig().getInt(p.getUniqueId() + ".coin");
	}

	public static void addCoin(OfflinePlayer p, int amount, boolean boost) {
		double multip1ier = 1;
		int multiplier = 1;
		String message = "";
		if (boost) {
			boolean first = true;
			for (CoinBooster booster : boosters) {
				multip1ier += booster.getAmplifier();
				if (first) {
					message += booster.getOwner().getName() + "'s Coin Booster";
					first = false;
				} else {
					message += ", " + booster.getOwner().getName() + "'s Coin Booster";
				}
			}
			if (multip1ier > 1) {
				multip1ier -= 1;
			}
			multiplier = Math.round(Math.round(multip1ier));
			amount *= multiplier;
		}
		if (p.isOnline()) {
			if (multiplier > 1) {
				p.getPlayer().sendMessage("§6+" + amount + " Coins (" + message + "§6)");
			} else {
				p.getPlayer().sendMessage("§6+" + amount + " Coins");
			}
		}
		plugin.getConfig().set(p.getUniqueId() + ".coin", (getCoin(p) + amount));
		CoinChangeEvent event = new CoinChangeEvent(p, amount, message);
		Bukkit.getServer().getPluginManager().callEvent(event);
		// plugin.saveConfig();
	}

	public static void addCoin(OfflinePlayer p, int amount, String message, boolean boost) {
		double multip1ier = 1;
		int multiplier = 1;
		if (boost) {
			for (CoinBooster booster : boosters) {
				multip1ier += booster.getAmplifier();
				message += ", " + booster.getOwner().getName() + "'s Coin Booster";
			}
			if (multip1ier > 1) {
				multip1ier -= 1;
			}
			multiplier = Math.round(Math.round(multip1ier));
			amount *= multiplier;
		}
		if (p.isOnline()) {
			p.getPlayer().sendMessage("§6+" + amount + " Coins (" + message + "§6)");
		}
		plugin.getConfig().set(p.getUniqueId() + ".coin", (getCoin(p) + amount));
		CoinChangeEvent event = new CoinChangeEvent(p, amount, message);
		Bukkit.getServer().getPluginManager().callEvent(event);// plugin.saveConfig();
	}

	public static void addCoin(OfflinePlayer p, int amount, String message) {
		addCoin(p, amount, message, true);
	}

	public static void addCoin(OfflinePlayer p, int amount) {
		addCoin(p, amount, true);
	}

	public static void takeCoin(OfflinePlayer p, int amount) {
		if (p.isOnline()) {
			p.getPlayer().sendMessage("§c-" + amount + " Coins");
		}
		int takeAmount = (getCoin(p) - amount);
		if (takeAmount < 0) {
			takeAmount = 0;
		}
		plugin.getConfig().set(p.getUniqueId() + ".coin", takeAmount);
		CoinChangeEvent event = new CoinChangeEvent(p, takeAmount, null);
		Bukkit.getServer().getPluginManager().callEvent(event);
		// plugin.saveConfig();
	}

	public static void takeCoin(OfflinePlayer p, int amount, String message) {
		if (p.isOnline()) {
			p.getPlayer().sendMessage("§c-" + amount + " Coins (" + message + "§c)");
		}
		int takeAmount = (getCoin(p) - amount);
		if (takeAmount < 0) {
			takeAmount = 0;
		}
		plugin.getConfig().set(p.getUniqueId() + ".coin", takeAmount);
		CoinChangeEvent event = new CoinChangeEvent(p, takeAmount, message);
		Bukkit.getServer().getPluginManager().callEvent(event);
		// plugin.saveConfig();
	}

	public static boolean hasCoin(OfflinePlayer p, int amount) {
		return getCoin(p) >= amount;
	}

	public static List<CoinBooster> getActiveBoosters() {
		return boosters;
	}

	public static void registerBooster(CoinBooster booster) {
		if (!boosters.contains(booster)) {
			boosters.add(booster);
		}
	}

	public static void unregisterBooster(CoinBooster booster) {
		if (boosters.contains(booster)) {
			boosters.remove(booster);
		}
	}

	public static boolean hasActivatedBooster(Player p) {
		return getActivatedBooster(p) != null;
	}

	public static CoinBooster getActivatedBooster(Player p) {
		for (CoinBooster booster : getActiveBoosters()) {
			if (booster.isActive() && booster.getOwner().getName().equalsIgnoreCase(p.getName())) {
				return booster;
			}
		}
		return null;
	}

}
