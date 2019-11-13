package me.nucha.kokumin.listeners;

import me.nucha.kokumin.Main;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PearlListener implements Listener {

	private Main plugin;

	public PearlListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (!Main.isTesting()) {
			return;
		}
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			Player p = event.getPlayer();
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					EntityPlayer cp = ((CraftPlayer) p).getHandle();
					if (cp.inBlock()) {
						p.teleport(event.getTo().getBlock().getLocation());
						p.sendMessage("Modified");
					}
				}
			}, 2L);
		}
	}
}
