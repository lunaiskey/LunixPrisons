package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.gangs.Gang;
import io.github.lunaiskey.lunixprison.gangs.GangManager;
import io.github.lunaiskey.lunixprison.gangs.GangMember;
import io.github.lunaiskey.lunixprison.gangs.GangRankType;
import io.github.lunaiskey.lunixprison.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class CommandGang implements CommandExecutor {

    private LunixPrison plugin;
    private Logger log;

    private static final String ALREADY_IN_GANG = StringUtil.color("&cYou're already in a gang.");
    private static final String NOT_IN_GANG = StringUtil.color("&cYou have to be in a gang to use this command.");
    private static final String NOT_OWNER = StringUtil.color("&cYou have to be the Gang Owner to use this command.");
    private static final String NOT_MOD = StringUtil.color("&cYou have to be a Gang Mod or higher to use this command.");
    private static final String INVALID_NAME_SIZE = StringUtil.color("&cGang name has to be between 3 and 15 characters.");
    private static final String NOT_ALPHANUMERIC = StringUtil.color("&cGang name has to be alphanumeric.");
    private static final String GANG_ALREADY_EXISTS = StringUtil.color("&cA Gang already exists with that name.");
    private static final String NOT_IMPLEMENTED = StringUtil.color("&cThis hasn't been implemented yet.");

    //private final boolean debug = false; //Unused rn.

    public CommandGang(LunixPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GangManager gangManager = plugin.getGangManager();
        PlayerManager playerManager = plugin.getPlayerManager();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID playerUUID = p.getUniqueId();
            Gang gang = gangManager.getGang(playerUUID);
            if (args.length == 0) {
                p.sendMessage(StringUtil.color(
                        "&3&lGang Commands:",
                        "&3&l| &f/gang create <name> &7Create a new gang",
                        "&3&l| &f/gang rename <name> &7Rename your gang",
                        "&3&l| &f/gang info [name] &7Get info on a gang",
                        "&3&l| &f/gang join <name> &7Join a gang",
                        "&3&l| &f/gang leave &7Leave current gang",
                        "&3&l| &f/gang invite <player> &7Invite player to gang",
                        "&3&l| &f/gang kick <player> &7Kick player from gagn",
                        "&3&l| &f/gang transfer <name> &7Transfer ownership to a gang member",
                        "&3&l| &f/gang disband &7Delete your gang"
                ));
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (!gangManager.isInGang(playerUUID)) {
                    if (args.length >= 2) {
                        String name = args[1];
                        if (isValidLength(name)) {
                            if (isAlphaNumeric(name)) {
                                if (!gangManager.gangExists(name)) {
                                    gangManager.createGang(playerUUID,name);
                                    p.sendMessage(StringUtil.color("&aYou have created the gang &f"+name+"&a."));
                                } else {
                                    p.sendMessage(GANG_ALREADY_EXISTS);
                                }
                            } else {
                                p.sendMessage(NOT_ALPHANUMERIC);
                            }
                        } else {
                            p.sendMessage(INVALID_NAME_SIZE);
                        }
                    } else {
                        p.sendMessage(StringUtil.color("&cUsage: /gang create <name>"));
                    }
                } else {
                    p.sendMessage(ALREADY_IN_GANG);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("rename")) {
                if (gangManager.isInGang(playerUUID)) {
                    GangRankType rank = gang.getMembers().get(playerUUID).getType();
                    String newName = args[1];
                    if (rank == GangRankType.OWNER) {
                        if (isValidLength(newName)) {
                            if (isAlphaNumeric(newName)) {
                                if (!gangManager.gangExists(newName)) {
                                    gang.setName(newName);
                                    p.sendMessage(StringUtil.color("&aYou have renamed the gang to &f"+newName+"&a."));
                                } else {
                                    if (gang.getName().equalsIgnoreCase(newName)) {
                                        gang.setName(newName);
                                        p.sendMessage(StringUtil.color("&aYou have renamed the gang to &f"+newName+"&a."));
                                    } else {
                                        p.sendMessage(GANG_ALREADY_EXISTS);
                                    }
                                }
                            } else {
                                p.sendMessage(NOT_ALPHANUMERIC);
                            }
                        } else {
                            p.sendMessage(INVALID_NAME_SIZE);
                        }
                    } else {
                        p.sendMessage(NOT_OWNER);
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("transfer")) {
                if (gang != null) {
                    if (gang.getOwner().equals(playerUUID)) {
                        if (args.length >= 2) {
                            String otherPlayerName = args[1];
                            Player otherPlayer = Bukkit.getPlayer(args[1]);
                            UUID otherPlayerUUID = otherPlayer != null ? otherPlayer.getUniqueId() : LunixPrison.getPlugin().getPlayerManager().getPlayerUUID(otherPlayerName);
                            if (otherPlayerUUID != null) {
                                if (otherPlayer != null) {
                                    if (gang.getMembers().containsKey(otherPlayerUUID)) {
                                        gang.setOwner(otherPlayerUUID);
                                        p.sendMessage(StringUtil.color("&aTransferred Gang to " + otherPlayer.getName() + "."));
                                        otherPlayer.sendMessage(StringUtil.color("&aGang " + gang.getName() + " has been transferred to you."));
                                    } else {
                                        p.sendMessage(StringUtil.color("&cPlayer \"" + otherPlayer.getName() + "\" isn't in your Gang."));
                                    }
                                } else {
                                    p.sendMessage(StringUtil.color("&cPlayer \"" + otherPlayerName + "\" has to be online to transfer to them."));
                                }
                            } else {
                                p.sendMessage(StringUtil.color("&cPlayer \"" + otherPlayerName + "\" has to be online to transfer to them."));
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cUsage: /gang transfer <player>"));
                        }
                    } else {
                        p.sendMessage(NOT_OWNER);
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                //p.sendMessage(NOT_IMPLEMENTED);
                return true;
            }
            if (args[0].equalsIgnoreCase("info")) {
                boolean useOtherGang = false;
                if (args.length >= 2) {
                    String otherGangName = args[1];
                    gang = gangManager.getGang(otherGangName);
                    useOtherGang = true;
                }
                if (gang != null) {
                    List<StringBuilder> strings = new ArrayList<>(List.of(new StringBuilder("&f"),new StringBuilder("&f"),new StringBuilder("&f")));
                    for (GangMember member : gang.getMembers().values()) {
                        String status = Bukkit.getPlayer(member.getPlayerUUID()) != null ? " &a●&f" : " &c●&f";
                        switch (member.getType()) {
                            case OWNER -> strings.get(0).append(member.getName()).append(status).append(", ");
                            case MOD -> strings.get(1).append(member.getName()).append(status).append(", ");
                            case MEMBER -> strings.get(2).append(member.getName()).append(status).append(", ");
                        }
                    }
                    for (StringBuilder builder : strings) {
                        int len = builder.length();
                        if (len >= 2) {
                            builder.delete(len-2,len);
                        }
                    }
                    p.sendMessage(
                            StringUtil.color(
                                    "&a&m                                                  &r",
                                    " &7» &6Gang Name: &f"+ gang.getName(),
                                    " &7» &6Trophies: &f"+gang.getTrophies(),
                                    "",
                                    " &7» &eOwner: "+strings.get(0).toString(),
                                    " &7» &eMods: "+(strings.get(1).length() > 0 ? strings.get(1).toString() : "&f[]"),
                                    " &7» &eMembers: "+(strings.get(2).length() > 0 ? strings.get(2).toString() : "&f[]"),
                                    "&a&m                                                  &r")
                    );
                } else {
                    if (useOtherGang) {
                        p.sendMessage(StringUtil.color("&cGang \""+args[1]+"\" doesn't exist."));
                    } else {
                        p.sendMessage(NOT_IN_GANG);
                    }

                }
                //p.sendMessage(NOT_IMPLEMENTED);
                return true;
            }
            if (args[0].equalsIgnoreCase("disband")) {
                if (gang != null) {
                    if (gang.getOwner().equals(playerUUID)) {
                        gangManager.removeGang(gang.getUUID());
                        // removeGang already sends a message.
                    } else {
                        p.sendMessage(NOT_OWNER);
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                //p.sendMessage(NOT_IMPLEMENTED);
                return true;
            }
            if (args[0].equalsIgnoreCase("invite")) {
                if (gang != null) {
                    GangRankType rank = gang.getMembers().get(playerUUID).getType();
                    switch (rank) {
                        case OWNER,MOD -> {
                            if (args.length >= 2) {
                                Player otherPlayer = Bukkit.getPlayer(args[1]);
                                if (otherPlayer != null) {
                                    if (!gangManager.isInGang(otherPlayer.getUniqueId())) {
                                        gang.getPendingInvites().add(otherPlayer.getUniqueId());
                                        for (UUID memberUUID : gang.getMembers().keySet()) {
                                            Player member = Bukkit.getPlayer(memberUUID);
                                            if (member != null) {
                                                member.sendMessage(StringUtil.color("&f"+p.getName()+"&a has been invited your gang."));
                                            }
                                        }
                                        otherPlayer.sendMessage(
                                                "&aYou have been invited to "+gang.getName()+".",
                                                "&aTo join, run /gang join "+gang.getName()+"."
                                        );
                                    } else {
                                        p.sendMessage(StringUtil.color("&cYou can't invite "+otherPlayer.getName()+" because they're already in a gang."));
                                    }
                                } else {
                                    p.sendMessage(StringUtil.color("&cPlayer \""+args[1]+"\" isn't online."));
                                }
                            } else {
                                p.sendMessage(StringUtil.color("&cUsage: /gang invite <player>"));
                            }

                        }
                        default -> p.sendMessage(NOT_MOD);
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                //p.sendMessage(NOT_IMPLEMENTED);
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (gang == null) {
                    if (args.length >= 2) {
                        String rawGangName = args[1];
                        Gang otherGang = gangManager.getGang(rawGangName);
                        if (otherGang != null) {
                            if (otherGang.getPendingInvites().contains(playerUUID)) {
                                p.sendMessage(StringUtil.color("&aYou have joined &f"+otherGang.getName()+"&a."));
                                for (UUID memberUUID : otherGang.getMembers().keySet()) {
                                    Player member = Bukkit.getPlayer(memberUUID);
                                    if (member != null) {
                                        member.sendMessage(StringUtil.color("&f"+p.getName()+"&a has joined your gang."));
                                    }
                                }
                                otherGang.addMember(playerUUID);
                            } else {
                                p.sendMessage(StringUtil.color("&c"+otherGang.getName()+" hasn't invited you to their gang."));
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cGang \""+rawGangName+"\" doesn't exist."));
                        }
                    } else {
                        p.sendMessage(StringUtil.color("&cUsage: /gang join <name>"));
                    }
                } else {
                    p.sendMessage(ALREADY_IN_GANG);
                }
                //p.sendMessage(NOT_IMPLEMENTED);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (gang != null) {
                    if (gang.getOwner().equals(playerUUID)) {
                        p.sendMessage(StringUtil.color("&cPlease either disband your gang or transfer it to another player."));
                    } else {
                        gang.removeMember(playerUUID);
                        p.sendMessage(StringUtil.color("&aYou have left gang &f"+gang.getName()+"&a."));
                        for (UUID member : gang.getMembers().keySet()) {
                            Player onlineMember = Bukkit.getPlayer(member);
                            if (onlineMember != null) {
                                onlineMember.sendMessage(StringUtil.color("&aPlayer &f"+p.getName()+"&a has left your gang."));
                            }
                        }
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("kick")) {
                if (gang != null) {
                    GangRankType rank = gang.getMembers().get(playerUUID).getType();
                    switch (rank) {
                        case OWNER,MOD -> {
                            if (args.length >= 2) {
                                String otherPlayerName = args[1];
                                Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
                                UUID otherPlayerUUID = otherPlayer != null ? otherPlayer.getUniqueId() : LunixPrison.getPlugin().getPlayerManager().getPlayerUUID(otherPlayerName);
                                if (otherPlayerUUID == null) {
                                    p.sendMessage(StringUtil.color("&cSomething is off and we couldn't find player..."));
                                    return true;
                                }
                                String otherPlayerNameFormatted = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayerUUID).getName();
                                GangRankType otherPlayerRank = gang.getMembers().get(otherPlayerUUID).getType();
                                if (otherPlayerRank.getValue() < rank.getValue()) {
                                    gang.removeMember(otherPlayerUUID);
                                    p.sendMessage(StringUtil.color("&aYou have kicked &f"+otherPlayerNameFormatted+"&a from the Gang."));
                                    if (otherPlayer != null) {
                                        otherPlayer.sendMessage(StringUtil.color("&cYou've been kicked from &f"+gang.getName()+"&c by &f"+p.getName()+"&c."));
                                    }
                                } else {
                                    p.sendMessage(StringUtil.color("&cYou can't kick Gang members that have the same or higher rank then you."));
                                }
                            } else {
                                p.sendMessage(StringUtil.color("&cUsage: /gang kick <player>"));
                            }
                        }
                        default -> p.sendMessage(NOT_MOD);
                    }
                } else {
                    p.sendMessage(NOT_IN_GANG);
                }
                return true;
            }
            p.sendMessage(StringUtil.color("&cInvalid Arguments."));
        }
        return true;
    }

    private boolean isValidLength(String name) {
        int nameSize = name.length();
        return nameSize >= 3 && nameSize <= 15;
    }

    private boolean isAlphaNumeric(String name) {
        return name.chars().allMatch(Character::isLetterOrDigit);
    }
}
