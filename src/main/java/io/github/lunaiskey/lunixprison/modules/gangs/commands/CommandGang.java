package io.github.lunaiskey.lunixprison.modules.gangs.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.gangs.Gang;
import io.github.lunaiskey.lunixprison.modules.gangs.GangManager;
import io.github.lunaiskey.lunixprison.modules.gangs.GangMember;
import io.github.lunaiskey.lunixprison.modules.gangs.GangRankType;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private GangManager gangManager;
    private PlayerManager playerManager;
    private Logger log;

    private static final String ALREADY_IN_GANG = ChatColor.RED+"You're already in a gang.";
    private static final String NOT_IN_GANG = ChatColor.RED+"You have to be in a gang to use this command.";
    private static final String NOT_OWNER = ChatColor.RED+"You have to be the Gang Owner to use this command.";
    private static final String NOT_MOD = ChatColor.RED+"You have to be a Gang Mod or higher to use this command.";
    private static final String INVALID_NAME_SIZE = ChatColor.RED+"Gang name has to be between 3 and 15 characters.";
    private static final String NOT_ALPHANUMERIC = ChatColor.RED+"Gang name has to be alphanumeric.";
    private static final String GANG_ALREADY_EXISTS = ChatColor.RED+"A Gang already exists with that name.";
    //private static final String NOT_IMPLEMENTED = ChatColor.RED+"This hasn't been implemented yet.";

    //private final boolean debug = false; //Unused rn.
    private static final int[] LENGTH_RANGE = {3,15};

    public CommandGang(LunixPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        this.playerManager = plugin.getPlayerManager();
        this.gangManager = plugin.getGangManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(StringUtil.color(
                    "&3&lGang Commands:",
                    "&3&l| &f/gang create <name> &7Create a new gang.",
                    "&3&l| &f/gang rename <name> &7Rename your gang.",
                    "&3&l| &f/gang info [name] &7Get info on a gang.",
                    "&3&l| &f/gang join <name> &7Join a gang.",
                    "&3&l| &f/gang leave &7Leave current gang.",
                    "&3&l| &f/gang invite <player> &7Invite player to gang.",
                    "&3&l| &f/gang kick <player> &7Kick player from gang.",
                    "&3&l| &f/gang transfer <name> &7Transfer ownership to a gang member.",
                    "&3&l| &f/gang disband &7Delete your gang."
            ));
            return true;
        }
        String arg0 = args[0].toLowerCase();
        switch(arg0) {
            case "create" -> createCommand(sender, args);
            case "rename" -> renameCommand(sender, args);
            case "info" -> infoCommand(sender, args);
            case "join" -> joinCommand(sender, args);
            case "leave" -> leaveCommand(sender, args);
            case "invite" -> inviteCommand(sender, args);
            case "kick" -> kickCommand(sender, args);
            case "transfer" -> transferCommand(sender, args);
            case "disband" -> disbandCommand(sender, args);
            default -> p.sendMessage(ChatColor.RED+"&cInvalid Arguments.");
        }
        return true;
    }

    private void createCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        if (gangManager.isInGang(playerUUID)) {
            p.sendMessage(ALREADY_IN_GANG);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(StringUtil.color("&cUsage: /gang create <name>"));
            return;
        }
        String name = args[1];
        if (!isValidLength(name)) {
            p.sendMessage(INVALID_NAME_SIZE);
            return;
        }
        if (!isAlphaNumeric(name)) {
            p.sendMessage(NOT_ALPHANUMERIC);
            return;
        }
        if (gangManager.gangExists(name)) {
            p.sendMessage(GANG_ALREADY_EXISTS);
            return;
        }
        gangManager.createGang(playerUUID,name);
        p.sendMessage(StringUtil.color("&aYou have created the gang &f"+name+"&a."));
    }
    private void renameCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (!gangManager.isInGang(playerUUID)) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        GangRankType rank = gang.getMembers().get(playerUUID).getType();
        String newName = args[1];
        if (rank != GangRankType.OWNER) {
            p.sendMessage(NOT_OWNER);
            return;
        }
        if (!isValidLength(newName)) {
            p.sendMessage(INVALID_NAME_SIZE);
            return;
        }
        if (!isAlphaNumeric(newName)) {
            p.sendMessage(NOT_ALPHANUMERIC);
            return;
        }
        if (gangManager.gangExists(newName) && !gang.getName().equalsIgnoreCase(newName)) {
            p.sendMessage(GANG_ALREADY_EXISTS);
            return;
        }
        gang.setName(newName);
        p.sendMessage(StringUtil.color("&aYou have renamed the gang to &f"+newName+"&a."));
    }
    private void infoCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        boolean useOtherGang = false;
        if (args.length >= 2) {
            String otherGangName = args[1];
            gang = gangManager.getGang(otherGangName);
            useOtherGang = true;
        }
        if (gang == null) {
            if (useOtherGang) {
                p.sendMessage(StringUtil.color("&cGang \""+args[1]+"\" doesn't exist."));
            } else {
                p.sendMessage(NOT_IN_GANG);
            }
        }
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
    }
    private void joinCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (gang != null) {
            p.sendMessage(ALREADY_IN_GANG);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(StringUtil.color("&cUsage: /gang join <name>"));
            return;
        }
        String rawGangName = args[1];
        Gang otherGang = gangManager.getGang(rawGangName);
        if (otherGang == null) {
            p.sendMessage(StringUtil.color("&cGang \""+rawGangName+"\" doesn't exist."));
            return;
        }
        if (!otherGang.getPendingInvites().contains(playerUUID)) {
            p.sendMessage(StringUtil.color("&c"+otherGang.getName()+" hasn't invited you to their gang."));
            return;
        }
        p.sendMessage(StringUtil.color("&aYou have joined &f"+otherGang.getName()+"&a."));
        for (UUID memberUUID : otherGang.getMembers().keySet()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null) {
                continue;
            }
            member.sendMessage(StringUtil.color("&f"+p.getName()+"&a has joined your gang."));
        }
        otherGang.addMember(playerUUID);
    }
    private void leaveCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (gang == null) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        if (gang.getOwner().equals(playerUUID)) {
            p.sendMessage(StringUtil.color("&cPlease either disband your gang or transfer it to another player."));
            return;
        }
        gang.removeMember(playerUUID);
        p.sendMessage(StringUtil.color("&aYou have left gang &f"+gang.getName()+"&a."));
        for (UUID member : gang.getMembers().keySet()) {
            Player onlineMember = Bukkit.getPlayer(member);
            if (onlineMember == null) {
                continue;
            }
            onlineMember.sendMessage(StringUtil.color("&aPlayer &f"+p.getName()+"&a has left your gang."));
        }
    }
    private void inviteCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (!gangManager.isInGang(playerUUID)) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        GangRankType rank = gang.getMembers().get(playerUUID).getType();
        if (rank.getWeight() < GangRankType.MOD.getWeight()) {
            p.sendMessage(NOT_MOD);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(StringUtil.color("&cUsage: /gang invite <player>"));
            return;
        }
        Player otherPlayer = Bukkit.getPlayer(args[1]);
        if (otherPlayer == null) {
            p.sendMessage(StringUtil.color("&cPlayer \""+args[1]+"\" isn't online."));
            return;
        }
        if (gangManager.isInGang(otherPlayer.getUniqueId())) {
            p.sendMessage(StringUtil.color("&cYou can't invite "+otherPlayer.getName()+" because they're already in a gang."));
            return;
        }
        gang.getPendingInvites().add(otherPlayer.getUniqueId());
        for (UUID memberUUID : gang.getMembers().keySet()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member == null) {
                continue;
            }
            member.sendMessage(StringUtil.color("&f"+p.getName()+"&a has been invited your gang."));
        }
        otherPlayer.sendMessage(
                "&aYou have been invited to "+gang.getName()+".",
                "&aTo join, run /gang join "+gang.getName()+"."
        );
    }
    private void kickCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);

        if (gang == null) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        GangRankType rank = gang.getMembers().get(playerUUID).getType();
        if (rank.getWeight() < GangRankType.MOD.getWeight()) {
            p.sendMessage(NOT_MOD);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(StringUtil.color("&cUsage: /gang kick <player>"));
            return;
        }
        String otherPlayerName = args[1];
        Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
        UUID otherPlayerUUID = otherPlayer != null ? otherPlayer.getUniqueId() : LunixPrison.getPlugin().getPlayerManager().getPlayerUUID(otherPlayerName);
        if (otherPlayerUUID == null) {
            p.sendMessage(StringUtil.color("&cSomething is off and we couldn't find player..."));
            return;
        }
        String otherPlayerNameFormatted = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayerUUID).getName();
        GangRankType otherPlayerRank = gang.getMembers().get(otherPlayerUUID).getType();
        if (otherPlayerRank.getWeight() >= rank.getWeight()) {
            p.sendMessage(StringUtil.color("&cYou can't kick Gang members that have the same or higher rank then you."));
            return;
        }
        gang.removeMember(otherPlayerUUID);
        p.sendMessage(StringUtil.color("&aYou have kicked &f"+otherPlayerNameFormatted+"&a from the Gang."));
        if (otherPlayer != null) {
            otherPlayer.sendMessage(StringUtil.color("&cYou've been kicked from &f"+gang.getName()+"&c by &f"+p.getName()+"&c."));
        }

    }
    private void transferCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (!gangManager.isInGang(playerUUID)) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        if (!gang.getOwner().equals(playerUUID)) {
            p.sendMessage(NOT_OWNER);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(StringUtil.color("&cUsage: /gang transfer <player>"));
            return;
        }
        Player otherPlayer = Bukkit.getPlayer(args[1]);
        if (otherPlayer == null) {
            p.sendMessage(StringUtil.color("&cPlayer \"" + args[1] + "\" has to be online to transfer to them."));
            return;
        }
        String otherPlayerName = otherPlayer.getName();
        UUID otherPlayerUUID = otherPlayer.getUniqueId();
        if (!gang.getMembers().containsKey(otherPlayerUUID)) {
            p.sendMessage(StringUtil.color("&cPlayer \"" + otherPlayerName + "\" isn't in your Gang."));
            return;
        }
        gang.setOwner(otherPlayerUUID);
        p.sendMessage(StringUtil.color("&aTransferred Gang to " + otherPlayerName + "."));
        otherPlayer.sendMessage(StringUtil.color("&aGang " + gang.getName() + " has been transferred to you."));
    }
    private void disbandCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();
        Gang gang = gangManager.getGang(playerUUID);
        if (gang == null) {
            p.sendMessage(NOT_IN_GANG);
            return;
        }
        if (!gang.getOwner().equals(playerUUID)) {
            p.sendMessage(NOT_OWNER);
            return;
        }
        gangManager.removeGang(gang.getUUID());
    }

    private boolean isValidLength(String name) {
        int nameSize = name.length();
        return nameSize >= LENGTH_RANGE[0] && nameSize <= LENGTH_RANGE[1];
    }

    private boolean isAlphaNumeric(String name) {
        return name.chars().allMatch(Character::isLetterOrDigit);
    }
}
