package io.github.lunaiskey.lunixprison.modules.armor.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.modules.armor.inventories.ArmorGUI;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandArmor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.openInventory(new ArmorGUI().getInv(p));
            return true;
        }
        String arg0 = args[0].toLowerCase();
        switch(arg0) {
            case "set" -> {setCommand(p,p,label,args);return true;}
        }
        Player other = Bukkit.getPlayer(arg0);
        if (other == null) {
            p.sendMessage(ChatColor.RED+"Player \""+args[0]+"\" has to be online to do this.");
            return true;
        }
        setCommand(p,other,label,args);
        return true;
    }

    private void setCommand(Player sender, Player toModify, String label, String[] args) {
        //armor <playername> set <piece> <option> <value> [extra value]
        //armor set <piece> <option> <value> [extra value]
        if (!sender.hasPermission("lunix.armor.set")) {
            sender.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        boolean modifySender = toModify.getUniqueId().equals(sender.getUniqueId());
        int offset = modifySender ? 0 : 1;
        if (args.length < (2+offset)) {
            sender.sendMessage(
                    "Usage:",
                    "| /armor [player] set <Piece> <UpgradeType> <value>",
                    "| /armor [player] set <Piece> ability <AbilityType> <value>"
            );
            return;
        }
        ArmorSlot armorSlot;
        try {
            armorSlot = ArmorSlot.valueOf(args[1+offset]);
        } catch (IllegalArgumentException ignored) {
            return;
        }
        switch (args[2+offset].toLowerCase()) {
            case "color" -> setColorCommand(sender,toModify,armorSlot,offset,label,args);
            case "tier" -> setTierCommand(sender,toModify,armorSlot,offset,label,args);
            case "ability" -> setAbilityCommand(sender,toModify,armorSlot,offset,label,args);
        }
    }

    private void setTierCommand(Player sender, Player toModify, ArmorSlot slot, int offset, String label, String[] args) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(toModify.getUniqueId());
        int tier;
        if (args.length < (5+offset)) {
            sender.sendMessage(Messages.NOT_ENOUGH_ARGS.getText());
            return;
        }
        try {
             tier = Integer.parseInt(args[4+offset]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage("Invalid Number");
            return;
        }
        Armor armor = lunixPlayer.getArmor().get(slot);
        if (tier < 0) {
            tier = 0;
        } else if (tier > armor.getTierMax()) {
            tier = armor.getTierMax();
        }
        armor.setTier(tier);
        sender.sendMessage("Set " + toModify.getName() + "'s " + slot.getName() + "'s Tier to " + tier);
        if (lunixPlayer.isArmorEquiped()) {
            toModify.getInventory().setItem(slot.getEquipmentSlot(), armor.getItemStack());
        }
    }

    private void setColorCommand(Player sender, Player toModify, ArmorSlot slot, int offset, String label, String[] args) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(toModify.getUniqueId());
        if (args.length < (5+offset)) {
            sender.sendMessage(Messages.NOT_ENOUGH_ARGS.getText());
            return;
        }
        String colorStr = args[4+offset];
        int color;
        try {
            if (colorStr.length() == 7 && colorStr.charAt(0) == '#') {
                color = Integer.parseInt(colorStr.substring(1), 16);
            } else if (colorStr.length() <= 6) {
                color = Integer.parseInt(colorStr, 16);
            } else {
                throw new IllegalArgumentException("Unsupported Hex Code");
            }
        } catch (IllegalArgumentException ignored) {
            sender.sendMessage(ChatColor.RED+"Invalid hex code!");
            return;
        }
        if (color < 0) {
            color = 0;
        } else if (color > 0xFFFFFF) {
            color = 0xFFFFFF;
        }
        Armor armor = lunixPlayer.getArmor().get(slot);
        armor.setCustomColor(Color.fromRGB(color));
        sender.sendMessage("Set " + toModify.getName() + "'s " + slot.getName() + "'s color to " +Integer.toHexString(color));
        if (lunixPlayer.isArmorEquiped()) {
            toModify.getInventory().setItem(slot.getEquipmentSlot(), armor.getItemStack());
        }
    }

    private void setAbilityCommand(Player sender, Player toModify, ArmorSlot slot, int offset, String label, String[] args) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(toModify.getUniqueId());
        AbilityType abilityType;
        if (args.length < (6+offset)) {
            sender.sendMessage(Messages.NOT_ENOUGH_ARGS.getText());
            return;
        }
        try {
            abilityType = AbilityType.valueOf(args[5+offset].toUpperCase());
        } catch (IllegalArgumentException ignored) {
            sender.sendMessage("Invalid Armor Upgrade name.");
            return;
        }
        int level;
        try {
            level = Integer.parseInt(args[6+offset]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage("Invalid number.");
            return;
        }
        Ability ability = abilityType.getAbility();
        if (level < 0) {
            level = 0;
        } else if (level > ability.getMaxLevel()) {
            level = ability.getMaxLevel();
        }
        Armor armor = lunixPlayer.getArmor().get(slot);
        armor.getAbilties().put(abilityType,level);
        if (lunixPlayer.isArmorEquiped()) {
            toModify.getInventory().setItem(slot.getEquipmentSlot(), armor.getItemStack());
        }
        sender.sendMessage("Set " + toModify.getName() + "'s " + slot.getName() + "'s ability " + ability.getName() + " to " + level);
    }
}
