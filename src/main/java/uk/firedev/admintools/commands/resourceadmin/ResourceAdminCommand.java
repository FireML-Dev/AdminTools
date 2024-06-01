package uk.firedev.admintools.commands.resourceadmin;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;
import uk.firedev.daisylib.message.component.ComponentMessage;
import uk.firedev.daisylib.message.component.ComponentReplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourceAdminCommand extends CommandAPICommand {

    private static ResourceAdminCommand instance = null;

    private ResourceAdminCommand() {
        super("resourceadmin");
        setPermission(CommandPermission.fromString("admintools.resourceadmin"));
        withShortDescription("Manages the Resource Worlds");
        withFullDescription("Manages the Resource Worlds");
        withSubcommands(getDeleteCommand(), getSetupCommand());
        executes((sender, arguments) -> {
            MessageConfig.getInstance().getResourceAdminUsageMessage().sendMessage(sender);
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
                        MessageConfig.getInstance().getResourceAdminDeleteConfirmMessage().sendMessage(player);
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
                    MessageConfig.getInstance().getResourceAdminSetupCompleteMessage().sendMessage(sender);
                });
    }

    public final MVWorldManager worldManager = AdminTools.getInstance().getMultiverseWorldManager();
    private final List<UUID> deleteConfirmation = new ArrayList<>();

    private void deleteWorlds(CommandSender sender) {
        // Notify the sender
        MessageConfig.getInstance().getResourceAdminDeleteStartingMessage().sendMessage(sender);
        // Delete the Worlds
        this.deleteWorld("Resource", sender);
        this.deleteWorld("Resource_nether", sender);
        this.deleteWorld("Resource_the_end", sender);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
        MessageConfig.getInstance().getResourceAdminDeleteCompleteMessage().sendMessage(sender);
    }

    private void deleteWorld(String worldName, CommandSender sender) {
        World w = Bukkit.getWorld(worldName);
        if (w != null) {
            if (w.getEnderDragonBattle() != null) { w.getEnderDragonBattle().getBossBar().removeAll(); }
            final ComponentMessage message = MessageConfig.getInstance().getResourceAdminDeleteEvacMessage();
            w.getPlayers().forEach(p -> {
                message.sendMessage(p);
                if (p.getRespawnLocation() != null) {
                    p.teleport(p.getRespawnLocation());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi spawn " + p.getName());
                }
            });
            Bukkit.dispatchCommand(sender, "adminwarps purge " + worldName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi resetdbfields Homes -w:" + worldName);
            worldManager.deleteWorld(worldName);
            ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", w.getName());
            MessageConfig.getInstance().getResourceAdminDeleteSuccessMessage().applyReplacer(replacer).sendMessage(sender);
        } else {
            ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", worldName);
            MessageConfig.getInstance().getResourceAdminDeleteNullWorldMessage().applyReplacer(replacer).sendMessage(sender);
        }
    }

    private void createWorlds(CommandSender sender) {
        this.createWorld("Resource", sender, World.Environment.NORMAL);
        this.createWorld("Resource_nether", sender, World.Environment.NETHER);
        this.createWorld("Resource_the_end", sender, World.Environment.THE_END);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
    }

    private void createWorld(String worldName, CommandSender sender, World.Environment env) {
        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            worldManager.addWorld(
                    worldName, // The worldname
                    env, // The overworld environment type.
                    null, // The world seed. Any seed is fine for me, so we just pass null.
                    WorldType.NORMAL, // Nothing special. If you want something like a flat world, change this.
                    true, // This means we want to structures like villages to generator, Change to false if you don't want this.
                    null // Specifies a custom generator. We are not using any, so we just pass null.
            );
            w = Bukkit.getWorld(worldName);
            if (w == null) {
                return;
            }
            w.getWorldBorder().setCenter(0, 0);
            w.getWorldBorder().setSize(16000);
            ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", w.getName());
            AdminTools.getInstance().getComponentLogger().info(
                    MessageConfig.getInstance().getResourceAdminSetupBorderMessage().applyReplacer(replacer).getMessage()
            );
            w.setDifficulty(Difficulty.HARD);
            AdminTools.getInstance().getComponentLogger().info(
                    MessageConfig.getInstance().getResourceAdminSetupDifficultyMessage().applyReplacer(replacer).getMessage()
            );
            if (MainConfig.getInstance().isResourcePreGenerate()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + worldName);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky worldborder");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
            }
            MessageConfig.getInstance().getResourceAdminSetupCreatedMessage().applyReplacer(replacer).sendMessage(sender);
        } else {
            ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", w.getName());
            MessageConfig.getInstance().getResourceAdminSetupExistsMessage().applyReplacer(replacer).sendMessage(sender);
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
