package me.nucha.kokumin.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUtil {

	public TitleUtil() {

	}

	public void sendTitle(Player p, String text, EnumTitleAction action) {
		text = text.replaceAll("§", "\u00a7");
		PacketPlayOutTitle title = new PacketPlayOutTitle(action, serialize(text));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}

	public void sendTitle(Player p, String text, EnumTitleAction action, int fadeIn, int stay, int fadeOut) {
		text = text.replaceAll("§", "\u00a7");
		PacketPlayOutTitle title2 = new PacketPlayOutTitle(action, serialize(text));
		PacketPlayOutTitle title = new PacketPlayOutTitle(fadeIn, stay, fadeIn);
		PlayerConnection pc = ((CraftPlayer) p).getHandle().playerConnection;
		pc.sendPacket(title);
		pc.sendPacket(title2);
	}

	public void clearTitle(Player p) {
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.CLEAR, serialize(""));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}

	private IChatBaseComponent serialize(String text) {
		return ChatSerializer.a("{\"text\":\"" + text + "\"}");
	}

}
