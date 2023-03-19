package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaCurrencyVoucher;
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

public class CurrencyVoucher extends LunixItem {

    private CurrencyType type;
    private BigInteger amount;
    //private String playerName; unused for now.

    public CurrencyVoucher( CurrencyType type,BigInteger amount) {
        super(ItemID.CURRENCY_VOUCHER, "   &7- &6&lBank Note &7-   ", null, Material.PAPER);
        this.type = type;
        this.amount = amount;
    }

    public CurrencyVoucher(CurrencyType type,Long amount) {
        this(type,BigInteger.valueOf(amount));
    }

    public CurrencyVoucher(MetaCurrencyVoucher currencyVoucher) {
        this(currencyVoucher.getType(),currencyVoucher.getAmount());
    }

    public CurrencyVoucher() {
        this(null,BigInteger.ZERO);
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        MetaCurrencyVoucher currencyMeta = (MetaCurrencyVoucher) meta;
        CurrencyType type = currencyMeta.getType();
        BigInteger amount = currencyMeta.getAmount();
        String strType = type.getColorCode()+type.getName();
        String strAmount = type.getColorCode()+type.getUnicode()+"&f"+Numbers.formattedNumber(amount);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&7• Type: "+strType));
        lore.add(StringUtil.color("&7• Amount: "+strAmount));
        lore.add(" ");
        lore.add(StringUtil.color("&eRight Click to redeem!"));
        return lore;
    }

    @Override
    public ItemStack getItemStack() {
        String name = getDisplayName();
        MetaCurrencyVoucher voucher = new MetaCurrencyVoucher(type,amount);
        ItemStack item = ItemBuilder.createItem(name,getMaterial(),getLore(voucher));
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        voucher.applyMeta(item);
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
        Currency.giveCurrency(e.getPlayer().getUniqueId(), type, amount);
        item.setAmount(e.getItem().getAmount() - 1);
        ChatColor color = type.getColorCode();
        String unicode = type.getUnicode();
        String text = type.getName();
        p.sendMessage(color + "Redeemed " + unicode + ChatColor.WHITE + Numbers.formattedNumber(amount) + " " + color + text + ".");
    }
}
