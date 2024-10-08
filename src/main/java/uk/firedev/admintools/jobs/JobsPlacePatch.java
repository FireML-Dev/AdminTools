package uk.firedev.admintools.jobs;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionType;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.daisylib.utils.BlockUtils;

import java.util.List;

public class JobsPlacePatch implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoneyPayment(JobsPrePaymentEvent e) {
        if (!MainConfig.getInstance().isJobsPlaceBreakEnabled()) {
            return;
        }
        if (e.getActionInfo() == null) {
            e.setCancelled(true);
            return;
        }
        if (e.getActionInfo().getType() == ActionType.BREAK && !bypass(e.getBlock().getWorld()) && BlockUtils.isPlayerPlaced(e.getBlock())) {
            if (e.getBlock().getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge()) {
                return;
            }
            e.setCancelled(true);
            return;
        }
        if (e.getActionInfo().getType() == ActionType.PLACE && !bypass(e.getBlock().getWorld()) && BlockUtils.isPlayerBroken(e.getBlock())) {
            if (e.getBlock().getBlockData() instanceof Ageable || e.getBlock().getBlockData() instanceof Sapling) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEXPPayment(JobsExpGainEvent e) {
        if (!MainConfig.getInstance().isJobsPlaceBreakEnabled()) {
            return;
        }
        if (e.getActionInfo() == null) {
            e.setCancelled(true);
            return;
        }
        if (e.getActionInfo().getType() == ActionType.BREAK && BlockUtils.isPlayerPlaced(e.getBlock())) {
            if (e.getBlock().getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge()) {
                return;
            }
            e.setCancelled(true);
            return;
        }
        if (e.getActionInfo().getType() == ActionType.PLACE && BlockUtils.isPlayerBroken(e.getBlock())) {
            if (e.getBlock().getBlockData() instanceof Ageable || e.getBlock().getBlockData() instanceof Sapling) {
                return;
            }
            e.setCancelled(true);
        }
    }

    private boolean bypass(World world) {
        List<String> bypassWorlds = MainConfig.getInstance().getJobsPlaceBreakBypassWorlds();
        return !bypassWorlds.isEmpty() && bypassWorlds.contains(world.getName());
    }

}
