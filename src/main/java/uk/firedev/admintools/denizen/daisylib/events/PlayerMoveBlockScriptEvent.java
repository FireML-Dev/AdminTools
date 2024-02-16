package uk.firedev.admintools.denizen.daisylib.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.firedev.daisylib.events.PlayerMoveBlockEvent;

public class PlayerMoveBlockScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // lib player moves block
    //
    // @Cancellable true
    //
    // @Triggers when a player's x or z coordinate changes
    //
    // @Context
    // <context.from> returns the old location
    // <context.to> returns the new location
    //
    // @Player Always.
    //
    // @Plugin DaisyLib
    //
    // @Group AdminTools
    //
    // -->

    public PlayerMoveBlockEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("lib player moves block");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("from")) {
            return new LocationTag(event.getFrom());
        }
        if (name.equals("to")) {
            return new LocationTag(event.getTo());
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onMoveBlock(PlayerMoveBlockEvent event) {
        this.event = event;
        fire(event);
    }

}
