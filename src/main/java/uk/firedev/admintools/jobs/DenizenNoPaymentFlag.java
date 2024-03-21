package uk.firedev.admintools.jobs;

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
        if (event.getActionInfo().getType() == ActionType.BREAK && event.getBlock() != null) {
            // check if we should pay for this location
            AbstractFlagTracker tracker = new LocationTag(event.getBlock().getLocation()).getFlagTracker();
            if (tracker.hasFlag("noJobsPayment")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEXPPayment(JobsExpGainEvent event) {
        if (event.getActionInfo().getType() == ActionType.BREAK && event.getBlock() != null) {
            // check if we should pay for this location
            AbstractFlagTracker tracker = new LocationTag(event.getBlock().getLocation()).getFlagTracker();
            if (tracker.hasFlag("noJobsPayment")) {
                System.out.println("Had noJobsPayment Flag");
                event.setCancelled(true);
            }
        }
    }

}
