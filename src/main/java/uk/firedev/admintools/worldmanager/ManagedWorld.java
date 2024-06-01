package uk.firedev.admintools.worldmanager;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.sun.jdi.PrimitiveValue;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang3.builder.Diff;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.message.component.ComponentMessage;
import uk.firedev.daisylib.message.component.ComponentReplacer;
import uk.firedev.daisylib.message.string.StringReplacer;

import java.util.ArrayList;
import java.util.List;

public class ManagedWorld {

    private final @NotNull String name;
    private final boolean preGenerate;
    private final int borderSize;
    private @NotNull Difficulty difficulty;
    private @NotNull World.Environment environment;
    private final @NotNull List<String> preCreationCommands;
    private final @NotNull List<String> postCreationCommands;
    private final @NotNull List<String> preDeletionCommands;
    private final @NotNull List<String> postDeletionCommands;

    public ManagedWorld(@NotNull ConfigurationSection section) {
        this.name = section.getName();
        this.preGenerate = section.getBoolean("pre-generate");
        this.borderSize = section.getInt("border-size", 0);
        try {
            this.difficulty = Difficulty.valueOf(section.getString("difficulty"));
        } catch (IllegalArgumentException ex) {
            Loggers.warn(AdminTools.getInstance().getComponentLogger(), "Invalid difficulty for world " + name + ". Defaulting to HARD");
            this.difficulty = Difficulty.HARD;
        }
        try {
            this.environment = World.Environment.valueOf(section.getString("environment"));
        } catch (IllegalArgumentException ex) {
            Loggers.warn(AdminTools.getInstance().getComponentLogger(), "Invalid environment for world " + name + ". Defaulting to NORMAL");
            this.environment = World.Environment.NORMAL;
        }

        StringReplacer replacer = new StringReplacer().addReplacement("world", name);

        // Pre Creation Commands
        List<String> preCreation = new ArrayList<>();
        preCreation.addAll(WorldManagerConfig.getInstance().getGlobalPreCreationCommands());
        preCreation.addAll(section.getStringList("creation-commands.pre-commands"));
        this.preCreationCommands = preCreation.stream().map(replacer::replace).toList();

        // Post Creation Commands
        List<String> postCreation = new ArrayList<>();
        postCreation.addAll(WorldManagerConfig.getInstance().getGlobalPostCreationCommands());
        postCreation.addAll(section.getStringList("creation-commands.post-commands"));
        this.postCreationCommands = postCreation.stream().map(replacer::replace).toList();

        // Pre Deletion Commands
        List<String> preDeletion = new ArrayList<>();
        preDeletion.addAll(WorldManagerConfig.getInstance().getGlobalPreDeletionCommands());
        preDeletion.addAll(section.getStringList("deletion-commands.pre-commands"));
        this.preDeletionCommands = preDeletion.stream().map(replacer::replace).toList();

        // Post Deletion Commands
        List<String> postDeletion = new ArrayList<>();
        postDeletion.addAll(WorldManagerConfig.getInstance().getGlobalPostDeletionCommands());
        postDeletion.addAll(section.getStringList("deletion-commands.post-commands"));
        this.postDeletionCommands = postDeletion.stream().map(replacer::replace).toList();
    }

    public @NotNull String getName() {
        return this.name;
    }

    public boolean isPreGenerate() {
        return this.preGenerate;
    }

    public int getBorderSize() {
        return this.borderSize;
    }

    public @NotNull Difficulty getDifficulty() {
        return this.difficulty;
    }

    public @NotNull World.Environment getEnvironment() {
        return this.environment;
    }

    public @NotNull List<String> getPreCreationCommands() {
        return this.preCreationCommands;
    }

    public @NotNull List<String> getPostCreationCommands() {
        return this.postCreationCommands;
    }

    public @NotNull List<String> getPreDeletionCommands() {
        return this.preDeletionCommands;
    }

    public @NotNull List<String> getPostDeletionCommands() {
        return this.postDeletionCommands;
    }

    public @Nullable World getWorld() {
        return Bukkit.getWorld(getName());
    }

    public boolean deleteWorld(@Nullable Audience audience) {
        MVWorldManager worldManager = AdminTools.getInstance().getMultiverseWorldManager();
        if (worldManager == null) {
            Loggers.warn(AdminTools.getInstance().getComponentLogger(), "Attempted to delete a world without Multiverse installed!");
            return false;
        }
        World world = getWorld();
        if (world != null) {
            if (world.getEnderDragonBattle() != null) { world.getEnderDragonBattle().getBossBar().removeAll(); }
            final ComponentMessage message = MessageConfig.getInstance().getResourceAdminDeleteEvacMessage();
            final Location evacLocation = WorldManagerConfig.getInstance().getEvacuationLocation();
            world.getPlayers().forEach(p -> {
                Location respawnLoc = p.getRespawnLocation();
                message.sendMessage(p);
                if (respawnLoc != null) {
                    p.teleport(respawnLoc);
                } else if (evacLocation != null) {
                    p.teleport(evacLocation);
                } else {
                    p.kick(WorldManagerConfig.getInstance().getEvacKickMessage().getMessage());
                }
            });
            getPreDeletionCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            worldManager.deleteWorld(world.getName());
            getPostDeletionCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            if (audience != null) {
                ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", world.getName());
                MessageConfig.getInstance().getResourceAdminDeleteSuccessMessage().applyReplacer(replacer).sendMessage(audience);
            }
            return true;
        } else {
            if (audience != null) {
                ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", this.name);
                MessageConfig.getInstance().getResourceAdminDeleteNullWorldMessage().applyReplacer(replacer).sendMessage(audience);
            }
            return false;
        }
    }

    public boolean createWorld(@Nullable Audience audience) {
        MVWorldManager worldManager = AdminTools.getInstance().getMultiverseWorldManager();
        if (worldManager == null) {
            Loggers.warn(AdminTools.getInstance().getComponentLogger(), "Attempted to create a world without Multiverse installed!");
            return false;
        }
        World world = getWorld();
        if (world == null) {
            worldManager.addWorld(
                    getName(), // The world name
                    getEnvironment(), // The world environment type.
                    null, // The world seed. Any seed is fine for me, so we just pass null.
                    WorldType.NORMAL, // Nothing special. If you want something like a flat world, change this.
                    true, // This means we want to structures like villages to generator, Change to false if you don't want this.
                    null // Specifies a custom generator. We are not using any, so we just pass null.
            );
            world = getWorld();
            if (world == null) {
                return false;
            }
            if (getBorderSize() > 1) {
                world.getWorldBorder().setCenter(0, 0);
                world.getWorldBorder().setSize(getBorderSize());
            }
            world.setDifficulty(getDifficulty());
            if (isPreGenerate()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + world.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky worldborder");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
            }
            if (audience != null) {
                ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", world.getName());
                MessageConfig.getInstance().getResourceAdminSetupCreatedMessage().applyReplacer(replacer).sendMessage(audience);
            }
            return true;
        } else {
            if (audience != null) {
                ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", world.getName());
                MessageConfig.getInstance().getResourceAdminSetupExistsMessage().applyReplacer(replacer).sendMessage(audience);
            }
            return false;
        }
    }

}
