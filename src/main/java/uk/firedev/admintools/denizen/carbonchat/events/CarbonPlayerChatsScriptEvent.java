package uk.firedev.admintools.denizen.carbonchat.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.draycia.carbon.api.CarbonChatProvider;
import net.draycia.carbon.api.event.events.CarbonChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CarbonPlayerChatsScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // carbon player chats
    //
    // @Cancellable false
    //
    // @Triggers when a player chats with carbon
    //
    // @Context
    // <context.channel> returns the channel name an ElementTag
    // <context.message> returns the message as an ElementTag
    //
    // @Player Always.
    //
    // @Plugin CarbonChat
    //
    // @Group AdminTools
    //
    // -->

    public CarbonPlayerChatsScriptEvent() {
        CarbonChatProvider.carbonChat().eventHandler().subscribe(CarbonChatEvent.class, event -> {
            this.event = event;
            fire();
        });
    }

    public CarbonChatEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("carbon player chats");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("channel")) {
            return new ElementTag(event.chatChannel().key().value());
        }
        if (name.equals("message")) {
            return new ElementTag(LegacyComponentSerializer.legacyAmpersand().serialize(event.message()));
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.sender().uuid()), null);
    }

}
