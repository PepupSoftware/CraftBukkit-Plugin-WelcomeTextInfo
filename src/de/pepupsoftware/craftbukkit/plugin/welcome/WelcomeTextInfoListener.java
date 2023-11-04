package de.pepupsoftware.craftbukkit.plugin.welcome;

import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class WelcomeTextInfoListener implements Listener {

    private WelcomeTextInfo wplugin;
    private PlayerTimeStore ptimestore;
    private Map < String, LocalDateTime > spielerliste;
    private Map < String, BossBar> barliste;
    
    public WelcomeTextInfoListener(final WelcomeTextInfo plugin) {
    	
    	try {
    		plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    		this.wplugin = plugin;
    		spielerliste = new HashMap <String, LocalDateTime>();
    		barliste = new HashMap <String, BossBar>();
    		ptimestore = plugin.getPlayerTimeStore();
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        
    	try {
    		event.setJoinMessage("");
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
    		
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
    	
    	try {	
    		event.setQuitMessage("");
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
    		
    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
   
		try {
			
			final Player p = event.getPlayer();
			BossBar bar = Bukkit.createBossBar("", BarColor.WHITE,BarStyle.SEGMENTED_6  );
			bar.setVisible(false);
			bar.setProgress(0.0D);
			ptimestore.updatePlayer(p, 0l);
			
			p.sendMessage(ChatColor.YELLOW + "Willkommen " + p.getName() + "!");
			p.sendMessage(" ");
			this.wplugin.getServer().broadcastMessage(ChatColor.YELLOW + "Zur Zeit sind folgende Spieler angemeldet:");
			final StringBuilder spieler = new StringBuilder();
			for (final Player ga : this.wplugin.getServer().getOnlinePlayers()) {
				if (spieler.toString().isEmpty()) {
					spieler.append(ga.getName());
				}
				else {
					spieler.append(", " + ga.getName());
				}
			}
			this.wplugin.getServer().broadcastMessage(ChatColor.YELLOW + spieler.toString());
			this.wplugin.getServer().broadcastMessage(ChatColor.WHITE + "Der Benutzer " + p.getName() + " hat sich angemeldet.");
        
			bar.addPlayer(p);
			spielerliste.put(p.getName(), LocalDateTime.now());
			barliste.put(p.getName(), bar);
			
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title \"Willkommen " + p.getName() + "!\"");
			
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

   }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        
    	try {
    		
    		final Player p = event.getPlayer();
    		long seconds = ChronoUnit.SECONDS.between(spielerliste.get(p.getName()), LocalDateTime.now());
    		
    		ptimestore.updatePlayer(p, seconds);
    		this.wplugin.getServer().broadcastMessage(ChatColor.WHITE + "Der Benutzer " + p.getName() + " hat die Welt verlassen.");
    		
    		spielerliste.remove(p.getName());
    		barliste.remove(p.getName());
    		
    		ptimestore.save();
    		
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }
	
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak (BlockBreakEvent event) {
    	
    	try {
    		final Player p = event.getPlayer();
    		final Block b = event.getBlock();
    		switch(b.getType()) {
    			case DIRT, GRASS, GRASS_BLOCK:
    				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players add " + p.getName() + " Gesamt 1");
    				break;
    			case DIAMOND:
    				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players add " + p.getName() + " Gesamt 1000");
    				break;
    			default:
    				final Material m = b.getType();
    				System.out.println("Blocktype = " + m.toString());
    				break;
    		}
    		
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }
    
    public void SpielerListen() {
    	
    	try {
    		for (final Player ga : this.wplugin.getServer().getOnlinePlayers()) {
    			this.wplugin.getServer().broadcastMessage(ChatColor.WHITE + "Spieler " + ga.getName() + " --> Spielzeit " + getspielzeit(spielerliste.get(ga.getName()), LocalDateTime.now(), ptimestore.getPlayTimeSecond(ga)));
    		}
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }

    public void update() {
	
    	try {
    		for (final Player ga : this.wplugin.getServer().getOnlinePlayers()) {
    			updateBossBar(ga, getspielzeit(spielerliste.get(ga.getName()), LocalDateTime.now(), ptimestore.getPlayTimeSecond(ga)), barliste.get(ga.getName()));
    		}
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }

    public void player_display_update() {
	
		String pname;
		
    	try {
    		for (final Player ga : this.wplugin.getServer().getOnlinePlayers()) {
    			pname = ga.getName() + " ".repeat(20);
    			ga.setPlayerListName(pname.substring(0, 20) + getspielzeit(spielerliste.get(ga	.getName()), LocalDateTime.now(), ptimestore.getPlayTimeSecond(ga)));
    		}
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }

    public void hiddenBossBar() {
    	
    	try {
    		for (final Player ga : this.wplugin.getServer().getOnlinePlayers()) {
    			barliste.get(ga.getName()).setVisible(false);
    		}
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}
    		
    }
    
    private void updateBossBar(final Player player, final String message, BossBar bar) {

    	try {
    		double progress = bar.getProgress();
    		progress = progress + 0.01D;
   	   		if (progress >= 1.00D) {
   	   			progress = 0.0D;
   	   		}
   	   		bar.setVisible(true);	
   	   		bar.setProgress(progress);
   	   		bar.setTitle(message);
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    	}

    }
            
    private String getspielzeit( LocalDateTime startzeit, LocalDateTime endezeit, long storeseconds) {

    	try {
    		
    		long seconds = ChronoUnit.SECONDS.between(startzeit, endezeit) + storeseconds;
    		long hours = seconds / 3600;
    		seconds -= (hours * 3600);
    		long minutes = seconds / 60;
    		seconds -= (minutes * 60);
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
    	
    	} catch (Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());
    		return null;
    	}
    
    }
    
}
