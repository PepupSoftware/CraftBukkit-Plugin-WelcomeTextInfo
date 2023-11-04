package de.pepupsoftware.craftbukkit.plugin.welcome;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WelcomeTextInfo extends JavaPlugin {

	private WelcomeTextInfoListener wListener;
	private WelcomeTextInfoCommands commands;
    private PlayerTimeStore ptimestore;
	private int task;
	private int taskplayer;
	
	
@Override	
    public void onEnable() {

    	try {
    		System.out.println("[WelcomeTextInfo-Plugin] Plugin ist gestartet!");
            task = 0;
            taskplayer = 0;
            ptimestore = new PlayerTimeStore(this.getClass().getSimpleName());
            mcausfuehren(Bukkit.getServer(), "gamerule keepInventory true");
            mcausfuehren(Bukkit.getServer(), "scoreboard objectives add Gesamt dummy \"Gesamt Punkte\"");
            mcausfuehren(Bukkit.getServer(), "scoreboard objectives setdisplay sidebar Gesamt");
            wListener = new WelcomeTextInfoListener(this);
            commands = new WelcomeTextInfoCommands(this, wListener);
            getCommand("gamertimelist").setExecutor(commands);
            getCommand("gametimer").setExecutor(commands);
	     	taskplayer = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,  () -> this.wListener.player_display_update(), 0l, 20l); 
		} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
    	
    }
    
    public void onDisable() {

    	try {
    		System.out.println("[WelcomeTextInfo-Plugin] Plugin ist gestoppt!");
    		if (task != 0) {
    			Bukkit.getScheduler().cancelTask(task);
    			task = 0;
    		}
    		if (taskplayer != 0) {
    			Bukkit.getScheduler().cancelTask(taskplayer);
    			taskplayer = 0;
    		}
    		ptimestore.save();
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }

    public void starttask(boolean start) {
    	
    	try {
    		if (start && task == 0) {
    	     	task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,  () -> this.wListener.update(), 0l, 20l); 
    		} else {
    			if (task != 0) {
    				Bukkit.getScheduler().cancelTask(task);
    				task = 0;
    			} 
    		} 
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
    		
    }
        
    private void mcausfuehren(Server serv, String cmd) {

    	new BukkitRunnable() {
    		@Override
    		public void run() {
    			serv.dispatchCommand(serv.getConsoleSender(), cmd);   
    			cancel();
    		}
    		
    	}.runTaskTimer(this, 1, 1);
    }
    
    public PlayerTimeStore getPlayerTimeStore() {
    	
    	return(ptimestore);
    }
}
