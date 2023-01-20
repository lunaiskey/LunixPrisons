package io.github.lunaiskey.lunixprison.modules.player;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixPickaxe;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.XPBoost;
import io.github.lunaiskey.lunixprison.modules.boosters.Booster;
import io.github.lunaiskey.lunixprison.modules.boosters.BoosterType;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class LunixPlayer {
    private BigInteger tokens;
    private long gems;
    private long lunixPoints;
    private int rank;
    private final UUID pUUID;
    private String name;
    private LunixPickaxe pickaxe;
    private Map<ArmorSlot,Armor> armor;
    private boolean isArmorEquiped;
    private ItemID selectedGemstone;
    private int gemstoneCount;

    private List<Booster> boosters;

    private Map<BoosterType,Integer> maxBooster;

    private ChatReplyType chatReplyType = null;
    private BigInteger cashback;

    public LunixPlayer(UUID pUUID, String name, BigInteger tokens, long gems, long lunixPoints, int rank, LunixPickaxe pickaxe, boolean isArmorEquiped, Map<ArmorSlot,Armor> armor, ItemID selectedGemstone, int gemstoneCount, List<Booster> boosters, BigInteger cashback) {
        this.pUUID = pUUID;
        this.name = name;
        this.tokens = tokens;
        this.gems = gems;
        this.lunixPoints = lunixPoints;
        this.rank = rank;
        if (pickaxe == null) {
            pickaxe = new LunixPickaxe(pUUID);
        }
        this.pickaxe = pickaxe;
        this.isArmorEquiped = isArmorEquiped;
        this.gemstoneCount = gemstoneCount;
        this.armor = Objects.requireNonNullElseGet(armor, LinkedHashMap::new);
        this.armor.putIfAbsent(ArmorSlot.HELMET,new Armor(ArmorSlot.HELMET));
        this.armor.putIfAbsent(ArmorSlot.CHESTPLATE,new Armor(ArmorSlot.CHESTPLATE));
        this.armor.putIfAbsent(ArmorSlot.LEGGINGS,new Armor(ArmorSlot.LEGGINGS));
        this.armor.putIfAbsent(ArmorSlot.BOOTS,new Armor(ArmorSlot.BOOTS));
        this.selectedGemstone = Objects.requireNonNullElse(selectedGemstone, ItemID.AMETHYST_GEMSTONE);
        this.boosters = Objects.requireNonNullElseGet(boosters, ArrayList::new);
        this.cashback = cashback;
        save();
    }

    public LunixPlayer(UUID pUUID, String name) {
        this(pUUID,name,BigInteger.ZERO,0,0,0,new LunixPickaxe(pUUID),false,null,ItemID.AMETHYST_GEMSTONE,0,null,BigInteger.ZERO);
    }

    public String getName() {
        return name;
    }

    public long getGems() {
        return gems;
    }

    public BigInteger getTokens() {
        return tokens;
    }

    public long getLunixPoints() {
        return lunixPoints;
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

    public UUID getpUUID() {
        return pUUID;
    }

    public LunixPickaxe getPickaxe() {
        return pickaxe;
    }

    public Map<ArmorSlot, Armor> getArmor() {
        return armor;
    }

    public Armor getHelmet() {
        return armor.get(ArmorSlot.HELMET);
    }

    public Armor getChestplate() {
        return armor.get(ArmorSlot.CHESTPLATE);
    }

    public Armor getLeggings() {
        return armor.get(ArmorSlot.LEGGINGS);
    }

    public Armor getBoots() {
        return armor.get(ArmorSlot.BOOTS);
    }

    public List<Booster> getBoosters() {
        return boosters;
    }

    public ChatReplyType getChatReplyType() {
        return chatReplyType;
    }

    public BigInteger getCashback() {
        return cashback;
    }

    public boolean isArmorEquiped() {
        return isArmorEquiped;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public void setTokens(BigInteger tokens) {
        this.tokens = tokens;
    }

    public void setLunixPoints(long lunixPoints) {
        this.lunixPoints = lunixPoints;
    }

    public void setName(String name) {
        LunixPrison.getPlugin().getPlayerManager().getPlayerNameMap().remove(this.name);
        this.name = name;
        LunixPrison.getPlugin().getPlayerManager().getPlayerNameMap().put(name.toUpperCase(),this.pUUID);
    }

    public void setRank(int rank) {
        this.rank = rank;
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(pUUID);
        if (mine != null) {
            mine.checkMineBlocks();
        }
    }

    public void setArmorEquiped(boolean armorEquiped) {
        isArmorEquiped = armorEquiped;
    }

    public void setPickaxe(LunixPickaxe pickaxe) {
        this.pickaxe = pickaxe;
    }

    public void setChatReplyType(ChatReplyType chatReplyType) {
        this.chatReplyType = chatReplyType;
    }

    public void setCashback(BigInteger cashback) {
        this.cashback = cashback;
    }

    public void giveTokens(BigInteger tokens) {this.tokens = this.tokens.add(tokens);}
    public void giveTokens(long tokens) {this.tokens = this.tokens.add(BigInteger.valueOf(tokens));}
    public void giveGems(long gems) {
        this.gems += gems;
    }
    public void giveLunixPoints(long pyrexPoints) {
        this.lunixPoints += pyrexPoints;
    }

    public void giveCurrency(CurrencyType type, long amount) {
        giveCurrency(type, BigInteger.valueOf(amount));
    }

    public void giveCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case GEMS -> giveGems(amount.longValue());
            case LUNIX_POINTS -> giveLunixPoints(amount.longValue());
            case TOKENS -> giveTokens(amount);
        }
    }

    public void takeTokens(BigInteger tokens, boolean giveCashBack) {
        if (giveCashBack) {
            cashback = cashback.add(tokens.divide(new BigInteger("20")));
            Player player = Bukkit.getPlayer(pUUID);
            if (player != null) {
                player.sendMessage(StringUtil.color("&aYou gained some cashback, check /cashback"));
            }
        }
        this.tokens = this.tokens.subtract(tokens);
    }
    public void takeGems(long gems) {
        this.gems -= gems;
    }
    public void takeLunixPoints(long lunixPoints) {
        this.lunixPoints -= lunixPoints;
    }

    public void takeCurrency(CurrencyType type, BigInteger amount, boolean giveCashBack) {
        switch (type) {
            case GEMS -> takeGems(amount.longValue());
            case LUNIX_POINTS -> takeLunixPoints(amount.longValue());
            case TOKENS -> takeTokens(amount,giveCashBack);
        }
    }

    public void setArmor(Map<ArmorSlot, Armor> armor) {
        this.armor = armor;
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
        SalesBoost boost = (SalesBoost) LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap().get(AbilityType.SALES_BOOST);
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
        XPBoost boost = (XPBoost) LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap().get(AbilityType.XP_BOOST);
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
        if (selectedGemstone.name().contains("_GEMSTONE")) {
            return selectedGemstone;
        } else {
            return ItemID.AMETHYST_GEMSTONE;
        }
    }

    public void setSelectedGemstone(ItemID selectedGemstone) {
        if (selectedGemstone.name().contains("_GEMSTONE")) {
            this.selectedGemstone = selectedGemstone;
        } else {
            this.selectedGemstone = ItemID.AMETHYST_GEMSTONE;
        }
    }

    public int getGemstoneCount() {
        return gemstoneCount;
    }

    public void setGemstoneCount(int gemstoneCount) {
        this.gemstoneCount = gemstoneCount;
    }

    public void save() {
        File file = new File(LunixPrison.getPlugin().getDataFolder() + "/playerdata/" + pUUID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        saveRootData(data);
        saveCurrencyData(data);
        savePickaxeData(data);
        saveArmorData(data);
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
        data.set("selectedGemstone",selectedGemstone.name());
        data.set("gemstoneCount",gemstoneCount);
        data.set("cashback",cashback);
    }

    private void saveCurrencyData(FileConfiguration data) {
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", tokens);
        currencyMap.put("gems", gems);
        currencyMap.put("lunixpoints", lunixPoints);
        data.createSection("currencies", currencyMap);
    }

    private void savePickaxeData(FileConfiguration data) {
        Map<String, Object> pickaxeData = new LinkedHashMap<>();
        pickaxeData.put("blocksBroken",pickaxe.getBlocksBroken());
        Map<String, Object> enchantMap = new LinkedHashMap<>();
        List<String> disabledEnchantsList = new ArrayList<>();
        for (EnchantType type : pickaxe.getEnchants().keySet()) {
            enchantMap.put(type.name(),pickaxe.getEnchants().get(type));
        }
        pickaxeData.put("enchants",enchantMap);
        for (EnchantType type : pickaxe.getDisabledEnchants()) {
            disabledEnchantsList.add(type.name());
        }
        pickaxeData.put("disabledEnchants",disabledEnchantsList);
        pickaxeData.put("rename",pickaxe.getRename());
        data.createSection("pickaxe", pickaxeData);
    }

    private void saveArmorData(FileConfiguration data) {
        Map<String, Object> armorData = new LinkedHashMap<>();
        armorData.put("isArmorEquiped",isArmorEquiped);
        for (Armor armor : armor.values()) {
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
            armorData.put(armor.getType().name(),pieceData);
        }
        data.createSection("armor",armorData);
    }
}
