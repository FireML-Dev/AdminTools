package uk.firedev.admintools.config;

import org.bukkit.plugin.java.JavaPlugin;
import uk.firedev.daisylib.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends Config {

    private static ConfigManager instance = null;

    public boolean isPlaceBreakEnabled() { return getConfig().getBoolean("hooks.jobs.place-break.enable-patch", false); }
    public List<String> getPlaceBreakBypassWorlds() { return getConfig().getStringList("hooks.jobs.place-break.world-bypass"); }
    public boolean isResourcePreGenerate() { return getConfig().getBoolean("resourceadmin.pre-generate", false); }
    public boolean preventCustomItemMending() { return getConfig().getBoolean("hooks.custom-items.prevent-mending", false); }
    public boolean preventCustomItemFurnaceFuel() { return getConfig().getBoolean("hooks.custom-items.prevent-furnace-fuel", true); }
    public boolean preventMapsInNonOwnerPlots() { return getConfig().getBoolean("hooks.plotsquared.prevent-maps-for-non-owners", true); }

    public ConfigManager(String fileName, JavaPlugin plugin) {
        super(fileName, plugin);
        instance = this;
    }

    public static ConfigManager getInstance() {
        return instance;
    }

}
