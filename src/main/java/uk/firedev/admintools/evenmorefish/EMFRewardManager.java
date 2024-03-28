package uk.firedev.admintools.evenmorefish;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EMFRewardManager {

    private static EMFRewardManager instance = null;

    private EMFRewardManager() {}

    public static EMFRewardManager getInstance() {
        if (instance == null) {
            instance = new EMFRewardManager();
        }
        return instance;
    }

    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.isPluginEnabled("Jobs")) {
            new JobsEXPRewardType().register();
        }
    }

}
