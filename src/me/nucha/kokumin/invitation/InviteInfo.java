package me.nucha.kokumin.invitation;

import me.nucha.kokumin.Main;

import org.bukkit.OfflinePlayer;

public class InviteInfo {

	private OfflinePlayer owner;
	private Main plugin;
	private Invite invite;
	private int timesAccepted;

	public InviteInfo(OfflinePlayer player, Main plugin) {
		this.owner = player;
		this.plugin = plugin;
		this.invite = new Invite(this.owner, this.plugin);
		if (!this.invite.hasAcceptedAtLeastOnce()) {
			this.timesAccepted = 0;
		} else {
			this.timesAccepted = this.invite.getAccepts().size();
		}
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public Invite getInvite() {
		return invite;
	}

	public int getTimesAccepted() {
		return timesAccepted;
	}

}
