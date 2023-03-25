package io.github.lunaiskey.lunixprison.util.reward.rewards;

import io.github.lunaiskey.lunixprison.util.reward.LunixReward;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommandReward implements LunixReward {

    private String command;

    public SendCommandReward(String command) {
        this.command = command;
    }

    @Override
    public void execute(Player player) {
        String command = this.command.replace("%player%",player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
    }
}
