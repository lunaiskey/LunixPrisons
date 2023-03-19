package io.github.lunaiskey.lunixprison.modules.player.chatcolor;

import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public enum LunixChatColor {
    AQUA("Aqua", ChatColor.AQUA,0, ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjllMTY5NzkzMDliNWE5YjY3M2Q2MGQxMzkwYmJhYjBkMDM4NWVhYzcyNTRkODI4YWRhMmEzNmE0NmY3M2E1OSJ9fX0=", UUID.fromString("ebd3c947-0e32-4f37-af49-5bc6f642de79"))),
    //BLACK("Black",ChatColor.BLACK,Material.BLACK_DYE),
    BLUE("Blue",ChatColor.BLUE,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I1MTA2YjA2MGVhZjM5ODIxNzM0OWYzY2ZiNGYyYzdjNGZkOWEwYjAzMDdhMTdlYmE2YWY3ODg5YmUwZmJlNiJ9fX0=",UUID.fromString("7c696517-5a5c-4ca7-99c9-86226b716ef8"))),
    DARK_AQUA("Cyan",ChatColor.DARK_AQUA,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc1YjdhYzlmMGM3MTIzMDNjZDNiNjU0ZTY0NmNlMWM0YmYyNDNhYjM0OGE2YTI1MzcwZjI2MDNlNzlhNjJhMCJ9fX0=",UUID.fromString("b3517e7e-0203-45a4-b72e-0bdf1a64f5a7"))),
    DARK_BLUE("Dark Blue",ChatColor.DARK_BLUE,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U3YWI3MTJjODdmNjdkNDhiOThmNzA2MzRkMWRjZmNkNTk4MGMzZDZmMGQ2MjJjZGMzMjMwOTEyMzYxYjU0ZSJ9fX0=",UUID.fromString("51784a4f-1c37-4b54-ba47-f65f0ed4b091"))),
    DARK_GRAY("Dark Gray",ChatColor.DARK_GRAY,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2FmNmZhYjc2N2NhNGQ3ZGY2MjE3Yjg5NWI2NjdiY2FjYzUyNGQ0MDcwNjg2MTlmODE5YTA3MGYzZjYyOWNlMCJ9fX0=",UUID.fromString("85f4a5f8-cb05-407c-9691-51fa6f975a34"))),
    DARK_GREEN("Dark Green",ChatColor.DARK_GREEN,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNlOWY0ZGJhZGRlMGY3MjdjNTgwM2Q3NWQ4YmIzNzhmYjlmY2I0YjYwZDMzYmVjMTkwOTJhM2EyZTdiMDdhOSJ9fX0=",UUID.fromString("c8752132-34f6-474b-b01e-46d94afb7c21"))),
    DARK_PURPLE("Purple", ChatColor.DARK_PURPLE,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY3ZjJiNTA2MzcwYzFlODRmOTBmYmYyOWM4MGUwY2I3ZTJhYzkzMjMwMzAxYjVkOGU0MmM2OGZkZGU4OWZlMCJ9fX0=",UUID.fromString("eb21356f-0818-4247-adcb-9cf6a585c76a"))),
    DARK_RED("Dark Red", ChatColor.DARK_RED,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY1ZjNiYWUwZDIwM2JhMTZmZTFkYzNkMTMwN2E4NmE2MzhiZTkyNDQ3MWYyM2U4MmFiZDlkNzhmOGEzZmNhIn19fQ==",UUID.fromString("ecf62be5-3060-40f6-bb4f-402e5257fb56"))),
    GOLD("Gold", ChatColor.GOLD,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE4OWYzNDdmNDI0NTBjZDJhMmU5YjhhNTM5ODgwN2QyOGM3ZjQyNTRiZDk5YThhNDk5Y2U1NDM1MzIwOTU1In19fQ==",UUID.fromString("74eac8b5-38f6-4a8b-9327-1c6eb605e748"))),
    GRAY("Light Gray", ChatColor.GRAY,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzMyOGRjZGUxNzNiZWZmOWYzZjQxYjkyMzIxM2ZjMWJiNzY3ODk2N2NjYjJlZGU3YTdjZjQwYjE4MzZiMWE3MyJ9fX0=",UUID.fromString("22fdeeef-d130-430c-afcc-ad74b2b85175"))),
    GREEN("Green", ChatColor.GREEN,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk4NWEyOTk1N2Q0MGZhNTY0ZDVlMzFjYmQ5MDVlMzY5NGE2MTYzOTNjZTEzNzEwYmZjMzFiMWI4YjBhNTIyZCJ9fX0=",UUID.fromString("ca73dccc-7e70-48e5-b257-2f70f48e99e8"))),
    LIGHT_PURPLE("Pink", ChatColor.LIGHT_PURPLE,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E5ZWE2ZTM2ZjllNTc5ZjU4NmFkYjE5MzdiYjE0Mzc3YjBkNzQwMzRmZmNiMjU1NmEyYWNiNDM1NjcxNDQ4ZiJ9fX0=",UUID.fromString("124455ea-a358-45af-b68b-75e37feffb8f"))),
    RED("Red",ChatColor.RED,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA2MmQ4ZDcyZjU4OTFjNzFmYWIzMGQ1MmUwNDgxNzk1YjNkMmQzZDJlZDJmOGI5YjUxN2Q3ZDI4MjFlMzVkNiJ9fX0=",UUID.fromString("01422ecb-fd4f-498c-a12b-f9e8b31c0f6f"))),
    WHITE("White", ChatColor.WHITE,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGUwZThhY2FiYWQyN2Q0NjE2ZmFlOWU0NzJjMGRlNjA4NTNkMjAzYzFjNmYzMTM2N2M5MzliNjE5ZjNlMzgzMSJ9fX0=",UUID.fromString("9f37c1fe-afdf-4411-9030-d829cb47a3ab"))),
    YELLOW("Yellow", ChatColor.YELLOW,0,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjAwYmY0YmYxNGM4Njk5YzBmOTIwOWNhNzlmZTE4MjUzZTkwMWU5ZWMzODc2YTJiYTA5NWRhMDUyZjY5ZWJhNyJ9fX0=",UUID.fromString("8aecd33d-b4a9-40f9-bccf-f18da225254f"))),
    BOLD("Bold",ChatColor.BOLD,1,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQyYmVkOWVjZmRiZGJhOWQwMDY0ZTM5MzYxNjhjMGNlNjg0Y2MzNDY2MTBkMjA5N2Q0Mjk0NGViZjgxZWNjOSJ9fX0=",UUID.fromString("4529cced-8df6-4497-822a-f4aa1fbd09b9"))),
    ITALIC("Italics",ChatColor.ITALIC,1,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYzYjgxNThlNGMzZDcxN2MwZjEwNjFiYzVhNGJhMzQ5ODZiYjE4NTFjMDIxZTViNTA3MGM2MmQzMTJlMjI1NCJ9fX0=",UUID.fromString("935754de-37e4-40b6-bb05-0c2d37949f2c"))),
    STRIKETHROUGH("StrikeThrough",ChatColor.STRIKETHROUGH,1,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEyMjExMWVkMmMxYWMwMzc5OWU0NDYzY2U1YTg2OTA4MzcyYTIxOTI5MjEyOWY0M2MzNmY1ZTc3Y2NmMGM1YiJ9fX0=",UUID.fromString("1a4371f5-11b7-465c-8818-acb0431cc996"))),
    UNDERLINE("Underline",ChatColor.UNDERLINE,1,ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2YzMjhjM2FmZTdmM2U4YzFiYWU4Njk5ZTNkY2FjZTBiYjYzYjQzMTQ1OTQxMjE3NTUxY2ZkNmU2NTg1M2Y4NiJ9fX0=", UUID.fromString("e25c05eb-34ef-4a30-8c0f-827ad9868116"))),
    ;

    private String name;
    private ChatColor color;
    private int codeType;
    private ItemStack playerHead;
    private static Map<ChatColor, LunixChatColor> map = new HashMap<>();
    private static List<LunixChatColor> colors = new ArrayList<>();
    private static List<LunixChatColor> formats = new ArrayList<>();
    private static List<LunixChatColor> misc = new ArrayList<>();

    static {
        for (LunixChatColor wrapper : values()) {
            map.put(wrapper.color,wrapper);
            switch (wrapper.codeType) {
                case -1 -> misc.add(wrapper);
                //case 0 -> colors.add(wrapper);
                case 1 -> formats.add(wrapper);
            }
        }
        colors.addAll(List.of(
                DARK_RED,RED,GOLD,YELLOW,WHITE,
                DARK_GREEN,GREEN,AQUA,DARK_AQUA,GRAY,
                DARK_BLUE,BLUE,LIGHT_PURPLE,DARK_PURPLE,DARK_GRAY));
    }

    /**
     * @param codeType -1 == No Type, 0 == Color, 1 == Format.
     */
    LunixChatColor(String name, ChatColor color, int codeType, ItemStack playerHead) {
        this.name = name;
        this.color = color;
        this.codeType = codeType;
        this.playerHead = playerHead;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public ItemStack getPlayerHead() {
        return playerHead;
    }

    public static LunixChatColor getWrapperFromBukkitChatColor(ChatColor color) {
        return map.get(color);
    }

    public static List<LunixChatColor> getColors() {
        return colors;
    }

    public static List<LunixChatColor> getFormats() {
        return formats;
    }

    public boolean isColor() {
        return codeType == 0;
    }

    public boolean isFormat() {
        return codeType == 1;
    }
}
