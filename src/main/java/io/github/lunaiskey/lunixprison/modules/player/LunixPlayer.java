package io.github.lunaiskey.lunixprison.modules.player;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.XPBoost;
import io.github.lunaiskey.lunixprison.modules.boosters.Booster;
import io.github.lunaiskey.lunixprison.modules.boosters.BoosterType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.ArmorStorage;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.ChatColorStorage;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.CurrencyStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class LunixPlayer {

    private final UUID pUUID;
    private String name;
    private int rank;
    private final CurrencyStorage currencyStorage;
    private PickaxeStorage pickaxeStorage;
    private final ArmorStorage armorStorage;
    private ChatColorStorage chatColorStorage;
    private List<Booster> boosters;
    private Map<BoosterType,Integer> maxBooster;
    private ChatReplyType chatReplyType = null;


    public LunixPlayer(UUID pUUID, String name, int rank,CurrencyStorage currencyStorage, PickaxeStorage pickaxeStorage, ArmorStorage armorStorage, ChatColorStorage chatColorStorage, List<Booster> boosters) {
        this.pUUID = pUUID;
        this.name = name;
        this.rank = rank;
        if (currencyStorage == null) {
            currencyStorage = new CurrencyStorage();
        }
        this.currencyStorage = currencyStorage;
        if (pickaxeStorage == null) {
            pickaxeStorage = new PickaxeStorage(pUUID);
        }
        this.pickaxeStorage = pickaxeStorage;
        if (armorStorage == null) {
            armorStorage = new ArmorStorage();
        }
        this.armorStorage = armorStorage;
        if (chatColorStorage == null) {
            chatColorStorage = new ChatColorStorage();
        }
        this.chatColorStorage = chatColorStorage;
        this.boosters = Objects.requireNonNullElseGet(boosters, ArrayList::new);
    }

    public LunixPlayer(UUID pUUID, String name) {
        this(pUUID,name,0,null,null,null,null,null);
    }

    public UUID getpUUID() {
        return pUUID;
    }

    public String getName() {
        return name;
    }

    public BigInteger getTokens() {
        return currencyStorage.getTokens();
    }

    public long getGems() {
        return currencyStorage.getGems();
    }

    public long getLunixPoints() {
        return currencyStorage.getLunixPoints();
    }

    public BigInteger getCurrency(CurrencyType type) {
        BigInteger amount = BigInteger.ZERO;
        switch (type) {
            case GEMS -> amount = BigInteger.valueOf(getGems()) ;
            case LUNIX_POINTS -> amount = BigInteger.valueOf(getLunixPoints());
            case TOKENS -> amount = getTokens();
        }
        return amount;
    }

    public int getRank() {
        return rank;
    }


    public PickaxeStorage getPickaxeStorage() {
        return pickaxeStorage;
    }

    public Map<ArmorSlot, Armor> getArmor() {
        return armorStorage.getArmor();
    }

    public Armor getHelmet() {
        return armorStorage.getArmor().get(ArmorSlot.HELMET);
    }

    public Armor getChestplate() {
        return armorStorage.getArmor().get(ArmorSlot.CHESTPLATE);
    }

    public Armor getLeggings() {
        return armorStorage.getArmor().get(ArmorSlot.LEGGINGS);
    }

    public Armor getBoots() {
        return armorStorage.getArmor().get(ArmorSlot.BOOTS);
    }

    public List<Booster> getBoosters() {
        return boosters;
    }

    public ChatReplyType getChatReplyType() {
        return chatReplyType;
    }

    public BigInteger getCashback() {
        return currencyStorage.getCashback();
    }

    public boolean isArmorEquiped() {
        return armorStorage.isArmorEquiped();
    }

    public void setGems(long gems) {
        currencyStorage.setGems(gems);
    }

    public void setTokens(BigInteger tokens) {
        currencyStorage.setTokens(tokens);
    }

    public void setLunixPoints(long lunixPoints) {
        currencyStorage.setLunixPoints(lunixPoints);
    }

    public void setName(String name) {
        PlayerManager.get().getPlayerNameMap().remove(this.name);
        this.name = name;
        PlayerManager.get().getPlayerNameMap().put(name.toUpperCase(),this.pUUID);
    }

    public void setRank(int rank) {
        this.rank = rank;
        PMine mine = PMineManager.get().getPMine(pUUID);
        if (mine != null) {
            mine.checkMineBlocks();
        }
    }

    public void setArmorEquiped(boolean armorEquiped) {
        armorStorage.setArmorEquiped(armorEquiped);
    }

    public void setPickaxeStorage(PickaxeStorage pickaxeStorage) {
        this.pickaxeStorage = pickaxeStorage;
    }

    public void setChatReplyType(ChatReplyType chatReplyType) {
        this.chatReplyType = chatReplyType;
    }

    public void setCashback(BigInteger cashback) {
        currencyStorage.setCashback(cashback);
    }

    public void giveTokens(BigInteger tokens) { currencyStorage.giveTokens(tokens); }
    public void giveTokens(long tokens) { currencyStorage.giveTokens(tokens); }
    public void giveGems(long gems) {
        currencyStorage.giveGems(gems);
    }
    public void giveLunixPoints(long lunixPoints) {
        currencyStorage.giveLunixPoints(lunixPoints);
    }

    public void giveCurrency(CurrencyType type, long amount) {
        currencyStorage.giveCurrency(type,amount);
    }

    public void giveCurrency(CurrencyType type, BigInteger amount) {
        currencyStorage.giveCurrency(type,amount);
    }

    public void takeTokens(BigInteger tokens, boolean giveCashBack) {
        currencyStorage.takeTokens(tokens,giveCashBack,pUUID);
    }
    public void takeGems(long gems) {
        currencyStorage.takeGems(gems);
    }
    public void takeLunixPoints(long lunixPoints) {
        currencyStorage.takeLunixPoints(lunixPoints);
    }

    public void takeCurrency(CurrencyType type, BigInteger amount, boolean giveCashBack) {
        currencyStorage.takeCurrency(type,amount,giveCashBack,pUUID);
    }

    public void setArmor(Map<ArmorSlot, Armor> armor) {
        armorStorage.setArmor(armor);
    }

    public double getBaseMultiplier() {
        return 0;
    }

    public double getRankMultiplier() {
        double rank = Math.max(getRank(),0);
        if (rank <= 100) {
            return 0.1D*rank;
        } else {
            return (0.1*100D) + ((rank-100)*0.025);
        }
    }

    public double getArmorMultiplier() {
        double multiplier = 0;
        SalesBoost boost = (SalesBoost) AbilityType.SALES_BOOST.getAbility();
        for (Armor armor : getArmor().values()) {
            multiplier += boost.getMultiplier(armor.getAbilties().get(AbilityType.SALES_BOOST));
        }
        return multiplier;
    }

    public double getBoosterMultiplier() {
        double multiplier = 0;
        for (Booster booster : boosters) {
            if (booster.getType() != BoosterType.SALES) {
                continue;
            }
            if (booster.getMultiplier() > multiplier) {
                multiplier = booster.getMultiplier();
            }
        }
        return multiplier;
    }

    public int getXPBoostTotal() {
        int total = 0;
        XPBoost boost = (XPBoost) AbilityType.XP_BOOST.getAbility();
        for (Armor armor : getArmor().values()) {
            total += boost.getBoost(armor.getAbilties().get(AbilityType.XP_BOOST));
        }
        return total;
    }

    /**
     * All multipliers added together, don't multiply the original amount by this, do amount + amount*getTotalMultiplier because this can return 0.
     * @return Total Multiplier, can be 0.
     */
    public double getTotalMultiplier() {
        return getBaseMultiplier()+getRankMultiplier()+getArmorMultiplier()+getBoosterMultiplier();
    }

    public ItemID getSelectedGemstone() {
        return armorStorage.getSelectedGemstone();
    }

    public void setSelectedGemstone(ItemID selectedGemstone) {
        armorStorage.setSelectedGemstone(selectedGemstone);
    }

    public int getGemstoneCount() {
        return armorStorage.getGemstoneCount();
    }

    public void setGemstoneCount(int gemstoneCount) {
        armorStorage.setGemstoneCount(gemstoneCount);
    }

    public ChatColorStorage getChatColorStorage() {
        return chatColorStorage;
    }

    public void save() {
        File file = new File(LunixPrison.getPlugin().getDataFolder() + "/playerdata/" + pUUID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        saveRootData(data);
        saveCurrencyData(data);
        savePickaxeData(data);
        saveArmorData(data);
        saveNameAndTextColorData(data);
        try {
            data.save(file);
        } catch (IOException e) {
            LunixPrison.getPlugin().getLogger().severe("Failed to save " + pUUID + "'s player data.");
            e.printStackTrace();
        }
    }

    private void saveRootData(FileConfiguration data) {
        data.set("name",name);
        data.set("rank",rank);
        data.set("selectedGemstone",armorStorage.getSelectedGemstone().name());
        data.set("gemstoneCount",armorStorage.getGemstoneCount());
        data.set("cashback",currencyStorage.getCashback());
    }

    private void saveCurrencyData(FileConfiguration data) {
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", currencyStorage.getTokens());
        currencyMap.put("gems", currencyStorage.getGems());
        currencyMap.put("lunixpoints", currencyStorage.getLunixPoints());
        data.createSection("currencies", currencyMap);
    }

    private void savePickaxeData(FileConfiguration data) {
        Map<String, Object> pickaxeData = new LinkedHashMap<>();
        pickaxeData.put("blocksBroken", pickaxeStorage.getBlocksBroken());
        Map<String, Object> enchantMap = new LinkedHashMap<>();
        for (EnchantType type : pickaxeStorage.getEnchants().keySet()) {
            enchantMap.put(type.name(), pickaxeStorage.getEnchants().get(type));
        }
        pickaxeData.put("enchants",enchantMap);
        List<String> disabledEnchantsList = new ArrayList<>();
        for (EnchantType type : pickaxeStorage.getDisabledEnchants()) {
            disabledEnchantsList.add(type.name());
        }
        pickaxeData.put("disabledEnchants",disabledEnchantsList);
        List<String> disabledMessagesList = new ArrayList<>();
        for (EnchantType type : pickaxeStorage.getDisabledMessages()) {
            disabledMessagesList.add(type.name());
        }
        pickaxeData.put("disabledMessages",disabledMessagesList);
        pickaxeData.put("rename", pickaxeStorage.getRename());
        data.createSection("pickaxe", pickaxeData);
    }

    private void saveArmorData(FileConfiguration data) {
        Map<String, Object> armorData = new LinkedHashMap<>();
        armorData.put("isArmorEquiped",armorStorage.isArmorEquiped());
        for (Armor armor : armorStorage.getArmor().values()) {
            Map<String, Object> pieceData = new LinkedHashMap<>();
            pieceData.put("tier",armor.getTier());
            if (armor.hasCustomColor()) {
                pieceData.put("customColor",armor.getCustomColor().asRGB());
            }
            Map<String,Object> abilityData = new LinkedHashMap<>();
            for(AbilityType type : armor.getAbilties().keySet()) {
                int abilityLevel = armor.getAbilties().get(type);
                if (abilityLevel > 0) {
                    abilityData.put(type.name(),abilityLevel);
                }
            }
            if (abilityData.size() > 0) {
                pieceData.put("abilities",abilityData);
            }
            armorData.put(armor.getSlot().name(),pieceData);
        }
        data.createSection("armor",armorData);
    }

    private void saveNameAndTextColorData(FileConfiguration data) {
        Map<String, Object> nameAndTextColorData = new LinkedHashMap<>();

        //Saves Chat UserName Color Data
        Map<String,Object> nameColorData = new LinkedHashMap<>();
        if (chatColorStorage.getSelectedNameColor() != null) nameColorData.put("selectedColor",chatColorStorage.getSelectedNameColor().name());
        List<String> selectedFormats = new ArrayList<>();
        for (LunixChatColor color : chatColorStorage.getSelectedNameFormats()) {
            selectedFormats.add(color.name());
        }
        nameColorData.put("selectedFormats",selectedFormats);
        List<String> unlockedNameColorAndFormats = new ArrayList<>();
        for (LunixChatColor color : chatColorStorage.getUnlockedNameColorAndFormats()) {
            unlockedNameColorAndFormats.add(color.name());
        }
        nameColorData.put("unlockedColorsAndFormats",unlockedNameColorAndFormats);
        nameAndTextColorData.put("name",nameColorData);

        //Saves Chat Text Color Data
        Map<String,Object> textColorData = new LinkedHashMap<>();
        if (chatColorStorage.getSelectedTextColor() != null) textColorData.put("selectedColor",chatColorStorage.getSelectedTextColor().name());
        List<String> unlockedTextColorAndFormats = new ArrayList<>();
        for (LunixChatColor color : chatColorStorage.getUnlockedTextColors()) {
            unlockedTextColorAndFormats.add(color.name());
        }
        textColorData.put("unlockedColorsAndFormats",unlockedTextColorAndFormats);
        nameAndTextColorData.put("text",textColorData);

        data.createSection("chatcolor",nameAndTextColorData);
    }
}
