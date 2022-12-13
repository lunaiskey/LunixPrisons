package io.github.lunaiskey.lunixprison.items.lunixitems;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.player.boosters.Booster;
import io.github.lunaiskey.lunixprison.player.boosters.BoosterType;
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
    private int length;
    private double multiplier;

    public BoosterItem(BoosterType boosterType, int length, double multiplier) {
        super(ItemID.BOOSTER, null, null, Material.BEACON);
        this.boosterType = boosterType;
        this.length = length;
        this.multiplier = multiplier;
    }

    public BoosterItem(ItemStack booster) {
        super(ItemID.BOOSTER,null,null,Material.BEACON);
        CompoundTag tag = NBTTags.getPyrexDataMap(booster);
        CompoundTag boosterData = tag.getCompound("boosterData");
        try {
            boosterType = BoosterType.valueOf(boosterData.getString("type"));
        } catch (Exception ignored){
            boosterType = null;
        }
        length = boosterData.getInt("length");
        multiplier = boosterData.getDouble("multiplier");
    }

    @Override
    public ItemStack getItemStack() {
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Multiplier: &F"+multiplier+"x"));
        lore.add(StringUtil.color("&7Length: &f"+ TimeUtil.parseTime(length)));
        lore.add(" ");
        lore.add(StringUtil.color("&eR-Click to activate"));
        String name;
        if (boosterType != null) {
            name = boosterType.getColor() + boosterType.getName();
        } else {
            name = ChatColor.GRAY + "Null Booster";
        }
        ItemStack item = ItemBuilder.createItem(name,getMaterial(),lore);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        CompoundTag boosterData = new CompoundTag();
        boosterData.putString("type",boosterType.name());
        boosterData.putInt("length",length);
        boosterData.putDouble("multiplier",multiplier);
        item = NBTTags.addPyrexData(item,"boosterData",boosterData);
        item = NBTTags.addPyrexData(item, "uuid", UUID.randomUUID().toString());
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        if (boosterType != null) {
            Player p = e.getPlayer();
            Action action = e.getAction();
            LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            if (lunixPlayer != null) {
                List<Booster> boosters = lunixPlayer.getBoosters();
                switch (action) {
                    case RIGHT_CLICK_AIR,RIGHT_CLICK_BLOCK -> {
                        boosters.add(new Booster(boosterType,multiplier,length,System.currentTimeMillis(),true));
                        e.getItem().setAmount(e.getItem().getAmount()-1);
                    }
                }
            }

        } else {
            e.getPlayer().sendMessage(StringUtil.color("&eThis Booster is invalid."));
        }
    }
}
