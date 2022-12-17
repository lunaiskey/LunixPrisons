package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.player.armor.Armor;
import io.github.lunaiskey.lunixprison.player.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.player.inventories.ArmorGUI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandArmor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.openInventory(new ArmorGUI(p).getInv());
                return true;
            }
            Player other = Bukkit.getPlayer(args[0]);
            if (other != null) {
                LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(other.getUniqueId());
                if (args.length >= 3) {
                    ArmorSlot armorSlot = ArmorSlot.valueOf(args[1].toUpperCase());
                    String action = args[2];
                    Armor armor = lunixPlayer.getArmor().get(armorSlot);
                    if (action.equalsIgnoreCase("set")) {
                        if (p.hasPermission("lunix.armor.set")) {
                            if (args.length >= 5) {
                                String changeType = args[3];
                                String value = args[4];
                                if (changeType.equalsIgnoreCase("tier")) {
                                    try {
                                        int newTier = Integer.parseInt(args[4]);
                                        if (newTier < 0) {
                                            newTier = 0;
                                        } else if (newTier > armor.getTierMax()) {
                                            newTier = armor.getTierMax();
                                        }
                                        armor.setTier(newTier);
                                        p.sendMessage("Set " + other.getName() + "'s " + armorSlot.getName() + "'s " + changeType.toLowerCase() + " to " + value);
                                        if (lunixPlayer.isArmorEquiped()) {
                                            other.getInventory().setItem(armorSlot.getSlot(), armor.getItemStack());
                                        }
                                    } catch (NumberFormatException ignored) {
                                        p.sendMessage("Invalid Number");
                                    }
                                    return true;
                                }
                                if (changeType.equalsIgnoreCase("color")) {
                                    int newValue = 0;
                                    boolean success = false;
                                    try {
                                        newValue = Integer.parseInt(value);
                                        if (newValue < 0) {
                                            newValue = 0;
                                        } else if (newValue > 0xFFFFFF) {
                                            newValue = 0xFFFFFF;
                                        }
                                        success = true;
                                    } catch (NumberFormatException ignored) {

                                    }
                                    if (!success) {
                                        try {
                                            if (value.length() == 7 && value.charAt(0) == '#') {
                                                newValue = Integer.parseInt(value.substring(1), 16);
                                            } else if (value.length() <= 6) {
                                                newValue = Integer.parseInt(value, 16);
                                            }
                                            if (newValue < 0) {
                                                newValue = 0;
                                            } else if (newValue > 0xFFFFFF) {
                                                newValue = 0xFFFFFF;
                                            }
                                            success = true;
                                        } catch (NumberFormatException ignored) {

                                        }
                                    }
                                    if (!success) {
                                        p.sendMessage("invalid color, please use either a whole number or a hex value");
                                        return true;
                                    } else {
                                        armor.setCustomColor(Color.fromRGB(newValue));
                                        p.sendMessage("Set " + other.getName() + "'s " + armorSlot.getName() + "'s " + changeType.toLowerCase() + " to " + value);
                                        if (lunixPlayer.isArmorEquiped()) {
                                            other.getInventory().setItem(armorSlot.getSlot(), armor.getItemStack());
                                        }

                                    }
                                    return true;
                                }
                                try {
                                    AbilityType abilityType = AbilityType.valueOf(changeType.toUpperCase());
                                    Ability ability = LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap().get(abilityType);
                                    int newValue = Integer.parseInt(value);
                                    if (newValue < 0) {
                                        newValue = 0;
                                    } else if (newValue > ability.getMaxLevel()) {
                                        newValue = ability.getMaxLevel();
                                    }
                                    armor.getAbilties().put(abilityType,newValue);
                                    if (lunixPlayer.isArmorEquiped()) {
                                        other.getInventory().setItem(armorSlot.getSlot(), armor.getItemStack());
                                    }
                                    p.sendMessage("Set " + other.getName() + "'s " + armorSlot.getName() + "'s " + changeType.toLowerCase() + " to " + value);
                                } catch (Exception ignored) {
                                    if (ignored instanceof NumberFormatException) {
                                        p.sendMessage("Invalid number.");
                                        return true;
                                    } else if (ignored instanceof IllegalArgumentException) {
                                        p.sendMessage("Invalid Armor Upgrade name.");
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                p.sendMessage("Invalid Player.");
            }

        } else {
            sender.sendMessage("This is a player only command.");
        }
        return true;
    }
}
