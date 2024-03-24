package uk.firedev.admintools.commands.resourceadmin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ResourceAdminListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ResourceAdminCommand.getInstance().removeFromDeleteConfirmList(p.getUniqueId());
    }

}