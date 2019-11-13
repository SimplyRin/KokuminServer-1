package me.nucha.kokumin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandRename implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (args.length >= 1) {
			if (p.getItemInHand() != null) {
				ItemStack item = p.getItemInHand();
				ItemMeta itemMeta = item.getItemMeta();
				String name = args[0];
				if (args.length > 1) {
					for (int i = 1; i < args.length; i++) {
						name += " " + args[i];
					}
				}
				name = ChatColor.translateAlternateColorCodes('&', name);
				itemMeta.setDisplayName(name);
				item.setItemMeta(itemMeta);
				p.setItemInHand(item);
				sender.sendMessage("§aアイテムの名前を §o" + name + " §aに変更しました");
			} else {
				p.sendMessage("§cアイテムを手に持ってください");
			}
			return true;
		}
		sender.sendMessage("§cUsage: /rename <name...>");
		return true;
	}

}
