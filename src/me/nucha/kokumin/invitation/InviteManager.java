package me.nucha.kokumin.invitation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.nucha.kokumin.Main;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class InviteManager {

	private Main plugin;

	public InviteManager(Main plugin) {
		this.plugin = plugin;
	}

	public Invite getInviteByCode(String str) {
		if (getInvites() != null) {
			for (Invite invite : getInvites()) {
				if (invite.getCode().equalsIgnoreCase(str)) {
					return invite;
				}
			}
		}
		return null;
	}

	public Invite getInviteByOwner(OfflinePlayer p) {
		if (getInvites() != null) {
			for (Invite invite : getInvites()) {
				if (invite.getOwner().getUniqueId().equals(p.getUniqueId())) {
					return invite;
				}
			}
		}
		return null;
	}

	public List<Invite> getInvites() {
		if (plugin.getConfig().isSet("invites")) {
			List<Invite> invites = new ArrayList<Invite>();
			for (String uuid : plugin.getConfig().getConfigurationSection("invites").getKeys(false)) {
				Invite invite = new Invite(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), plugin);
				if (invite.hasCreated()) {
					invites.add(invite);
				}
			}
			return invites;
		}
		return null;
	}

	public String createInviteCode() {
		Random random = new Random();
		return RandomStringUtils.randomAlphabetic(random.nextInt(6) + 4).toLowerCase();
	}

	public boolean hasAcceptedInvite(UUID by) {
		return getAcceptedInvite(by) != null;
	}

	public Invite getAcceptedInvite(UUID by) {
		if (getInvites() != null) {
			for (Invite invite : getInvites()) {
				if (invite.hasAcceptedAtLeastOnce() && invite.hasAccepted(by)) {
					return invite;
				}
			}
		}
		return null;
	}

}
