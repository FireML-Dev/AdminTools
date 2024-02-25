package uk.firedev.admintools.commands.resourceadmin;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import uk.firedev.daisylib.Loggers;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.MessageUtils;
import uk.firedev.admintools.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ResourceAdminCommand implements CommandExecutor, TabCompleter {

    public final MVWorldManager worldManager = AdminTools.getInstance().mvCore.getMVWorldManager();
    private static List<UUID> deleteConfirmation = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            MessageUtils.getInstance().fromConfigList("messages.resourceadmin.usage").forEach(
                    s -> MessageUtils.getInstance().sendPrefixedMessage(sender, s)
            );
            return true;
        }

        switch (args[0]) {
            case "delete" -> {
                if (sender instanceof Player p) {
                    UUID uuid = p.getUniqueId();
                    if (isInDeleteConfirmList(uuid)) {
                        removeFromDeleteConfirmList(uuid);
                        this.deleteWorlds(sender);
                    } else {
                        addToDeleteConfirmList(uuid);
                        MessageUtils.getInstance().sendPrefixedMessageFromConfig(p, "messages.resourceadmin.delete.confirm");
                    }
                } else {
                    this.deleteWorlds(sender);
                }
                return true;
            }
            case "setup" -> {
                this.createWorlds(sender);
                MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.complete");
                return true;
            }
        }
        return false;
    }

    private void deleteWorlds(CommandSender sender) {
        // Notify the sender
        MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.starting");
        // Delete the Worlds
        this.deleteWorld("Resource", sender);
        this.deleteWorld("Resource_nether", sender);
        this.deleteWorld("Resource_the_end", sender);
        if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
        }
        MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.complete");
    }

    private void deleteWorld(String worldname, CommandSender sender) {
        World w = Bukkit.getWorld(worldname);
        if (w != null) {
            if (w.getEnderDragonBattle() != null) { w.getEnderDragonBattle().getBossBar().removeAll(); }
            w.getPlayers().forEach(p -> {
                MessageUtils.getInstance().sendPrefixedMessageFromConfig(p, "messages.resourceadmin.delete.evacmessage");
                if (p.getRespawnLocation() != null) {
                    p.teleport(p.getRespawnLocation());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
                }
            });
            Bukkit.dispatchCommand(sender, "adminwarps purge " + worldname);
            if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi resetdbfields Homes -w:" + worldname);
            }
            worldManager.deleteWorld(worldname);
            MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.worldsuccess",
                    "world", w.getName()
            );
        } else {
            MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.delete.nullworld",
                    "world", worldname
            );
        }
    }

    private void createWorlds(CommandSender sender) {
        this.createWorld("Resource", sender, World.Environment.NORMAL);
        this.createWorld("Resource_nether", sender, World.Environment.NETHER);
        this.createWorld("Resource_the_end", sender, World.Environment.THE_END);
        if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi reload");
        }
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
            w.getWorldBorder().setCenter(0, 0);
            w.getWorldBorder().setSize(16000);
            Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), MessageUtils.getInstance().fromConfig("messages.resourceadmin.setup.setborder",
                    "world", w.getName())
            );
            w.setDifficulty(Difficulty.HARD);
            Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), MessageUtils.getInstance().fromConfig("messages.resourceadmin.setup.setdifficulty",
                    "world", w.getName())
            );
            if (ConfigManager.getInstance().isResourcePreGenerate()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + worldname);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky worldborder");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
            }
            MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.created",
                    "world", w.getName()
            );
        } else {
            MessageUtils.getInstance().sendPrefixedMessageFromConfig(sender, "messages.resourceadmin.setup.exists",
                    "world", w.getName()
            );
        }
    }

    private static boolean isInDeleteConfirmList(UUID uuid) {
        return deleteConfirmation.contains(uuid);
    }

    public static void addToDeleteConfirmList(UUID uuid) {
        if (!isInDeleteConfirmList(uuid)) {
            deleteConfirmation.add(uuid);
            AdminTools.getScheduler().runTaskLater(() -> removeFromDeleteConfirmList(uuid), 100L);
        }
    }

    public static void removeFromDeleteConfirmList(UUID uuid) {
        if (isInDeleteConfirmList(uuid)) {
            deleteConfirmation.remove(uuid);
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if (!(sender instanceof Player) || args.length == 0) {
            return new ArrayList<>();
        }

        List<String> suggest = List.of("setup", "delete");

        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], suggest, completions);
        return completions;
    }

}
