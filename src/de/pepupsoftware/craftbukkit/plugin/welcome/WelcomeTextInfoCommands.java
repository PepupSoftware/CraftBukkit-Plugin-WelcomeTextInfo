package de.pepupsoftware.craftbukkit.plugin.welcome;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class WelcomeTextInfoCommands implements TabExecutor {

	private WelcomeTextInfo plugin;
	private WelcomeTextInfoListener wListener;
	
	public WelcomeTextInfoCommands(WelcomeTextInfo wplugin, WelcomeTextInfoListener listener) {

		try {
			this.plugin = wplugin;
			this.wListener = listener;
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	boolean result = false;
    	
    	try {
    		if ( cmd.getName().equalsIgnoreCase("gamertimelist") ) {
    			wListener.SpielerListen();
    			result = true;
    		} else {
    		
    			if ( cmd.getName().equalsIgnoreCase("gametimer") ) {
    				if (args.length == 1) {
    					if ( args[0].equalsIgnoreCase("on") ) {
    						plugin.starttask(true);
    						result = true;
    					}
    					if ( args[0].equalsIgnoreCase("off") ) {
    						plugin.starttask(false);
    						wListener.hiddenBossBar();
    						result = true;
    					}    			
    				} else {
    					result = false;
    				}
        		
    			}
        	}
        	
    	} catch (Exception e) {
        		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
        }

    	return result;
    	
    }

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> parameterliste = null;

		try {
			
			if ( cmd.getName().equalsIgnoreCase("gametimer") ) {
				parameterliste = new ArrayList<String>();
				if (args.length == 1) {
					parameterliste.add("on");
					parameterliste.add("off");
				}
			}
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
		
		return parameterliste;
	}
    	
}
