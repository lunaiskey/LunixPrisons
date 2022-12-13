package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.mines.PMineManager;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.mines.inventories.PMineGUI;
import io.github.lunaiskey.lunixprison.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.TimeUtil;
import org.bukkit.Bukkit;
import org.apache.commons.lang3.tuple.Pair;
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
            PMine mine = LunixPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
            if (args.length == 0) {
                p.openInventory(new PMineGUI(p).getInv());
                return true;
            }
            if (args[0].equalsIgnoreCase("reset")) {
                if (p.hasPermission("pyrex.resetcooldown.bypass") || !resetCooldown.containsKey(p.getUniqueId()) || System.currentTimeMillis() >= resetCooldown.get(p.getUniqueId())+300000) {
                    if (mine != null) {
                        p.sendMessage("PMine resetting...");
                        mine.reset();
                        resetCooldown.put(p.getUniqueId(),System.currentTimeMillis());
                    } else {
                        p.sendMessage("PMine not found. Please report this to an admin.");
                    }
                } else {
                    p.sendMessage("Reset is on Cooldown: "+ TimeUtil.countdown(resetCooldown.get(p.getUniqueId())+300000));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("fly")) {
                if (p.getLocation().getWorld().getName().equalsIgnoreCase(PMineWorld.getWorldName())) {
                    if (p.getAllowFlight()) {
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.sendMessage("[MINE] Flight Disabled");
                    } else {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                        p.sendMessage("[MINE] Flight Enabled");
                    }

                }
                return true;
            }
            if (args[0].equalsIgnoreCase("tp")) {
                p.teleport(LunixPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId()).getCenter().add(0.5,1,0.5));
                p.sendMessage("Teleporting to mine...");
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage("PMine Commands:",
                        "| /pmine reset",
                        "| /pmine tp",
                        "| /pmine fly",
                        "| /pmine checkmineblocks");
                if (p.hasPermission("pyrex.debug")) {
                    p.sendMessage("| /pmine debug getposition",
                            "| /pmine debug getgridposition"
                            //"| /pmine debug genbedrock (DOESNT NEED TO RUN.)"
                    );
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("checkmineblocks")) {
                mine.checkMineBlocks();
                p.sendMessage(StringUtil.color("&aChecked your blocks. You have "+mine.getComposition().size()+" Available blocks."));
                return true;
            }
            if (p.hasPermission("pyrex.debug")) {
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("getposition")) {
                            Location l = p.getLocation().clone();
                            p.sendMessage("you are in " + l.getWorld().getName() + " at " + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("getgridposition")) {
                            Location l = p.getLocation().clone();
                            Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                            PMine pMine = LunixPrison.getPlugin().getPmineManager().getPMine(loc.getLeft(), loc.getRight());
                            if (pMine != null && l.getWorld().getName().equalsIgnoreCase("mines")) {
                                p.sendMessage("Owner: " + Bukkit.getOfflinePlayer(pMine.getOwner()).getName());
                            } else {
                                p.sendMessage("Owner: No-One");
                            }
                            p.sendMessage(loc.getLeft() + "," + loc.getRight());
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("genbedrock")) {
                            if (debug) {
                                Location l = p.getLocation().clone();
                                Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                                PMine pMine = LunixPrison.getPlugin().getPmineManager().getPMine(loc.getLeft(), loc.getRight());
                                pMine.genBedrock();
                            }
                            return true;
                        }
                    }
                }
            } else {
                p.sendMessage(Messages.NO_PERMISSION.getText());
                return true;
            }
            p.sendMessage(StringUtil.color("Invalid Arguments."));
            return true;

        }
        return true;
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
            if (sender.hasPermission("pyrex.debug")) {
                completions.add("debug");
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("debug")) {
                if (sender.hasPermission("pyrex.debug")) {
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
