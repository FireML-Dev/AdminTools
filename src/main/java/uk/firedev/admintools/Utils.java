package uk.firedev.admintools;

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Utils {

    public static boolean isCustomItem(@NotNull ItemStack item) {
        return Bukkit.getPluginManager().isPluginEnabled("Denizen") && ItemScriptHelper.isItemscript(item);
    }

}
