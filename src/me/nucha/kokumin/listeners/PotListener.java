package me.nucha.kokumin.listeners;

import me.nucha.kokumin.Main;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class PotListener implements Listener {

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (!Main.isFastPotEnabled()) {
			return;
		}
		if (event.getEntityType() == EntityType.SPLASH_POTION) {
			Projectile projectile = event.getEntity();
			if (projectile.getShooter() instanceof Player && ((Player) projectile.getShooter()).isSprinting()) {
				Vector velocity = projectile.getVelocity();
				velocity.setY(velocity.getY() - 1.0);
				projectile.setVelocity(velocity);
			}
		}
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if (!Main.isFastPotEnabled()) {
			return;
		}
		if (event.getEntity().getShooter() instanceof Player) {
			final Player shooter = (Player) event.getEntity().getShooter();
			if (shooter.isSprinting() && event.getIntensity((LivingEntity) shooter) > 0.5) {
				event.setIntensity((LivingEntity) shooter, 1.0);
			}
		}
	}

}
