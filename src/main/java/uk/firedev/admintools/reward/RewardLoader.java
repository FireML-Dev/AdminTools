package uk.firedev.admintools.reward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import uk.firedev.admintools.reward.daisylib.denizen.DaisyLibDenizenItemRewardType;
import uk.firedev.admintools.reward.evenmorefish.jobs.EMFJobsEXPEquationRewardType;
import uk.firedev.admintools.reward.evenmorefish.jobs.EMFJobsEXPRewardType;
import uk.firedev.admintools.reward.daisylib.jobs.DaisyLibJobsEXPRewardType;
import uk.firedev.admintools.reward.daisylib.jobs.DaisyLibJobsEXPEquationRewardType;

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
        if (pm.isPluginEnabled("Denizen")) {
            new DaisyLibDenizenItemRewardType().register();
        }
    }

}
