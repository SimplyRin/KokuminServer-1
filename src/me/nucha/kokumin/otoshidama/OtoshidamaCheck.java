package me.nucha.kokumin.otoshidama;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.coin.Coin;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class OtoshidamaCheck {

	private Main plugin;
	private List<String> claimers;
	private FileConfiguration config;

	public OtoshidamaCheck(Main plugin) {
		this.plugin = plugin;
		this.claimers = new ArrayList<String>();
		this.config = plugin.getConfig();
		if (plugin.getConfig().isSet("otoshidama.claimers")) {
			this.claimers = config.getStringList("otoshidama.claimers");
		}
	}

	public boolean isAvailable() {
		String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		return (date.equalsIgnoreCase("2018/01/01")) || (date.equalsIgnoreCase("2018/01/02"))
				|| (date.equalsIgnoreCase("2018/01/03")) || (date.equalsIgnoreCase("2018/01/04"))
				|| (date.equalsIgnoreCase("2018/01/05")) || (date.equalsIgnoreCase("2018/01/06"));
	}

	public void claim(Player p) {
		if (!claimers.contains(p.getUniqueId().toString())) {
			p.sendMessage("§a§l================================");
			p.sendMessage("");
			p.sendMessage("    §e§l明けまして§d§lおめでとうございます§d§l！");
			p.sendMessage("    §d§lHappy §e§lNew Year§d§l!");
			p.sendMessage("");
			p.sendMessage("    §b今年もどうぞよろしくお願い申し上げます。");
			p.sendMessage("");
			p.sendMessage("§a§l================================");
			Coin.addCoin(p, 2018, "Happy New Year!");
			FireworkEffect effect1 = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.RED)
					.withFade(Color.WHITE).build();
			FireworkEffect effect2 = FireworkEffect.builder().with(Type.CREEPER).withColor(Color.LIME)
					.withFade(Color.GREEN).build();
			Random rnd = new Random();
			BukkitRunnable runnable = new BukkitRunnable() {
				int i = 0;

				@Override
				public void run() {
					if (i <= 5) {
						if (i < 5) {
							Firework firework = p.getWorld().spawn(p.getLocation().clone()
									.add(rnd.nextInt(6) - 3, rnd.nextInt(3), rnd.nextInt(6) - 3), Firework.class);
							FireworkMeta fireworkMeta = firework.getFireworkMeta();
							fireworkMeta.addEffect(effect1);
							fireworkMeta.setPower(2);
							firework.setFireworkMeta(fireworkMeta);
						} else {
							Firework firework = p.getWorld().spawn(p.getLocation().clone(), Firework.class);
							FireworkMeta fireworkMeta = firework.getFireworkMeta();
							fireworkMeta.addEffect(effect2);
							fireworkMeta.setPower(2);
							firework.setFireworkMeta(fireworkMeta);
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 0.5f);
						}
						i++;
						return;
					}
					cancel();
				}
			};
			claimers.add(p.getUniqueId().toString());
			save();
			runnable.runTaskTimer(plugin, 5, 5);
		}
	}

	public void save() {
		plugin.getConfig().set("otoshidama.claimers", claimers);
	}

}
