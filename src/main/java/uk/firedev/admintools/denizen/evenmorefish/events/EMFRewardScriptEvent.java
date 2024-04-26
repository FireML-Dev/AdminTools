package uk.firedev.admintools.denizen.evenmorefish.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.oheers.fish.api.reward.EMFRewardEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EMFRewardScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // emf reward
    //
    // @Cancellable false
    //
    // @Triggers when an emf reward is being given
    //
    // @Context
    // <context.key> returns the reward key as an ElementTag
    // <context.value> returns the reward value as an ElementTag
    // <context.hook_location> returns the hook location as a LocationTag
    // <context.velocity> returns the fish velocity as a LocationTag
    //
    // @Player Always.
    //
    // @Plugin EvenMoreFish
    //
    // @Group AdminTools
    //
    // -->

    public EMFRewardEvent event;

    @Override
    public boolean couldMatch(ScriptEvent.ScriptPath path) {
        return path.eventLower.startsWith("emf reward");
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "key" -> new ElementTag(event.getReward().getKey());
            case "value" -> new ElementTag(event.getReward().getValue());
            case "hook_location" -> new LocationTag(event.getHookLocation());
            case "velocity" -> new LocationTag(event.getVelocity());
            default -> super.getContext(name);
        };
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onEMFFish(EMFRewardEvent event) {
        this.event = event;
        fire(event);
    }

}
