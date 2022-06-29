package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.enchants.*;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PickaxeHandler {

    private static final String id = "PYREX_PICKAXE";
    private final Map<EnchantType,PyrexEnchant> enchantments = new HashMap<>();

    public PickaxeHandler() {
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
        enchantments.put(EnchantType.XP_BOOST,new XPBoost());
        enchantments.put(EnchantType.NIGHT_VISION,new NightVision());
        enchantments.put(EnchantType.MINE_BOMB,new MineBomb());
    }

    public Map<EnchantType, PyrexEnchant> getEnchantments() {
        return enchantments;
    }

    public static String getId() {
        return id;
    }

    public ItemStack updatePickaxe(ItemStack item, UUID p) {
        CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(item);
        if (pyrexDataMap.contains("id")) {
            // is custom pickaxe
            if (pyrexDataMap.getString("id").equals("PyrexPickaxe")) {
                pyrexDataMap.putString("id","PYREX_PICKAXE");
                item = NBTTags.addPyrexData(item,"id","PYREX_PICKAXE");
            }
            if (pyrexDataMap.getString("id").equals("PYREX_PICKAXE")) {
                ItemMeta meta = item.getItemMeta();
                OfflinePlayer player = Bukkit.getOfflinePlayer(p);
                meta.setDisplayName(StringUtil.color("&d"+player.getName()+"'s &fPickaxe"));
                List<String> lore = new ArrayList<>();
                PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p).getPickaxe();
                Map<EnchantType, Integer> enchants = pickaxe.getEnchants();
                lore.add(" ");
                lore.add(StringUtil.color("&d&lPickaxe Stats:"));
                lore.add(StringUtil.color("&d&l| &fBlocks: "+pickaxe.getBlocksBroken()));
                lore.add(" ");
                lore.add(StringUtil.color("&d&lEnchants"));
                for (EnchantType enchantType : EnchantType.getSortedSet()) {
                    PyrexEnchant pyrexEnchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantType);
                    if (enchants.containsKey(enchantType) && enchants.get(enchantType) > 0) {
                        lore.add(StringUtil.color("&d&l| &f"+pyrexEnchant.getName()+" "+enchants.get(enchantType)));
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
        }
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
            if (NBTTags.getPyrexDataMap(item).getString("id").equals("PYREX_PICKAXE")) {
                hasPickaxe = true;
                break;
            }
        }
        if (!hasPickaxe) {
            p.getInventory().addItem(PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxe().getItemStack());
        }
        return hasPickaxe;
    }

}
