package uk.firedev.admintools.denizen.huskchat.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.oheers.fish.api.EMFFishEvent;
import net.william278.huskchat.bukkit.event.ChatMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HuskChatPlayerChatsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // huskchat player chats
    //
    // @Cancellable true
    //
    // @Triggers when a player chats with huskchat
    //
    // @Context
    // <context.channel> returns the channel name an ElementTag
    // <context.message> returns the message as an ElementTag
    //
    // @Player Always.
    //
    // @Plugin HuskChat
    //
    // @Group AdminTools
    //
    // -->

    public ChatMessageEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("huskchat player chats");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("channel")) {
            return new ElementTag(event.getChannelId());
        }
        if (name.equals("message")) {
            return new ElementTag(event.getMessage());
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getSender().getUuid()), null);
    }

    @EventHandler
    public void onHuskChatChats(ChatMessageEvent event) {
        this.event = event;
        fire(event);
    }

}
