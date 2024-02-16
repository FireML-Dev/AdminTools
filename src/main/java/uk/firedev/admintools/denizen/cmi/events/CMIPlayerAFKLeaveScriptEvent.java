package uk.firedev.admintools.denizen.cmi.events;

import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CMIPlayerAFKLeaveScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // cmi player leaves afk
    //
    // @Cancellable true
    //
    // @Triggers when a player leaves afk mode
    //
    // @Player Always.
    //
    // @Plugin CMI
    //
    // @Group AdminTools
    //
    // -->

    public CMIAfkLeaveEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("cmi player leaves afk");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onCMIPlayerLeavesAFK(CMIAfkLeaveEvent event) {
        this.event = event;
        fire(event);
    }

}
