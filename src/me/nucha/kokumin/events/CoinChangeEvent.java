package me.nucha.kokumin.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoinChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private OfflinePlayer player;
	private int amount;
	private String comment;
	private boolean hascomment;

	public CoinChangeEvent(OfflinePlayer player, int amount, String comment) {
		this.player = player;
		this.amount = amount;
		if (comment != null) {
			this.comment = comment;
			this.hascomment = true;
		} else {
			this.comment = "";
			this.hascomment = false;
		}
	}

	public CoinChangeEvent(OfflinePlayer player, int amount) {
		this.player = player;
		this.amount = amount;
		this.comment = "";
		this.hascomment = false;
	}

	public int getAmount() {
		return amount;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public String getComment() {
		return comment;
	}

	public boolean hasComment() {
		return hascomment;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
