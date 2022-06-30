package net.syobonoon.plugin.micloslantern;

import org.bukkit.plugin.java.JavaPlugin;

public class MicLoSLantern extends JavaPlugin {

    @Override
    public void onEnable() {
    	new LanternListener(this);
    	getLogger().info("onEnable");
    }
}
