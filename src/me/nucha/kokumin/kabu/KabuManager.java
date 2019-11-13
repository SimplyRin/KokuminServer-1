package me.nucha.kokumin.kabu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.coin.Coin;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KabuManager {

	private Main plugin;
	private BukkitRunnable hendouTask;
	private int price;

	public KabuManager(Main plugin) {
		this.plugin = plugin;
		if (!plugin.getConfig().isSet("kabu.price")) {
			plugin.getConfig().set("kabu.price", 100);
			plugin.saveConfig();
			price = 100;
		} else {
			price = plugin.getConfig().getInt("kabu.price");
		}
		hendouTask = new BukkitRunnable() {
			Random rnd = new Random();

			@Override
			public void run() {
				double random = Math.random();
				int hendou = 0;
				if (random > 0.5) {
					hendou = rnd.nextInt(10) + 5;
				} else {
					hendou = -(rnd.nextInt(10) + 5);
				}
				price += hendou;
				if (price <= 50) {
					price = 50;
				}
				plugin.getConfig().set("kabu.price", price);
				List<String> changes = getPriceChanges();
				if (changes == null) {
					changes = new ArrayList<>();
				}
				if (changes.size() == 10) {
					changes.remove(0);
				}
				String change = null;
				String date = new SimpleDateFormat("kk:mm:ss").format(new Date());
				if (hendou > 0) {
					change = "§a↑" + Math.abs(hendou) + "円§7 - " + date;
				} else {
					change = "§3↓" + Math.abs(hendou) + "円§7 - " + date;
				}
				changes.add(change);
				plugin.getConfig().set("kabu.price-changes", changes);
				plugin.saveConfig();
				for (Player viewers : new ArrayList<Player>(GuiKabu.viewers)) {
					GuiKabu.open(viewers);
				}
			}
		};
		hendouTask.runTaskTimer(plugin, 0L, 20 * 60);
	}

	public boolean buyKabu(Player p, int amount) {
		if (Coin.hasCoin(p, amount * price)) {
			plugin.getConfig().set(p.getUniqueId().toString() + ".kabu", getKabus(p) + amount);
			Coin.takeCoin(p, (price * amount), price + "円の株を" + amount + "個購入");
			return true;
		}
		return false;
	}

	public boolean sellKabu(Player p, int amount) {
		if (hasKabu(p, amount)) {
			plugin.getConfig().set(p.getUniqueId().toString() + ".kabu", getKabus(p) - amount);
			Coin.addCoin(p, (price * amount), price + "円の株を" + amount + "個売却", false);
			return true;
		}
		return false;
	}

	public int getKabus(Player p) {
		int kabus = 0;
		if (plugin.getConfig().isSet(p.getUniqueId().toString() + ".kabu")) {
			kabus = plugin.getConfig().getInt(p.getUniqueId().toString() + ".kabu");
		}
		return kabus;
	}

	public boolean hasKabu(Player p, int atleast) {
		return getKabus(p) >= atleast;
	}

	public int getPrice() {
		return price;
	}

	public List<String> getPriceChanges() {
		if (plugin.getConfig().isSet("kabu.price-changes")) {
			return plugin.getConfig().getStringList("kabu.price-changes");
		}
		return null;
	}

}
