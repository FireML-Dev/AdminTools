package uk.firedev.admintools.denizen.cmi.events;

import com.Zrips.CMI.events.CMIAfkKickEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CMIPlayerAFKKickScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // cmi player afk kick
    //
    // @Cancellable true
    //
    // @Triggers when a player is kicked from being AFK
    //
    // @Player Always.
    //
    // @Plugin CMI
    //
    // @Group AdminTools
    //
    // -->

    public CMIAfkKickEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("cmi player afk kick");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onCMIPlayerAFKKick(CMIAfkKickEvent event) {
        this.event = event;
        fire(event);
    }

}
