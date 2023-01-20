package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.pickaxe.enchants.*;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PickaxeManager {

    private static final String LUNIX_PICKAXE_ID = "LUNIX_PICKAXE";
    private final Map<EnchantType, LunixEnchant> enchantments = new HashMap<>();

    public PickaxeManager() {
        registerEnchants();
    }

    private void registerEnchants() {
        enchantments.put(EnchantType.EFFICIENCY,new Efficiency());
        enchantments.put(EnchantType.FORTUNE,new Fortune());
        enchantments.put(EnchantType.HASTE,new Haste());
        enchantments.put(EnchantType.JUMP_BOOST,new JumpBoost());
        enchantments.put(EnchantType.SPEED,new Speed());
        enchantments.put(EnchantType.JACK_HAMMER,new JackHammer());
        enchantments.put(EnchantType.KEY_FINDER,new KeyFinder());
        enchantments.put(EnchantType.GEM_FINDER,new GemFinder());
        enchantments.put(EnchantType.LOOT_FINDER,new LootFinder());
        enchantments.put(EnchantType.STRIKE,new Strike());
        enchantments.put(EnchantType.EXPLOSIVE,new Explosive());
        enchantments.put(EnchantType.NUKE,new Nuke());
        //enchantments.put(EnchantType.XP_BOOST,new XPBoost());
        enchantments.put(EnchantType.NIGHT_VISION,new NightVision());
        enchantments.put(EnchantType.MINE_BOMB,new MineBomb());
    }





    public ItemStack updatePickaxe(ItemStack item, UUID p) {
        ItemID id = NBTTags.getItemID(item);
        if (id != ItemID.LUNIX_PICKAXE) return item;
        ItemMeta meta = item.getItemMeta();
        LunixPlayer player = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p);
        LunixPickaxe pickaxe = player.getPickaxe();
        if (pickaxe.getRename() == null) {
            meta.setDisplayName(StringUtil.color("&a"+player.getName()+"'s &fPickaxe"));
        } else {
            meta.setDisplayName(pickaxe.getRename());
        }
        List<String> lore = new ArrayList<>();
        Map<EnchantType, Integer> enchants = pickaxe.getEnchants();
        lore.add(" ");
        lore.add(StringUtil.color("&e&lPickaxe Stats:"));
        lore.add(StringUtil.color("&e&l| &fLevel: 1"));
        lore.add(StringUtil.color("&e&l| &fExp: &7&l|||||||||| &b0/250"));
        lore.add(StringUtil.color("&e&l| &fBlocks: "+pickaxe.getBlocksBroken()));
        lore.add(" ");
        lore.add(StringUtil.color("&b&lEnchants"));
        for (EnchantType enchantType : EnchantType.getSortedEnchants()) {
            LunixEnchant lunixEnchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantType);
            if (enchants.containsKey(enchantType) && enchants.get(enchantType) > 0) {
                lore.add(StringUtil.color("&b&l| &f"+ lunixEnchant.getName()+" "+enchants.get(enchantType)));
                if (enchantType == EnchantType.EFFICIENCY) {
                    meta.addEnchant(Enchantment.DIG_SPEED,enchants.get(enchantType),true);
                }
            }
        }
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    public void updateInventoryPickaxe(Player p) {
        ItemStack[] inventory = new ItemStack[p.getInventory().getContents().length];
        int i = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                inventory[i] = updatePickaxe(item,p.getUniqueId());
            } else {
                inventory[i] = null;
            }
            i++;
        }
        p.getInventory().setContents(inventory);
    }

    public void updateMainHandPickaxe(Player p) {
        p.getInventory().setItemInMainHand(updatePickaxe(p.getInventory().getItemInMainHand(),p.getUniqueId()));
    }

    public boolean hasOrGivePickaxe(Player p) {
        boolean hasPickaxe = false;
        for(ItemStack item : p.getInventory().getContents()) {
            if (NBTTags.getLunixDataMap(item).getString("id").equals(LUNIX_PICKAXE_ID)) {
                hasPickaxe = true;
                break;
            }
        }
        if (!hasPickaxe) {
            p.getInventory().addItem(LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxe().getItemStack());
        }
        return hasPickaxe;
    }

    public Map<EnchantType, LunixEnchant> getEnchantments() {
        return enchantments;
    }

    public static String getLunixPickaxeId() {
        return LUNIX_PICKAXE_ID;
    }

}
