package me.nucha.kokumin.coin;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.nucha.kokumin.utils.TimeUtil;

public class CoinBooster {

	private double amplifier;
	private Date expire;
	private OfflinePlayer owner;
	private boolean active;

	public CoinBooster(double amplifier, Date expire, OfflinePlayer owner) {
		this.amplifier = amplifier;
		this.expire = expire;
		this.owner = owner;
		this.active = false;
	}

	public void activate() {
		if (System.currentTimeMillis() > expire.getTime()) {
			return;
		}
		active = true;
		String till = TimeUtil.toDiffTime(System.currentTimeMillis(), expire.getTime(), "HH時間 mm分 ss秒");
		Bukkit.broadcastMessage("§e§l" + owner.getName() + "が x" + amplifier + " コインブースターを有効化しました！\n" + "期限: " + till);
		Coin.registerBooster(this);
	}

	public void tick() {
		if (!active) {
			return;
		}
		if (System.currentTimeMillis() > expire.getTime()) {
			expire();
		}
	}

	public void expire() {
		if (!active) {
			return;
		}
		active = false;
		Bukkit.broadcastMessage("§9§l" + owner.getName() + "の x" + amplifier + " コインブースターの期限が切れました！");
		Coin.unregisterBooster(this);
	}

	public boolean isActive() {
		return active;
	}

	public double getAmplifier() {
		return amplifier;
	}

	public Date getExpire() {
		return expire;
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

}
