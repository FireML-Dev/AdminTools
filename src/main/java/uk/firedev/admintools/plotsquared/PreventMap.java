package uk.firedev.admintools.plotsquared;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.firedev.admintools.MessageUtils;
import uk.firedev.admintools.config.ConfigManager;

public class PreventMap implements Listener {

    @EventHandler
    public void onMapCreated(PlayerInteractEvent e) {
        if (!ConfigManager.getInstance().preventMapsInNonOwnerPlots()) {
            return;
        }
        PlotAPI plotAPI = new PlotAPI();
        Plot plot = plotAPI.wrapPlayer(e.getPlayer().getUniqueId()).getCurrentPlot();

        if (plot != null && e.getItem() != null && e.getItem().getType() == Material.MAP && !validMember(plot, e.getPlayer())) {
            e.setCancelled(true);
            MessageUtils.getInstance().sendMessage(e.getPlayer(), MessageUtils.getInstance().fromConfig("messages.mapartprotection.denymessage"));
        }
    }

    private boolean validMember(Plot plot, Player p) {
        return plot.getMembers().contains(p.getUniqueId()) || plot.getOwner() != null && plot.getOwner().equals(p.getUniqueId()) || p.isOp();
    }

}