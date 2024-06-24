package uk.firedev.admintools.jobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.flags.AbstractFlagTracker;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.gamingmesh.jobs.container.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DenizenNoPaymentFlag implements Listener {

    @EventHandler
    public void onPayment(JobsPrePaymentEvent event) {
        AbstractFlagTracker flagTracker = null;
        if (event.getActionInfo().getType() == ActionType.BREAK && event.getBlock() != null) {
            flagTracker = new LocationTag(event.getBlock().getLocation()).getFlagTracker();
        } else if (event.getActionInfo().getType() == ActionType.KILL && event.getEntity() != null) {
            flagTracker = new EntityTag(event.getEntity()).getFlagTracker();
        }
        if (flagTracker != null && flagTracker.hasFlag("noJobsPayment")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEXPPayment(JobsExpGainEvent event) {
        AbstractFlagTracker flagTracker = null;
        if (event.getActionInfo().getType() == ActionType.BREAK && event.getBlock() != null) {
            flagTracker = new LocationTag(event.getBlock().getLocation()).getFlagTracker();
        } else if (event.getActionInfo().getType() == ActionType.KILL && event.getEntity() != null) {
            flagTracker = new EntityTag(event.getEntity()).getFlagTracker();
        }
        if (flagTracker != null && flagTracker.hasFlag("noJobsPayment")) {
            event.setCancelled(true);
        }
    }

}
