package io.github.lunaiskey.lunixprison.modules.mines.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.PMineGUI;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.TimeUtil;
import org.bukkit.Bukkit;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class CommandPMine implements CommandExecutor, TabCompleter {

    private LunixPrison plugin;
    private Logger log;
    private final boolean debug = false;
    private static Map<UUID,Long> resetCooldown = new HashMap<>();

    public CommandPMine(LunixPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
            if (args.length == 0) {
                p.openInventory(new PMineGUI().getInv(p));
                return true;
            }
            String arg0 = args[0].toLowerCase();
            switch(arg0) {
                case "reset" -> resetCommand(sender, label, args);
                case "fly" -> flyCommand(sender, label, args);
                case "tp" -> teleportCommand(sender, label, args);
                case "help" -> helpCommand(sender, label, args);
                case "checkmineblocks" -> checkMineBlocksCommand(sender, label, args);
                case "debug" -> debugCommand(sender, label, args);
                default -> p.sendMessage(ChatColor.RED+"Invalid Arguments.");
            }
            return true;
        }
        return true;
    }

    public void resetCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (mine == null) {
            p.sendMessage(ChatColor.RED+"PMine not found. Please report this to an admin.");
            return;
        }
        boolean canReset =
                p.hasPermission("lunix.resetcooldown.bypass") ||
                !resetCooldown.containsKey(p.getUniqueId()) ||
                System.currentTimeMillis() >= resetCooldown.get(p.getUniqueId())+(5 * 60 * 1000);
        if (!canReset) {
            p.sendMessage("Reset is on Cooldown: "+ TimeUtil.countdown(resetCooldown.get(p.getUniqueId())+300000));
            return;
        }
        p.sendMessage("PMine resetting...");
        mine.reset();
        resetCooldown.put(p.getUniqueId(),System.currentTimeMillis());
    }
    public void flyCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        //PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (!p.getLocation().getWorld().getName().equalsIgnoreCase(PMineWorld.getWorldName())) {
            p.sendMessage(ChatColor.RED+"You have to be in the Mine world to use this command.");
            return;
        }
        boolean isFlying = p.getAllowFlight();
        String status = isFlying ? ChatColor.RED+"Disabled" : ChatColor.GREEN+"Enabled";
        p.setAllowFlight(!isFlying);
        p.setFlying(!isFlying);
        p.sendMessage("[PMine] Flight "+status+".");
    }
    public void teleportCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        p.teleport(mine.getCenter().add(0.5,1,0.5));
        p.sendMessage(ChatColor.GREEN+"Teleporting to mine...");
    }
    public void helpCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        p.sendMessage(
                ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"PMine Commands:",
                ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine reset",
                ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine tp",
                ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine fly",
                ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine checkmineblocks"
        );
        if (!(p.hasPermission("lunix.debug"))) {
            return;
        }
        p.sendMessage(

        );
    }
    public void checkMineBlocksCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        mine.checkMineBlocks();
        p.sendMessage("&aChecked your blocks, You have "+mine.getComposition().size()+" Available blocks.");
    }

    public void debugCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (!(p.hasPermission("lunix.debug"))) {
            p.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        if (args.length < 2) {
            p.sendMessage(
                    ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"PMine Debug Commands:",
                    ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine debug getposition",
                    ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine debug getgridposition",
                    ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine debug genbedrock",
                    ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/pmine debug sendborder"
            );
            return;
        }
        String arg1 = args[1].toLowerCase();
        switch (arg1) {
            case "getposition" -> {
                Location l = p.getLocation().clone();
                p.sendMessage("you are in " + l.getWorld().getName() + " at " + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
            }
            case "getgridposition" -> {
                Location l = p.getLocation().clone();
                Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                PMine pMine = LunixPrison.getPlugin().getPMineManager().getPMine(loc.getLeft(), loc.getRight());
                if (pMine != null && l.getWorld().getName().equalsIgnoreCase("mines")) {
                    p.sendMessage("Owner: " + Bukkit.getOfflinePlayer(pMine.getOwner()).getName());
                } else {
                    p.sendMessage("Owner: No-One");
                }
                p.sendMessage(loc.getLeft() + "," + loc.getRight());
            }
            case "genbedrock" -> {
                if (debug) {
                    Location l = p.getLocation().clone();
                    Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                    PMine pMine = LunixPrison.getPlugin().getPMineManager().getPMine(loc.getLeft(), loc.getRight());
                    pMine.genBedrock();
                }
            }
            case "sendborder" -> {
                mine.sendBorder(p);
                p.sendMessage(ChatColor.GREEN+"Attempting to send border packets.");
            }
            default -> {
                p.sendMessage(ChatColor.RED+"Invalid Arguments.");
            }
        }
    }



    public static Map<UUID, Long> getResetCooldown() {
        return resetCooldown;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("reset");
            completions.add("tp");
            completions.add("fly");
            completions.add("checkmineblocks");
            if (sender.hasPermission("lunix.debug")) {
                completions.add("debug");
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("debug")) {
                if (sender.hasPermission("lunix.debug")) {
                    completions.add("getposition");
                    completions.add("getgridposition");
                }
            }
        }
        List<String> finalCompletions = new ArrayList<>();
        org.bukkit.util.StringUtil.copyPartialMatches(args[args.length-1],completions,finalCompletions);
        return finalCompletions;
    }
}
