package uk.firedev.admintools.reward.evenmorefish.jobs;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.firedev.admintools.AdminTools;
import uk.firedev.daisylib.utils.ObjectUtils;

public class EMFJobsEXPRewardType implements com.oheers.fish.api.reward.RewardType {

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value, Location location) {
        String[] parsedValue = value.split(",");
        if (parsedValue.length < 2) {
            getPlugin().getLogger().warning("Invalid format for EvenMoreFish RewardType " + getIdentifier() + ". Valid format: \"JOBS_EXP:Miner,50\"");
            return;
        }
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        Job job = Jobs.getJob(parsedValue[0]);
        if (job == null) {
            getPlugin().getLogger().warning("Invalid job specified for EvenMoreFish RewardType " + getIdentifier() + ": " + parsedValue[0]);
            return;
        }
        if (!jobsPlayer.isInJob(job)) {
            return;
        }
        if (!ObjectUtils.isDouble(parsedValue[1])) {
            getPlugin().getLogger().warning("Invalid number specified for EvenMoreFish RewardType " + getIdentifier() + ": " + parsedValue[1]);
            return;
        }
        double xp = Double.parseDouble(parsedValue[1]);
        JobProgression prog = jobsPlayer.getJobProgression(job);
        if (prog != null) {
            prog.addExperience(xp);
            jobsPlayer.setSaved(false);
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "JOBS_EXP";
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
