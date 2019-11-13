package me.nucha.kokumin.commands;

import me.nucha.kokumin.kabu.GuiKabu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOpenKabu implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage("§6" + args[0] + " §cはオフラインです");
				return true;
			}
			GuiKabu.open(target);
			sender.sendMessage("§a" + target.getName() + "に株のメニューを表示しました");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cゲーム内から実行してください");
			return true;
		}
		Player p = (Player) sender;
		GuiKabu.open(p);
		return true;
	}

}
