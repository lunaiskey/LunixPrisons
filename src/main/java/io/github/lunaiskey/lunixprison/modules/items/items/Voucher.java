package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.modules.player.Currency;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Voucher extends LunixItem {

    private BigInteger amount;
    private CurrencyType type;
    private String playerName;

    public Voucher(BigInteger amount, CurrencyType type, String playerName) {
        super(ItemID.VOUCHER, "   &7- &6&lBank Note &7-   ", null, Material.PAPER);
        this.amount = amount;
        this.type = type;
    }

    public Voucher( CurrencyType type,BigInteger amount) {
        this(amount,type,null);
    }

    public Voucher(Long amount, CurrencyType type, String playerName) {
        this(BigInteger.valueOf(amount),type,playerName);
    }

    @Override
    public ItemStack getItemStack() {
        String name = getDisplayName();
        String strType = type.getColorCode()+type.getName();
        String strAmount = type.getColorCode()+type.getUnicode()+"&f"+Numbers.formattedNumber(amount);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&7• Type: "+strType));
        lore.add(StringUtil.color("&7• Amount: "+strAmount));
        lore.add(" ");
        lore.add(StringUtil.color("&eRight Click to redeem!"));
        ItemStack item = ItemBuilder.createItem(name,getMaterial(),lore);
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        item = NBTTags.setCurrencyVoucherTags(item,amount,type);
        return item;

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (type == null || amount == null) {
            p.sendMessage(StringUtil.color("&cInvalid Voucher..."));
            return;
        }
        ItemStack item = e.getItem();
        Currency.giveCurrency(e.getPlayer().getUniqueId(), type,amount);
        item.setAmount(e.getItem().getAmount()-1);
        ChatColor color = type.getColorCode();
        String unicode = type.getUnicode();
        String text = type.getName();
        p.sendMessage(color+"Redeemed "+unicode+ChatColor.WHITE+Numbers.formattedNumber(amount)+" "+color+text+".");
    }
}
