package uk.firedev.admintools.reward.daisylib.denizen;

import com.denizenscript.denizen.objects.ItemTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.firedev.admintools.AdminTools;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.utils.ItemUtils;
import uk.firedev.daisylib.utils.ObjectUtils;

import java.util.logging.Level;

public class DaisyLibDenizenItemRewardType implements uk.firedev.daisylib.reward.RewardType {

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value) {
        String[] splitValue = value.split(",");
        ItemTag itemTag = ItemTag.valueOf(splitValue[0], false);
        if (itemTag == null) {
            Loggers.info(getComponentLogger(), "Invalid Denizen Item specified for RewardType " + getIdentifier() + ": " + splitValue[0]);
            return;
        }
        int quantity = 1;
        if (splitValue.length >= 2) {
            if (!ObjectUtils.isInt(splitValue[1])) {
                Loggers.info(getComponentLogger(), "Invalid number specified for RewardType " + getIdentifier() + ": " + splitValue[1]);
                return;
            }
            quantity = Math.max(Integer.parseInt(splitValue[1]), 1);
        }
        ItemStack item = itemTag.getItemStack();
        for (int i = 0; i < quantity; ++i) {
            ItemUtils.giveItem(item, player);
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "DENIZEN_ITEM";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FireML";
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return AdminTools.getInstance();
    }

}
