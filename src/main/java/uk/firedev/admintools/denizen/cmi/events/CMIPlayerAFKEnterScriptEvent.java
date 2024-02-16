package uk.firedev.admintools.denizen.cmi.events;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CMIPlayerAFKEnterScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // cmi player enters afk
    //
    // @Cancellable true
    //
    // @Triggers when a player enters afk mode
    //
    // @Player Always.
    //
    // @Plugin CMI
    //
    // @Group AdminTools
    //
    // -->

    public CMIAfkEnterEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("cmi player enters afk");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onCMIPlayerEntersAFK(CMIAfkEnterEvent event) {
        this.event = event;
        fire(event);
    }

}
