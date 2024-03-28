package uk.firedev.admintools.reward.evenmorefish;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import net.Zrips.CMILib.Equations.ParseError;
import net.Zrips.CMILib.Equations.Parser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.firedev.admintools.AdminTools;

public class EMFJobsEXPEquationRewardType implements com.oheers.fish.api.reward.RewardType {

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value, Location hookLocation) {
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
        JobProgression prog = jobsPlayer.getJobProgression(job);
        if (prog != null) {
            Parser equation;
            try {
                equation = new Parser(parsedValue[1]);
                equation.setVariable("joblevel", prog.getLevel());
            } catch (ParseError ex) {
                getPlugin().getLogger().warning("Invalid equation specified for EvenMoreFish RewardType " + getIdentifier() + ": " + parsedValue[1]);
                return;
            }
            System.out.println(equation.getValue());
            prog.addExperience(equation.getValue());
            jobsPlayer.setSaved(false);
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "JOBS_EXP_EQUATION";
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
