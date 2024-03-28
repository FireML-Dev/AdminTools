package uk.firedev.admintools.reward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import uk.firedev.admintools.reward.daisylib.DaisyLibJobsEXPEquationRewardType;
import uk.firedev.admintools.reward.daisylib.DaisyLibJobsEXPRewardType;
import uk.firedev.admintools.reward.evenmorefish.EMFJobsEXPEquationRewardType;
import uk.firedev.admintools.reward.evenmorefish.EMFJobsEXPRewardType;

public class RewardLoader {

    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.isPluginEnabled("Jobs")) {
            new DaisyLibJobsEXPRewardType().register();
            new DaisyLibJobsEXPEquationRewardType().register();
            if (pm.isPluginEnabled("EvenMoreFish")) {
                new EMFJobsEXPRewardType().register();
                new EMFJobsEXPEquationRewardType().register();
            }
        }
    }

}
