package io.github.lunaiskey.lunixprison.modules.player;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.ChatColorStorage;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class PlayerManager {

    private static PlayerManager instance;
    private Map<UUID, LunixPlayer> playerMap = new HashMap<>();
    private Map<String, UUID> playerNameMap = new HashMap<>();

    private PlayerManager() {

    }

    public static PlayerManager get() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }


    public LunixPlayer getLunixPlayer(UUID playerUUID) {
        return playerMap.get(playerUUID);
    }

    public LunixPlayer getLunixPlayer(String player) {
        if (player == null) {
            return null;
        }
        return getLunixPlayer(playerNameMap.get(player.toUpperCase()));
    }

    public void createLunixPlayer(UUID pUUID) {
        Player player = Bukkit.getPlayer(pUUID);
        if (player != null) {
            playerMap.put(pUUID,new LunixPlayer(pUUID, player.getName()));
        } else {
            playerMap.put(pUUID,new LunixPlayer(pUUID, Bukkit.getOfflinePlayer(pUUID).getName()));
        }
    }

    public void loadPlayers() {
        File[] playerFiles = new File(LunixPrison.getPlugin().getDataFolder(), "playerdata").listFiles(new PMineManager.IsPMineFile());
        if (playerFiles == null) {
            return;
        }
        for (File file : playerFiles) {
            loadPlayer(file);
        }
    }

    private void loadPlayer(File file) {
        FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
        UUID pUUID = UUID.fromString(file.getName().replace(".yml",""));
        //Load Lunix Data from map
        Map<String,Object> playerData = fileConf.getValues(true);
        String cachedName = (String) playerData.getOrDefault("name",Bukkit.getOfflinePlayer(pUUID).getName());
        LunixPlayer lunixPlayer = new LunixPlayer(pUUID,cachedName);
        loadRootData(lunixPlayer,playerData);
        loadCurrencyData(lunixPlayer,playerData);
        loadPickaxeData(lunixPlayer,playerData);
        loadArmorData(lunixPlayer,playerData);
        LoadNameAndTextColorData(lunixPlayer,playerData);
        //Finished Loading
        playerMap.put(pUUID,lunixPlayer);
        //LunixPlayer testPlayer = playerMap.get(pUUID);
        //LunixPrison.getPlugin().getLogger().info(testPlayer.getName()+": "+testPlayer.getTokens());
        playerNameMap.put(cachedName.toUpperCase(),pUUID);
    }

    private void loadRootData(LunixPlayer lunixPlayer,Map<String,Object> playerData) {
        String cachedName = lunixPlayer.getName();
        if (cachedName.equals("null")) {
            String name = Bukkit.getOfflinePlayer(lunixPlayer.getpUUID()).getName();
            if (name != null) {
                lunixPlayer.setName(name);
            }
        }
        int rank = ((Number) playerData.getOrDefault("rank",0)).intValue();
        ItemID selectedGemstone = ItemID.valueOf((String) playerData.getOrDefault("selectedGemstone",ItemID.AMETHYST_GEMSTONE.name()));
        int gemstoneCount = ((Number) playerData.getOrDefault("gemstoneCount",0)).intValue();
        BigInteger cashback = new BigInteger(playerData.getOrDefault("cashback", BigInteger.ZERO).toString());

        lunixPlayer.setRank(rank);
        lunixPlayer.setSelectedGemstone(selectedGemstone);
        lunixPlayer.setGemstoneCount(gemstoneCount);
        lunixPlayer.setCashback(cashback);
    }

    private void loadCurrencyData(LunixPlayer lunixPlayer,Map<String,Object> playerData) {
        //Load Currencies from map
        Map<String, Object> currencyMap = ((MemorySection) playerData.get("currencies")).getValues(true);
        BigInteger tokens = new BigInteger(currencyMap.getOrDefault("tokens", BigInteger.ZERO).toString());
        long gems = ((Number) currencyMap.getOrDefault("gems", 0L)).longValue();
        long lunixPoints = ((Number) currencyMap.getOrDefault("lunixpoints", 0L)).longValue();
        lunixPlayer.setTokens(tokens);
        lunixPlayer.setGems(gems);
        lunixPlayer.setLunixPoints(lunixPoints);
    }
    private void loadPickaxeData(LunixPlayer lunixPlayer,Map<String,Object> playerData) {
        //Load Pickaxe Data from file.
        Map<String,Object> pickaxeMap = ((MemorySection) playerData.get("pickaxe")).getValues(true);
        Map<String,Object> pickaxeEnchant = ((MemorySection) pickaxeMap.get("enchants")).getValues(true);
        Map<EnchantType,Integer> pickaxeEnchantMap = new HashMap<>();
        for (String enchant : pickaxeEnchant.keySet()) {
            pickaxeEnchantMap.put(EnchantType.valueOf(enchant),(int)pickaxeEnchant.get(enchant));
        }
        Object pickaxeDisabledEnchantsObject = pickaxeMap.get("disabledEnchants");
        List<String> pickaxeDisabledEnchantList = pickaxeDisabledEnchantsObject == null ? new ArrayList<>() : (List<String>) pickaxeDisabledEnchantsObject;
        Set<EnchantType> pickaxeDisabledEnchants = new HashSet<>();
        for (String str : pickaxeDisabledEnchantList) {
            try {
                pickaxeDisabledEnchants.add(EnchantType.valueOf(str));
            } catch (Exception ignored) {}
        }
        Object pickaxeDisabledMessagesObject = pickaxeMap.get("disabledMessages");
        List<String> pickaxeDisabledMessagesList = pickaxeDisabledMessagesObject == null ? new ArrayList<>() : (List<String>) pickaxeDisabledMessagesObject;
        Set<EnchantType> pickaxeDisabledMessages = new HashSet<>();
        for (String str : pickaxeDisabledMessagesList) {
            try {
                pickaxeDisabledMessages.add(EnchantType.valueOf(str));
            } catch (Exception ignored) {}
        }
        long blocksBroken = ((Number) pickaxeMap.getOrDefault("blocksBroken",0L)).longValue();
        String rename = (String) pickaxeMap.getOrDefault("rename",null);
        PickaxeStorage pickaxe = new PickaxeStorage(lunixPlayer.getpUUID(),pickaxeEnchantMap,pickaxeDisabledEnchants,pickaxeDisabledMessages,blocksBroken,rename);
        lunixPlayer.setPickaxeStorage(pickaxe);
    }

    private void loadArmorData(LunixPlayer lunixPlayer,Map<String,Object> playerData) {
        //Load Armor data from file.
        Map<String,Object> armorData = ((MemorySection) playerData.get("armor")).getValues(true);
        boolean isArmorEquiped = (boolean) armorData.getOrDefault("isArmorEquiped",false);
        Map<ArmorSlot, Armor> armorMap = new HashMap<>();
        for (ArmorSlot type : ArmorSlot.values()) {
            Map<String,Object> armorPieceMap = ((MemorySection) armorData.get(type.name())).getValues(true);
            Color color = armorPieceMap.get("customColor") != null ? Color.fromRGB((Integer) armorPieceMap.get("customColor")) : null;
            int tier = (int) armorPieceMap.getOrDefault("tier",0);
            Map<AbilityType, Integer> abilityMap = new HashMap<>();
            MemorySection abilitiesSection =(MemorySection) armorPieceMap.get("abilities");
            if (abilitiesSection != null) {
                Map<String,Object> abilities = abilitiesSection.getValues(true);
                for (String str : abilities.keySet()) {
                    int level = 0;
                    try {
                        level = (Integer) abilities.get(str);
                    } catch (Exception ignored) {
                    }
                    AbilityType abilityType = AbilityType.valueOf(str);
                    abilityMap.put(abilityType,level);
                }
            }
            Armor piece = new Armor(type,tier,color,abilityMap);
            armorMap.put(type,piece);
        }
        lunixPlayer.setArmorEquiped(isArmorEquiped);
        lunixPlayer.setArmor(armorMap);
    }

    private void LoadNameAndTextColorData(LunixPlayer lunixPlayer, Map<String,Object> playerData) {
        ChatColorStorage storage = lunixPlayer.getChatColorStorage();
        Object chatColorDataObject = playerData.get("chatcolor");
        if (chatColorDataObject == null) return;
        Map<String,Object> chatColorData = ((MemorySection) chatColorDataObject).getValues(true);

        Object nameColorDataObject = chatColorData.get("name");
        if (nameColorDataObject == null) return;
        Map<String,Object> nameColorData = ((MemorySection) nameColorDataObject).getValues(true);

        LunixChatColor selectedNameColor;
        String nameColorDataText = (String) nameColorData.getOrDefault("selectedColor",null);
        try {
            selectedNameColor = nameColorDataText == null ? null : LunixChatColor.valueOf(nameColorDataText);
        } catch (IllegalArgumentException ignored) {
            selectedNameColor = null;
        }
        storage.setSelectedNameColor(selectedNameColor);
        Set<LunixChatColor> selectedNameFormats = storage.getSelectedNameFormats();
        List<String> selectedNameFormatsList = (List<String>) nameColorData.getOrDefault("selectedFormats",new ArrayList<String>());
        for (String str : selectedNameFormatsList) {
            try {
                selectedNameFormats.add(LunixChatColor.valueOf(str));
            } catch (IllegalArgumentException ignored) {}
        }
        Set<LunixChatColor> unlockedNameColorAndFormats = storage.getUnlockedNameColorAndFormats();
        List<String> unlockedNameColorAndFormatsList = (List<String>) nameColorData.getOrDefault("unlockedColorsAndFormats",new ArrayList<String>());
        for (String str : unlockedNameColorAndFormatsList) {
            try {
                unlockedNameColorAndFormats.add(LunixChatColor.valueOf(str));
            } catch (IllegalArgumentException ignored) {}
        }

        Object textColorDataObject = chatColorData.get("text");
        if (textColorDataObject == null ) return;
        Map<String,Object> textColorData = ((MemorySection) textColorDataObject).getValues(true);
        LunixChatColor selectedTextColor;
        String textColorDataText = (String) textColorData.getOrDefault("selectedColor",null);
        try {
            selectedTextColor = textColorDataText == null ? null : LunixChatColor.valueOf(textColorDataText);
        } catch (IllegalArgumentException ignored) {
            selectedTextColor = null;
        }
        storage.setSelectedTextColor(selectedTextColor);
        Set<LunixChatColor> unlockedTextColors = storage.getUnlockedTextColors();
        List<String> unlockedTextColorsList = (List<String>) textColorData.getOrDefault("unlockedColorsAndFormats",new ArrayList<String>());
        for (String str : unlockedTextColorsList) {
            try {
                unlockedTextColors.add(LunixChatColor.valueOf(str));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public int getLunixItemCount(Player p, ItemID id) {
        int count = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            CompoundTag tag = NBTTags.getLunixDataTag(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(id.name())) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }

    public void removeLunixItem(Player p, ItemID id, int amount) {
        ItemStack[] inv = p.getInventory().getContents();
        for (int i = 0;i<inv.length;i++) {
            ItemStack item = inv[i];
            CompoundTag tag = NBTTags.getLunixDataTag(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(id.name())) {
                    if (amount > 0) {
                        if (amount > item.getAmount()) {
                            amount -= item.getAmount();
                            inv[i] = null;
                        } else {
                            item.setAmount(item.getAmount()-amount);
                            amount = 0;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        p.getInventory().setContents(inv);
    }

    public int getGemstoneCountMax(Player p) {
        LunixPlayer player = getPlayerMap().get(p.getUniqueId());
        return switch (player.getSelectedGemstone()) {
            case AMETHYST_GEMSTONE -> 200;
            case JASPER_GEMSTONE -> 225;
            case OPAL_GEMSTONE -> 250;
            case JADE_GEMSTONE -> 275;
            case TOPAZ_GEMSTONE -> 300;
            case AMBER_GEMSTONE -> 325;
            case SAPPHIRE_GEMSTONE -> 350;
            case EMERALD_GEMSTONE -> 400;
            case RUBY_GEMSTONE -> 450;
            case DIAMOND_GEMSTONE -> 500;
            default -> 100000;
        };

    }

    public void tickGemstoneCount(Player p) {
        tickGemstoneCount(p,1);
    }

    public void tickGemstoneCount(Player p, int amount) {
        LunixPlayer player = getPlayerMap().get(p.getUniqueId());
        int combined = amount + player.getGemstoneCount();
        int ticks = (combined - combined%(getGemstoneCountMax(p)))/(getGemstoneCountMax(p));
        if (ticks > 0) {
            LunixItem lunixItem = ItemManager.get().getLunixItem(player.getSelectedGemstone());
            while (ticks > 0) {
                p.getInventory().addItem(lunixItem.getItemStack());
                ticks--;
            }
        }
        player.setGemstoneCount(combined%getGemstoneCountMax(p));
    }

    public List<UUID> getSortedByRank() {
        List<UUID> sortedValues = new ArrayList<>(playerMap.keySet());
        sortedValues.sort(Comparator.comparingInt(o -> playerMap.get(o).getRank()));
        return sortedValues;
    }

    @Nullable
    public UUID getPlayerUUID(String name) {
        return getPlayerNameMap().get(name.toUpperCase());
    }

    public void payForBlocks(Player player, long amount) {
        payForBlocks(player,PlayerManager.get().getLunixPlayer(player.getUniqueId()),amount);
    }

    public void payForBlocks(LunixPlayer lunixPlayer, long amount) {
        payForBlocks(Bukkit.getPlayer(lunixPlayer.getpUUID()),lunixPlayer,amount);
    }

    public void payForBlocks(Player p, LunixPlayer lunixPlayer,long amount) {
        Pair<Integer,Integer> pair = PMineManager.get().getGridLocation(p.getLocation());
        PMine mine = PMineManager.get().getPMine(pair.getLeft(),pair.getRight());

        PickaxeStorage pickaxe = lunixPlayer.getPickaxeStorage();
        double multiplier = lunixPlayer.getTotalMultiplier();
        int fortune = Math.max(pickaxe.getEnchants().getOrDefault(EnchantType.FORTUNE,5),5);

        BigDecimal newAmount = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(mine.getSellPrice())).multiply(BigDecimal.valueOf(fortune).multiply(BigDecimal.valueOf(40)));
        BigInteger tokens = newAmount.multiply(BigDecimal.valueOf(multiplier+1)).toBigInteger();

        if (mine.getOwner() == lunixPlayer.getpUUID()) {
            lunixPlayer.giveTokens(tokens);
        } else {
            LunixPlayer mineOwner = PlayerManager.get().getPlayerMap().get(mine.getOwner());
            double tax = mine.getMineTax()/100D;
            double mineOwnerAmount = 1-tax;
            lunixPlayer.giveTokens(new BigDecimal(tokens).multiply(BigDecimal.valueOf(mineOwnerAmount)).toBigInteger());
            mineOwner.giveTokens(new BigDecimal(tokens).multiply(BigDecimal.valueOf(tax)).toBigInteger());
            LunixPrison.getPlugin().getSavePending().add(mineOwner.getpUUID());
        }
    }

    public void checkPlayerData() {
        PMineManager pMineManager = PMineManager.get();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!(getPlayerMap().containsKey(p.getUniqueId()))) {
                createLunixPlayer(p.getUniqueId());
            }
            getPlayerMap().get(p.getUniqueId()).setName(p.getName());
            if (pMineManager.getPMine(p.getUniqueId()) == null) {
                pMineManager.newPMine(p.getUniqueId());
            }
            LunixPrison.getPlugin().getSavePending().add(p.getUniqueId());
        }
    }


    public Map<UUID, LunixPlayer> getPlayerMap() {
        return playerMap;
    }

    public Map<String, UUID> getPlayerNameMap() {
        return playerNameMap;
    }
}
