package uk.firedev.admintools.config;

import uk.firedev.admintools.AdminTools;

import java.util.List;

public class MainConfig extends uk.firedev.daisylib.Config {

    private static MainConfig instance = null;

    public boolean isPlaceBreakEnabled() { return getConfig().getBoolean("hooks.jobs.place-break.enable-patch", false); }
    public List<String> getPlaceBreakBypassWorlds() { return getConfig().getStringList("hooks.jobs.place-break.world-bypass"); }
    public boolean isResourcePreGenerate() { return getConfig().getBoolean("resourceadmin.pre-generate", false); }
    public boolean preventCustomItemMending() { return getConfig().getBoolean("hooks.custom-items.prevent-mending", false); }
    public boolean preventCustomItemFurnaceFuel() { return getConfig().getBoolean("hooks.custom-items.prevent-furnace-fuel", true); }
    public boolean preventMapsInNonOwnerPlots() { return getConfig().getBoolean("hooks.plotsquared.prevent-maps-for-non-owners", true); }

    private MainConfig() {
        super("config.yml", AdminTools.getInstance());
        instance = this;
    }

    public static MainConfig getInstance() {
        if (instance == null) {
            instance = new MainConfig();
        }
        return instance;
    }

}
