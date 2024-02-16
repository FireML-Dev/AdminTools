package uk.firedev.admintools.denizen.daisylib.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.events.ScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.firedev.daisylib.events.DaisyLibReloadEvent;

public class DaisyLibReloadScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // daisylib reload
    //
    // @Triggers when DaisyLib is reloaded
    //
    // @Plugin DaisyLib
    //
    // @Group AdminTools
    //
    // -->

    @Override
    public boolean couldMatch(ScriptEvent.ScriptPath path) {
        return path.eventLower.startsWith("daisylib reload");
    }

    @EventHandler
    public void onDaisyLibReload(DaisyLibReloadEvent event) {
        fire(event);
    }

}
