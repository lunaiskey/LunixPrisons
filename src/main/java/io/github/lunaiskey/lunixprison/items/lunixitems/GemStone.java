package io.github.lunaiskey.lunixprison.items.lunixitems;

import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.player.inventories.GemStoneGUI;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GemStone extends LunixItem {

    public GemStone(ItemID id) {
        super(id, "Gemstone", null, Material.PLAYER_HEAD);
    }

    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.getSkull(StringUtil.color("&f" + getName() + " Gemstone"), getLore(),getSkullTexture(),getSkullUUID());
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        return item;
    }

    @Override
    public List<String> getLore() {
        List<String> desc = new ArrayList<>();
        desc.add(StringUtil.color("&7This Gemstone is used to upgrade"));
        desc.add(StringUtil.color("&7your Armor to Tier "+getTier()));
        return desc;
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
        Player p = e.getPlayer();
        p.openInventory(new GemStoneGUI(p).getInv());
    }

    public int getTier() {
        return switch (getItemID()) {
            case DIAMOND_GEMSTONE -> 10;
            case RUBY_GEMSTONE -> 9;
            case EMERALD_GEMSTONE -> 8;
            case SAPPHIRE_GEMSTONE -> 7;
            case AMBER_GEMSTONE -> 6;
            case TOPAZ_GEMSTONE -> 5;
            case JADE_GEMSTONE -> 4;
            case OPAL_GEMSTONE -> 3;
            case JASPER_GEMSTONE -> 2;
            case AMETHYST_GEMSTONE -> 1;
            default -> 0;
        };
    }
    @Override
    public String getName() {
        String name = WordUtils.capitalizeFully(getItemID().name().substring(0,getItemID().name().length()-9));
        return getColor()+name;
    }

    public ChatColor getColor() {
        return ChatColor.of("#"+getHex());
    }

    public String getHex() {
        return switch (getItemID()) {
            case AMETHYST_GEMSTONE -> "A12FFA";
            case JASPER_GEMSTONE -> "A30071";
            case OPAL_GEMSTONE -> "EEEEEE";
            case JADE_GEMSTONE -> "49FCAA";
            case TOPAZ_GEMSTONE -> "E6D48A";
            case AMBER_GEMSTONE -> "FA7D33";
            case SAPPHIRE_GEMSTONE -> "2441FA";
            case EMERALD_GEMSTONE -> "39AD39";
            case RUBY_GEMSTONE -> "A1000B";
            case DIAMOND_GEMSTONE -> "4DF5FC";
            default -> "FFFFFF";
        };
    }

    public String getSkullTexture() {
        return switch (getItemID()) {
            case DIAMOND_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc3MmIzODgyZTE1NGMyMjU4OGU4MDFkZmFmMDI5Mzc4OTdjODg5ZTk5ZWYwZDkyZDhmNWJmYTM5MDJjZWI2MCJ9fX0=";
            case RUBY_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZkODEwNjhjYmRmNGEzNjQyMzFhMjY0NTNkNmNkNjYwYTAwOTVmOWNkODc5NTMwN2M1YmU2Njc0Mjc3MTJlIn19fQ==";
            case EMERALD_GEMSTONE -> "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQxYzZlMjQ5ZGE4MTRmYmE3NjA0MzA1YWM0OGZmY2ZjM2U1MGYxNTJiY2M4ZjBiYWVlYTMyOWU1MmQ4MTViMyJ9fX0=";
            case SAPPHIRE_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGEwYWY5OWU4ZDg3MDMxOTRhODQ3YTU1MjY4Y2Y1ZWY0YWM0ZWIzYjI0YzBlZDg2NTUxMzM5ZDEwYjY0NjUyOSJ9fX0=";
            case AMBER_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTczYmNmYzM5ZWI4NWRmMTg0ODUzNTk4NTIxNDA2MGExYmQxYjNiYjQ3ZGVmZTQyMDE0NzZlZGMzMTY3MTc0NCJ9fX0=";
            case TOPAZ_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYzOTI3NzNkMTE0YmUzMGFlYjNjMDljOTBjYmU2OTFmZmVhY2ViMzk5YjUzMGZlNmZiNTNkZGMwY2VkMzcxNCJ9fX0=";
            case JADE_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyODJjNmJiODM0M2UwZjBkNjFlZTA3NDdkYWRhNzUzNDRmMzMyZTlmZjBhY2FhM2FkY2RmMDkzMjFkM2RkIn19fQ==";
            case OPAL_GEMSTONE -> "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE4YTRlZDQxYjlkMGUwNzFiZmMxMjNiNmE3YWZhZDM0MDE4NGI4Y2U2Zjc1OTcxNjRmNzZhMTg5YzYyMTkxMSJ9fX0=";
            case JASPER_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTczNTExZTUwNGMzMTZiMTM5ZWRiMzVmZWJlNzNlZjU5MWMwZjQ1NWU4Y2FmOWVlMzUzYmMxMmI2YzE0YTkyMiJ9fX0=";
            case AMETHYST_GEMSTONE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkYjU5MjYwODk1NTc4ZDM3ZTU5NTA1ODgwNjAyZGU5NDBiMDg4ZTVmZmY4ZGEzZTY1MjAxZDczOWM4NmU4NCJ9fX0=";
            default -> "";
        };
    }

    public UUID getSkullUUID() {
        return switch (getItemID()) {
            case DIAMOND_GEMSTONE -> UUID.fromString("46a1f161-f967-4e86-ab1c-092a68645cc4");
            case RUBY_GEMSTONE -> UUID.fromString("a5397f9d-5a9b-4a25-9655-dbebe393e17b");
            case EMERALD_GEMSTONE -> UUID.fromString("234b2ba5-0e80-4815-b7a5-953042a8e5e0");
            case SAPPHIRE_GEMSTONE -> UUID.fromString("6865c46d-57b5-4342-be16-1ad4e1bd995d");
            case AMBER_GEMSTONE -> UUID.fromString("c03de23a-7265-4ec2-ac2a-1a4b1c7905b7");
            case TOPAZ_GEMSTONE -> UUID.fromString("0638c209-a618-44e6-b372-410f3fd7db02");
            case JADE_GEMSTONE -> UUID.fromString("2701b03a-7f00-482e-927f-42a4f114a68a");
            case OPAL_GEMSTONE -> UUID.fromString("88ca930f-5d1d-465c-a0dd-56ed4b9bea1a");
            case JASPER_GEMSTONE -> UUID.fromString("4ef6de3c-d7e1-44d8-bb5b-52f7ccb87505");
            case AMETHYST_GEMSTONE -> UUID.fromString("7d33e0eb-4a47-4aea-ad47-1355f460b403");
            default -> UUID.fromString("022cca33-3e93-4890-a6e7-6d0c8b44e449");
        };
    }

    public int getIntFromHex() {
        return Integer.decode("#"+getHex());
    }

}
