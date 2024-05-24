package uk.firedev.admintools.denizen.evenmorefish.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.oheers.fish.api.EMFCompetitionEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EMFCompetitionEndScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // emf competition ends
    //
    // @Cancellable false
    //
    // @Triggers when an emf competition ends
    //
    // @Context
    // <context.name> returns the competition name as an ElementTag
    // <context.type> returns the competition type as an ElementTag
    //
    // @Plugin EvenMoreFish
    //
    // @Group AdminTools
    //
    // -->

    public EMFCompetitionEndEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("emf competition ends");
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "name" -> new ElementTag(event.getCompetition().getCompetitionName());
            case "type" -> new ElementTag(event.getCompetition().getCompetitionType());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onEMFCompEnd(EMFCompetitionEndEvent event) {
        this.event = event;
        fire(event);
    }

}
