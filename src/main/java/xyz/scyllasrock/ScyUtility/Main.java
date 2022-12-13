package xyz.scyllasrock.ScyUtility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.scyllasrock.ScyUtility.objects.gui.GUIListener;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&aScyUtility enabled successfully!"));
		
		//Register listeners
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
	}

	@Override
	public void onDisable() {
		
	}

	public static Main getInstance() {
		return instance;
	}


}
