package me.nucha.kokumin.commands;

import me.nucha.kokumin.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMOTD implements CommandExecutor {

	private Main plugin;

	public CommandMOTD(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			String msg = args[0];
			if (args.length > 1) {
				for (int in = 1; in < args.length; in++) {
					msg += " " + args[in];
				}
			}
			Main.setMotdComment(msg);
			sender.sendMessage("§aMOTDを設定しました: " + ChatColor.translateAlternateColorCodes('&', msg));
			plugin.getConfig().set("motd_comment", msg);
			plugin.saveConfig();
			return true;
		}
		sender.sendMessage("§a/setmotd <comment>");
		sender.sendMessage("§a現在のMOTD: " + ChatColor.translateAlternateColorCodes('&', Main.getMotdComment()));
		return true;
	}

}
