package com.tcc.SkelUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.tcc.SkelUtil.objects.gui.GUIListener;

public class SkelUtil extends JavaPlugin {
	
	private static SkelUtil instance;
	
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&aSkelUtil enabled successfully!"));
		
		//Register listeners
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
	}

	@Override
	public void onDisable() {
		
	}

	public static SkelUtil getInstance() {
		return instance;
	}


}
