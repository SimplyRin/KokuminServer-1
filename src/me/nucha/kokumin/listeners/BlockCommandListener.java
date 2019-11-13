package me.nucha.kokumin.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlockCommandListener implements Listener {

	private List<String> blockedcmds;

	public BlockCommandListener() {
		blockedcmds = new ArrayList<>();
		blockedcmds.add("//calc");
		blockedcmds.add("//calculate");
		blockedcmds.add("//solve");
		blockedcmds.add("//eval");
		blockedcmds.add("//evaluate");
		blockedcmds.add("/worldedit:/calc");
		blockedcmds.add("/worldedit:/calculate");
		blockedcmds.add("/worldedit:/solve");
		blockedcmds.add("/worldedit:/eval");
		blockedcmds.add("/worldedit:/evaluate");
	}

	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if (!p.isOp()) {
			if (blockedcmds.contains(event.getMessage().toLowerCase())) {
				p.sendMessage("§cそのコマンドは使うことが禁止されています");
				event.setCancelled(true);
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (all.isOp()) {
						all.sendMessage("§8[Command Blocked§8] §c" + p.getName() + "§8: " + event.getMessage());

					}
				}
			}
		}
	}

}
