package io.github.lunaiskey.lunixprison.util.reward;

import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reward {

    private String rewardName;
    private double chance;

    private List<String> commands;
    private List<ItemStack> items;

    public Reward(String rewardName, double chance, List<ItemStack> items, List<String> commandsList) {
        this.rewardName = rewardName;
        this.chance = chance;
        this.commands = commandsList;
        this.items = items;
        if (commands == null) return;
        commands = new ArrayList<>(commands);
        if (commandsList.size() <= 0) return;
        for (int i = 0;i<commands.size();i++) {
            String command = commands.get(i);
            StringBuilder builder = new StringBuilder(command);
            if (builder.charAt(0) == '/') {
                builder.deleteCharAt(0);
            }
            commands.set(i,builder.toString());
        }
    }

    public String getRewardName() {
        return rewardName;
    }

    public double getChance() {
        return chance;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void giveReward(Player player) {
        Location playerLoc = player.getLocation();
        if (commands != null && commands.size() > 0) {
            for (String command : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("%player%",player.getDisplayName()));
            }
        }
        if (items != null && items.size() > 0) {
            for (ItemStack item : items) {
                Map<Integer,ItemStack> leftOver = player.getInventory().addItem(item);
                if (playerLoc.getWorld() != null) {
                    for (ItemStack leftOverItem : leftOver.values()) {
                        playerLoc.getWorld().dropItem(playerLoc,leftOverItem);
                    }
                }
            }
        }
        player.sendMessage(StringUtil.color("&aYou have been given reward "+rewardName+"&a."));
    }
}
