package uk.firedev.admintools.config;

import uk.firedev.admintools.AdminTools;

import java.util.List;

public class MainConfig extends uk.firedev.daisylib.Config {

    private static MainConfig instance = null;

    private MainConfig() {
        super("config.yml", "config.yml", AdminTools.getInstance(), true);
    }

    public static MainConfig getInstance() {
        if (instance == null) {
            instance = new MainConfig();
        }
        return instance;
    }

    /// Denizen Hooks

    public boolean isDenizenDaisyLibHook() {
        return getConfig().getBoolean("hooks.denizen.daisylib");
    }

    public boolean isDenizenEMFHook() {
        return getConfig().getBoolean("hooks.denizen.evenmorefish");
    }

    public boolean isDenizenCarbonChatHook() { return getConfig().getBoolean("hooks.denizen.carbonchat"); }

    /// Jobs Reborn

    // Place Break Patch
    public boolean isJobsPlaceBreakEnabled() {
        return getConfig().getBoolean("hooks.jobs.place-break.enable-patch");
    }

    public List<String> getJobsPlaceBreakBypassWorlds() {
        return getConfig().getStringList("hooks.jobs.place-break.world-bypass");
    }

    /// Custom Items

    public boolean isPreventCustomItemMending() {
        return getConfig().getBoolean("hooks.custom-items.prevent-mending");
    }

    public boolean isPreventCustomItemFurnaceFuel() {
        return getConfig().getBoolean("hooks.custom-items.prevent-furnace-fuel", true);
    }

}
