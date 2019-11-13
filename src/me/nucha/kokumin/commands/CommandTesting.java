package me.nucha.kokumin.commands;

import me.nucha.kokumin.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTesting implements CommandExecutor {

	public CommandTesting() {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Main.isTesting()) {
			Main.setTesting(false);
			sender.sendMessage("§6Testing: §cOff");
		} else {
			Main.setTesting(true);
			sender.sendMessage("§6Testing: §aOn");
		}
		return true;
	}

}
