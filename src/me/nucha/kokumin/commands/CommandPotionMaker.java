package me.nucha.kokumin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nucha.kokumin.listeners.gui.GuiPotionMaker;

public class CommandPotionMaker implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cプレイヤーのみ実行可能です");
			return true;
		}
		Player p = (Player) sender;
		GuiPotionMaker.open(p);
		return true;
	}

}
