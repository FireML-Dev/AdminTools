package uk.firedev.admintools.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.utils.ItemUtils;

public class MendingPrevention implements Listener {

    @EventHandler
    public void onMendingApply(InventoryClickEvent e) {
        if (!MainConfig.getInstance().isPreventCustomItemMending()) {
            return;
        }
        if (!(e.getClickedInventory() instanceof AnvilInventory inv) || !(e.getWhoClicked() instanceof Player p)) {
            return;
        }
        ItemStack result = inv.getResult();
        if (result == null || e.getSlot() != 2 || !result.containsEnchantment(Enchantment.MENDING)) {
            return;
        }
        if (ItemUtils.isCustomItem(result)) {
            e.setCancelled(true);
            MessageConfig.getInstance().getMendingPreventionDeniedMessage().sendMessage(p);
        }
    }

    @EventHandler
    public void onMend(PlayerItemMendEvent e) {
        if (!MainConfig.getInstance().isPreventCustomItemMending()) {
            return;
        }
        if (ItemUtils.isCustomItem(e.getItem())) {
            e.setCancelled(true);
        }
    }

}
