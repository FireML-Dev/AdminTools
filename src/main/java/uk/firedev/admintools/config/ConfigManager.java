package uk.firedev.admintools.config;

import org.bukkit.plugin.java.JavaPlugin;
import uk.firedev.daisylib.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends Config {

    private static ConfigManager instance = null;

    // Jobs Place/Break Patch
    public static boolean PLACE_BREAK_ENABLED = false;
    public static List<String> PLACE_BREAK_BYPASS_WORLDS = new ArrayList<>();

    // ResourceAdmin Command
    public static boolean RESOURCE_PRE_GENERATE = false;

    public ConfigManager(String fileName, JavaPlugin plugin) {
        super(fileName, plugin);
        instance = this;
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    @Override
    public void reload() {
        super.reload();
        PLACE_BREAK_ENABLED = getConfig().getBoolean("jobs.place-break.enable-patch", false);
        RESOURCE_PRE_GENERATE = getConfig().getBoolean("resourceadmin.pre-generate", false);
        PLACE_BREAK_BYPASS_WORLDS = getConfig().getStringList("jobs.place-break.world-bypass");
    }

}
