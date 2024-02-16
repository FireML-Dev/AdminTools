package uk.firedev.admintools.denizen.cmi.events;

import com.Zrips.CMI.events.CMIPlayerVanishEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CMIPlayerVanishScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // cmi player vanish
    //
    // @Location true
    //
    // @Group Block
    //
    // @Cancellable true
    //
    // @Triggers when a player vanishes
    //
    // @Context
    // <context.location> returns the location of the vanishing player.
    //
    // @Group AdminTools
    //
    // -->

    public CMIPlayerVanishScriptEvent() {
        registerCouldMatcher("cmi player vanish");
    }

    public CMIPlayerVanishEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "location" -> new LocationTag(event.getPlayer().getLocation());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onSpongeAbsorbEvent(CMIPlayerVanishEvent event) {
        this.event = event;
        fire(event);
    }

}
