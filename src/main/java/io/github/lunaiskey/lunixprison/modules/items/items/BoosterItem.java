package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaBoosterItem;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.boosters.Booster;
import io.github.lunaiskey.lunixprison.modules.boosters.BoosterType;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.TimeUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoosterItem extends LunixItem {

    private BoosterType boosterType;
    private int lengthSeconds;
    private double multiplier;

    public BoosterItem(BoosterType boosterType, int lengthSeconds, double multiplier) {
        super(ItemID.BOOSTER, null, null, Material.BEACON);
        this.boosterType = boosterType;
        setDisplayName(boosterType == null ? ChatColor.GRAY + "Null Booster" : boosterType.getColoredName());
        this.lengthSeconds = lengthSeconds;
        this.multiplier = multiplier;
    }

    public BoosterItem(MetaBoosterItem itemMeta) {
        this(itemMeta.getBoosterType(), itemMeta.getLengthSeconds(), itemMeta.getMultiplier());
    }

    public BoosterItem() {
        this(null,0,0);
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial());
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        CompoundTag boosterData = new CompoundTag();
        boosterData.putString("type",boosterType.name());
        boosterData.putInt("length", lengthSeconds);
        boosterData.putDouble("multiplier",multiplier);
        item = NBTTags.addLunixData(item,"boosterData",boosterData);
        item = NBTTags.addLunixData(item, "uuid", UUID.randomUUID().toString());
        LunixPrison.getPlugin().getItemManager().updateItemStack(item);
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public List<String> getDescription() {
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Multiplier: &F"+multiplier+"x"));
        lore.add(StringUtil.color("&7Length: &f"+ TimeUtil.parseTime(lengthSeconds)));
        lore.add(" ");
        lore.add(StringUtil.color("&eR-Click to activate"));
        return lore;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        MetaBoosterItem itemData = new MetaBoosterItem(e.getItem());
        BoosterType boosterType = itemData.getBoosterType();
        int lengthSeconds = itemData.getLengthSeconds();
        double multiplier = itemData.getMultiplier();
        if (boosterType == null) {
            p.sendMessage(StringUtil.color("&eThis Booster is invalid."));
            return;
        }
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getLunixPlayer(p.getUniqueId());
        if (lunixPlayer == null) {
            p.sendMessage(ChatColor.RED+"Unable to apply Booster while player data is unloaded.");
            return;
        }
        Action action = e.getAction();
        List<Booster> boosters = lunixPlayer.getBoosters();
        switch (action) {
            case RIGHT_CLICK_AIR,RIGHT_CLICK_BLOCK -> {
                boosters.add(new Booster(boosterType,multiplier, lengthSeconds,System.currentTimeMillis(),true));
                e.getItem().setAmount(e.getItem().getAmount()-1);
            }
        }
    }


}
