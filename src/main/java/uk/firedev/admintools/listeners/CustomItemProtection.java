package uk.firedev.admintools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.daisylib.utils.ItemUtils;

public class CustomItemProtection implements Listener {

    @EventHandler
    public void onBurn(FurnaceBurnEvent e) {
        if (!MainConfig.getInstance().preventCustomItemFurnaceFuel()) {
            return;
        }
        if (ItemUtils.isCustomItem(e.getFuel())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent e) {
        if (!MainConfig.getInstance().preventCustomItemFurnaceFuel()) {
            return;
        }
        if (ItemUtils.isCustomItem(e.getSource()) && !ItemUtils.isCustomItem(e.getResult())) {
            e.setCancelled(true);
        }
    }

}