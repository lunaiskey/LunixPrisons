package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixPlayerHeadItem;
import io.github.lunaiskey.lunixprison.modules.items.Rarity;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.player.inventories.GemStoneGUI;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GemStone extends LunixPlayerHeadItem {

    private GemstoneType type;

    public GemStone(GemstoneType type) {
        super(type.getItemID(), "Gemstone", null, null);
        this.type = type;
        switch (type) {
            case AMETHYST,JASPER,OPAL -> setRarity(Rarity.COMMON);
            case JADE,TOPAZ,AMBER -> setRarity(Rarity.UNCOMMON);
            case SAPPHIRE,EMERALD,RUBY -> setRarity(Rarity.RARE);
            case DIAMOND -> setRarity(Rarity.EPIC);
        }
        setDisplayName(StringUtil.color("&f" + getName() + " Gemstone"));
        List<String> desc = new ArrayList<>();
        desc.add(StringUtil.color("&7This Gemstone is used to upgrade"));
        desc.add(StringUtil.color("&7your Armor to Tier "+type.getTier()));
        setDescription(desc);
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        return null;
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
        p.openInventory(new GemStoneGUI().getInv(p));
    }

    @Override
    public UUID getHeadUUID() {
        return type.getSkullUUID();
    }

    @Override
    public String getHeadBase64() {
        return type.getSkullBase64();
    }


    public String getName() {
        String name = WordUtils.capitalizeFully(type.name());
        return getColor()+name;
    }

    public ChatColor getColor() {
        return ChatColor.of("#"+type.getHex());
    }

    public int getIntFromHex() {
        return Integer.decode("#"+type.getHex());
    }

    public enum GemstoneType {
        AMETHYST,
        JASPER,
        OPAL,
        JADE,
        TOPAZ,
        AMBER,
        SAPPHIRE,
        EMERALD,
        RUBY,
        DIAMOND,
        ;

        public String getHex() {
            return switch (this) {
                case AMETHYST -> "A12FFA";
                case JASPER -> "A30071";
                case OPAL -> "EEEEEE";
                case JADE -> "49FCAA";
                case TOPAZ -> "E6D48A";
                case AMBER -> "FA7D33";
                case SAPPHIRE -> "2441FA";
                case EMERALD -> "39AD39";
                case RUBY -> "A1000B";
                case DIAMOND -> "4DF5FC";
            };
        }

        public String getSkullBase64() {
            return switch (this) {
                case DIAMOND -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc3MmIzODgyZTE1NGMyMjU4OGU4MDFkZmFmMDI5Mzc4OTdjODg5ZTk5ZWYwZDkyZDhmNWJmYTM5MDJjZWI2MCJ9fX0=";
                case RUBY -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZkODEwNjhjYmRmNGEzNjQyMzFhMjY0NTNkNmNkNjYwYTAwOTVmOWNkODc5NTMwN2M1YmU2Njc0Mjc3MTJlIn19fQ==";
                case EMERALD -> "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQxYzZlMjQ5ZGE4MTRmYmE3NjA0MzA1YWM0OGZmY2ZjM2U1MGYxNTJiY2M4ZjBiYWVlYTMyOWU1MmQ4MTViMyJ9fX0=";
                case SAPPHIRE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGEwYWY5OWU4ZDg3MDMxOTRhODQ3YTU1MjY4Y2Y1ZWY0YWM0ZWIzYjI0YzBlZDg2NTUxMzM5ZDEwYjY0NjUyOSJ9fX0=";
                case AMBER -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTczYmNmYzM5ZWI4NWRmMTg0ODUzNTk4NTIxNDA2MGExYmQxYjNiYjQ3ZGVmZTQyMDE0NzZlZGMzMTY3MTc0NCJ9fX0=";
                case TOPAZ -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYzOTI3NzNkMTE0YmUzMGFlYjNjMDljOTBjYmU2OTFmZmVhY2ViMzk5YjUzMGZlNmZiNTNkZGMwY2VkMzcxNCJ9fX0=";
                case JADE -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyODJjNmJiODM0M2UwZjBkNjFlZTA3NDdkYWRhNzUzNDRmMzMyZTlmZjBhY2FhM2FkY2RmMDkzMjFkM2RkIn19fQ==";
                case OPAL -> "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE4YTRlZDQxYjlkMGUwNzFiZmMxMjNiNmE3YWZhZDM0MDE4NGI4Y2U2Zjc1OTcxNjRmNzZhMTg5YzYyMTkxMSJ9fX0=";
                case JASPER -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTczNTExZTUwNGMzMTZiMTM5ZWRiMzVmZWJlNzNlZjU5MWMwZjQ1NWU4Y2FmOWVlMzUzYmMxMmI2YzE0YTkyMiJ9fX0=";
                case AMETHYST -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkYjU5MjYwODk1NTc4ZDM3ZTU5NTA1ODgwNjAyZGU5NDBiMDg4ZTVmZmY4ZGEzZTY1MjAxZDczOWM4NmU4NCJ9fX0=";
            };
        }

        public UUID getSkullUUID() {
            return switch (this) {
                case DIAMOND -> UUID.fromString("46a1f161-f967-4e86-ab1c-092a68645cc4");
                case RUBY -> UUID.fromString("a5397f9d-5a9b-4a25-9655-dbebe393e17b");
                case EMERALD -> UUID.fromString("234b2ba5-0e80-4815-b7a5-953042a8e5e0");
                case SAPPHIRE -> UUID.fromString("6865c46d-57b5-4342-be16-1ad4e1bd995d");
                case AMBER -> UUID.fromString("c03de23a-7265-4ec2-ac2a-1a4b1c7905b7");
                case TOPAZ -> UUID.fromString("0638c209-a618-44e6-b372-410f3fd7db02");
                case JADE -> UUID.fromString("2701b03a-7f00-482e-927f-42a4f114a68a");
                case OPAL -> UUID.fromString("88ca930f-5d1d-465c-a0dd-56ed4b9bea1a");
                case JASPER -> UUID.fromString("4ef6de3c-d7e1-44d8-bb5b-52f7ccb87505");
                case AMETHYST -> UUID.fromString("7d33e0eb-4a47-4aea-ad47-1355f460b403");
            };
        }

        public int getTier() {
            return switch (this) {
                case DIAMOND -> 10;
                case RUBY -> 9;
                case EMERALD -> 8;
                case SAPPHIRE -> 7;
                case AMBER -> 6;
                case TOPAZ -> 5;
                case JADE -> 4;
                case OPAL -> 3;
                case JASPER -> 2;
                case AMETHYST -> 1;
            };
        }

        public ItemID getItemID() {
            return switch (this) {
                case DIAMOND -> ItemID.DIAMOND_GEMSTONE;
                case RUBY -> ItemID.RUBY_GEMSTONE;
                case EMERALD -> ItemID.EMERALD_GEMSTONE;
                case SAPPHIRE -> ItemID.SAPPHIRE_GEMSTONE;
                case AMBER -> ItemID.AMBER_GEMSTONE;
                case TOPAZ -> ItemID.TOPAZ_GEMSTONE;
                case JADE -> ItemID.JADE_GEMSTONE;
                case OPAL -> ItemID.OPAL_GEMSTONE;
                case JASPER -> ItemID.JASPER_GEMSTONE;
                case AMETHYST -> ItemID.AMETHYST_GEMSTONE;
            };
        }
    }
}
