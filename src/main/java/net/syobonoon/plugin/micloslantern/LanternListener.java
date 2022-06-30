package net.syobonoon.plugin.micloslantern;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LanternListener implements Listener{
	private Plugin plugin;
	List<Player> nearplayer = new ArrayList<Player>();

	public LanternListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	//プレイヤーが動いたらランタンを光らす
	@EventHandler
	public void playerMoveLantern(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		World world = p.getWorld();
		Location loc_p = p.getLocation();

		if (nearplayer.contains(p)) return;

		int x = loc_p.getBlockX();
		int y = loc_p.getBlockY();
		int z = loc_p.getBlockZ();

		Block block = judgeLantern(world, x, y, z);
		if(block == null) return;

		Location loc_l = block.getLocation();
		nearplayer.add(p);
		lightLanternContinue(p, loc_l);
		return;
	}

	//プレイヤーが離れるまで光らせ続ける
	private void lightLanternContinue(Player p, Location loc) {
		BukkitRunnable task = new BukkitRunnable() {
			int loop_cnt = 0;
			int x_l = loc.getBlockX();
			int y_l = loc.getBlockY();
			int z_l = loc.getBlockZ();

			public void run() {

				//プレイヤーが離れたら
				if(Bukkit.getServer().getPlayer(p.getName()) == null || p.getLocation().distance(loc) > 3 || loop_cnt > 4*60) {
					this.cancel();
					nearplayer.remove(p);
				}
				p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, x_l+0.5, y_l+0.25, z_l+0.5, 0);
				loop_cnt++;
			}
		};
		task.runTaskTimer(plugin, 0, 5);
	}

	//半径1mのブロックにランタンが存在するかどうかを確認する関数
	public Block judgeLantern(World world, int x, int y, int z) {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				for(int k = -1; k <= 1; k++) {
					Block block = world.getBlockAt(new Location(world, x+i, y+j, z+k));
					if (block.getType() == Material.SOUL_LANTERN) return block;
				}
			}
		}
		return null;
	}
}
