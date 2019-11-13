package me.nucha.kokumin.invitation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.nucha.kokumin.Main;

import org.bukkit.OfflinePlayer;

public class Invite {

	private OfflinePlayer owner;
	private Main plugin;
	private String code;
	private List<String> accepts;

	private String PATH_CODE;
	private String PATH_ACCEPTS;

	public Invite(OfflinePlayer owner, Main plugin) {
		this.owner = owner;
		this.PATH_CODE = "invites." + owner.getUniqueId().toString() + ".code";
		this.PATH_ACCEPTS = "invites." + owner.getUniqueId().toString() + ".accepts";
		this.plugin = plugin;
		if (hasCreated()) {
			this.code = plugin.getConfig().getString(PATH_CODE);
		}
		if (hasAcceptedAtLeastOnce()) {
			this.accepts = plugin.getConfig().getStringList(PATH_ACCEPTS);
		}
	}

	public boolean create() {
		if (hasCreated()) {
			return false;
		}
		String code = Main.inviteManager.createInviteCode();
		if (Main.inviteManager.getInviteByCode(code) != null) {
			return false;
		}
		this.code = code;
		plugin.getConfig().set(PATH_CODE, code);
		plugin.saveConfig();
		return true;
	}

	public void accept(UUID by) {
		if (hasAccepted(by)) {
			return;
		}

		if (!hasAcceptedAtLeastOnce()) {
			accepts = new ArrayList<String>();
		}
		accepts.add(by.toString());
		plugin.getConfig().set(PATH_ACCEPTS, accepts);
		plugin.saveConfig();
	}

	public boolean hasAccepted(UUID by) {
		return hasAcceptedAtLeastOnce() && getAccepts().contains(by.toString());
	}

	public boolean hasCreated() {
		return plugin.getConfig().isSet(PATH_CODE);
	}

	public boolean hasAcceptedAtLeastOnce() {
		return plugin.getConfig().isSet(PATH_ACCEPTS);
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public String getCode() {
		return code;
	}

	public List<String> getAccepts() {
		return accepts;
	}

}
