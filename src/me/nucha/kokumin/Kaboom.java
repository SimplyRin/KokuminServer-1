package me.nucha.kokumin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Kaboom {

	private static boolean cankaboom = true;
	private static List<String> nofall = new ArrayList<String>();

	public static void kaboom() {
		if (cankaboom) {
			cankaboom = false;
			for (Player all : Bukkit.getOnlinePlayers()) {
				registerNoFall(all);
				Location l = all.getLocation();
				l.getWorld().strikeLightningEffect(l);
				Vector v = all.getVelocity();
				all.setVelocity(v.add(new Vector(0, 1, 0)));
			}
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					cankaboom = true;
				}
			}, 100L);
		}
	}

	public static boolean canKaboom() {
		return cankaboom;
	}

	public static void registerNoFall(Player p) {
		if (!nofall.contains(p.getName())) {
			nofall.add(p.getName());
		}
	}

	public static void unregisterNoFall(Player p) {
		if (nofall.contains(p.getName())) {
			nofall.remove(p.getName());
		}
	}

	public static boolean hasNoFall(Player p) {
		return nofall.contains(p.getName());
	}

}
