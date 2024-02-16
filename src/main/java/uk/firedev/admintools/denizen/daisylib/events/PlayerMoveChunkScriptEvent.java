package uk.firedev.admintools.denizen.daisylib.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ChunkTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.firedev.daisylib.events.PlayerMoveChunkEvent;

public class PlayerMoveChunkScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // lib player moves chunk
    //
    // @Cancellable true
    //
    // @Triggers when a player moves into a different chunk
    //
    // @Context
    // <context.from> returns the old chunk
    // <context.to> returns the new chunk
    //
    // @Player Always.
    //
    // @Plugin DaisyLib
    //
    // @Group AdminTools
    //
    // -->

    public PlayerMoveChunkEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("lib player moves chunk");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("from")) {
            return new ChunkTag(event.getFrom());
        }
        if (name.equals("to")) {
            return new ChunkTag(event.getTo());
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onMoveBlock(PlayerMoveChunkEvent event) {
        this.event = event;
        fire(event);
    }

}
