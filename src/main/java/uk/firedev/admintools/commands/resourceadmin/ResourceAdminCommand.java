package uk.firedev.admintools.commands.resourceadmin;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ResourceAdminCommand extends CommandAPICommand {

    private static ResourceAdminCommand instance = null;

    private ResourceAdminCommand() {
        super("resourceadmin");
        setPermission(CommandPermission.fromString("admintools.resourceadmin"));
        withShortDescription("Manages the Resource Worlds");
        withFullDescription("Manages the Resource Worlds");
        withSubcommands(getDeleteCommand(), getSetupCommand());
        executes((sender, arguments) -> {
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.usage");
        });
    }

    public static ResourceAdminCommand getInstance() {
        if (instance == null) {
            instance = new ResourceAdminCommand();
        }
        return instance;
    }

    private CommandAPICommand getDeleteCommand() {
        return new CommandAPICommand("delete")
                .executesPlayer((player, arguments) -> {
                    UUID uuid = player.getUniqueId();
                    if (isInDeleteConfirmList(uuid)) {
                        removeFromDeleteConfirmList(uuid);
                        this.deleteWorlds(player);
                    } else {
                        addToDeleteConfirmList(uuid);
                        MessageConfig.getInstance().sendPrefixedMessageFromConfig(player, "messages.resourceadmin.delete.confirm");
                    }
                })
                .executes((sender, arguments) -> {
                    this.deleteWorlds(sender);
                });
    }

    private CommandAPICommand getSetupCommand() {
        return new CommandAPICommand("setup")
                .executes((sender, arguments) -> {
                    this.createWorlds(sender);
                    MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.complete");
                });
    }

    public final MVWorldManager worldManager = AdminTools.getInstance().mvCore.getMVWorldManager();
    private final List<UUID> deleteConfirmation = new ArrayList<>();

    private void deleteWorlds(CommandSender sender) {
        // Notify the sender
        MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.starting");
        // Delete the Worlds
        this.deleteWorld("Resource", sender);
        this.deleteWorld("Resource_nether", sender);
        this.deleteWorld("Resource_the_end", sender);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
        MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.complete");
    }

    private void deleteWorld(String worldname, CommandSender sender) {
        World w = Bukkit.getWorld(worldname);
        if (w != null) {
            if (w.getEnderDragonBattle() != null) { w.getEnderDragonBattle().getBossBar().removeAll(); }
            w.getPlayers().forEach(p -> {
                MessageConfig.getInstance().sendPrefixedMessageFromConfig(p, "messages.resourceadmin.delete.evacmessage");
                if (p.getRespawnLocation() != null) {
                    p.teleport(p.getRespawnLocation());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi spawn " + p.getName());
                }
            });
            Bukkit.dispatchCommand(sender, "adminwarps purge " + worldname);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi resetdbfields Homes -w:" + worldname);
            worldManager.deleteWorld(worldname);
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.worldsuccess",
                    "world", w.getName()
            );
        } else {
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.nullworld",
                    "world", worldname
            );
        }
    }

    private void createWorlds(CommandSender sender) {
        this.createWorld("Resource", sender, World.Environment.NORMAL);
        this.createWorld("Resource_nether", sender, World.Environment.NETHER);
        this.createWorld("Resource_the_end", sender, World.Environment.THE_END);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
    }

    private void createWorld(String worldname, CommandSender sender, World.Environment env) {
        World w = Bukkit.getWorld(worldname);
        if (w == null) {
            worldManager.addWorld(
                    worldname, // The worldname
                    env, // The overworld environment type.
                    null, // The world seed. Any seed is fine for me, so we just pass null.
                    WorldType.NORMAL, // Nothing special. If you want something like a flat world, change this.
                    true, // This means we want to structures like villages to generator, Change to false if you don't want this.
                    null // Specifies a custom generator. We are not using any, so we just pass null.
            );
            w = Bukkit.getWorld(worldname);
            if (w == null) {
                return;
            }
            w.getWorldBorder().setCenter(0, 0);
            w.getWorldBorder().setSize(16000);
            Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), MessageConfig.getInstance().fromConfig("messages.resourceadmin.setup.setborder",
                    "world", w.getName())
            );
            w.setDifficulty(Difficulty.HARD);
            Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), MessageConfig.getInstance().fromConfig("messages.resourceadmin.setup.setdifficulty",
                    "world", w.getName())
            );
            if (MainConfig.getInstance().isResourcePreGenerate()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + worldname);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky worldborder");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
            }
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.created",
                    "world", w.getName()
            );
        } else {
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.exists",
                    "world", w.getName()
            );
        }
    }

    private boolean isInDeleteConfirmList(UUID uuid) {
        return deleteConfirmation.contains(uuid);
    }

    public void addToDeleteConfirmList(UUID uuid) {
        if (!isInDeleteConfirmList(uuid)) {
            deleteConfirmation.add(uuid);
            AdminTools.getScheduler().runTaskLater(() -> removeFromDeleteConfirmList(uuid), 100L);
        }
    }

    public void removeFromDeleteConfirmList(UUID uuid) {
        if (isInDeleteConfirmList(uuid)) {
            deleteConfirmation.remove(uuid);
        }
    }

}
