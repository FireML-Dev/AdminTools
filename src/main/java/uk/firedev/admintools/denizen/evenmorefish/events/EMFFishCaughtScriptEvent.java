package uk.firedev.admintools.denizen.evenmorefish.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.oheers.fish.api.EMFFishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EMFFishCaughtScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // emf fish caught
    //
    // @Cancellable true
    //
    // @Triggers when an emf fish is caught
    //
    // @Context
    // <context.fish> returns the fish name as an ElementTag
    // <context.rarity> returns the fish rarity as an ElementTag
    //
    // @Player Always.
    //
    // @Plugin EvenMoreFish
    //
    // @Group AdminTools
    //
    // -->

    public EMFFishEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("emf fish caught");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("fish")) {
            return new ElementTag(event.getFish().getName());
        }
        if (name.equals("rarity")) {
            return new ElementTag(event.getFish().getRarity().getValue());
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onEMFFish(EMFFishEvent event) {
        this.event = event;
        fire(event);
    }

}
