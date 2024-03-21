package uk.firedev.admintools.jobs;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.flags.AbstractFlagTracker;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionType;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * The kill limiter is split between Denizen and Java. This is the Java side (obviously).
 */
public class DenizenKillLimiter implements Listener {

    @EventHandler
    public void onPayment(JobsPrePaymentEvent event) {
        if (event.getActionInfo().getType() == ActionType.KILL && event.getLivingEntity() != null) {
            Entity entity = event.getLivingEntity();
            // check if we should pay for this entity
            AbstractFlagTracker tracker = new PlayerTag(event.getPlayer()).getFlagTracker();
            ObjectTag flagValue = tracker.getFlagValue("killTracker." + entity.getType());
            String victimUUID = String.valueOf(entity.getUniqueId());
            if (flagValue instanceof ListTag listTag && !listTag.contains(victimUUID)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEXPPayment(JobsExpGainEvent event) {
        if (event.getActionInfo().getType() == ActionType.KILL && event.getLivingEntity() != null) {
            Entity entity = event.getLivingEntity();
            // check if we should pay for this entity
            AbstractFlagTracker tracker = new PlayerTag(event.getPlayer()).getFlagTracker();
            ObjectTag flagValue = tracker.getFlagValue("killTracker." + entity.getType());
            String victimUUID = String.valueOf(entity.getUniqueId());
            if (flagValue instanceof ListTag listTag && listTag.contains(victimUUID)) {
                event.setCancelled(true);
            }
        }
    }

}
