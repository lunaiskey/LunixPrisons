package io.github.lunaiskey.lunixprison.player;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.pickaxe.LunixPickaxe;
import io.github.lunaiskey.lunixprison.player.armor.Armor;
import io.github.lunaiskey.lunixprison.player.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.abilitys.XPBoost;
import io.github.lunaiskey.lunixprison.player.boosters.Booster;
import io.github.lunaiskey.lunixprison.player.boosters.BoosterType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
    private final Map<ArmorSlot,Armor> armor;
    private boolean isArmorEquiped;
    private ItemID selectedGemstone;
    private int gemstoneCount;

    private List<Booster> boosters;

    private Map<BoosterType,Integer> maxBooster;

    public LunixPlayer(UUID pUUID, String name, BigInteger tokens, long gems, long lunixPoints, int rank, LunixPickaxe pickaxe, boolean isArmorEquiped, Map<ArmorSlot,Armor> armor, ItemID selectedGemstone, int gemstoneCount, List<Booster> boosters) {
        this.pUUID = pUUID;
        this.name = name;
        this.tokens = tokens;
        this.gems = gems;
        this.lunixPoints = lunixPoints;
        this.rank = rank;
        this.pickaxe = pickaxe;
        this.isArmorEquiped = isArmorEquiped;
        this.gemstoneCount = gemstoneCount;
        this.armor = Objects.requireNonNullElseGet(armor, HashMap::new);
        this.armor.putIfAbsent(ArmorSlot.HELMET,new Armor(ArmorSlot.HELMET));
        this.armor.putIfAbsent(ArmorSlot.CHESTPLATE,new Armor(ArmorSlot.CHESTPLATE));
        this.armor.putIfAbsent(ArmorSlot.LEGGINGS,new Armor(ArmorSlot.LEGGINGS));
        this.armor.putIfAbsent(ArmorSlot.BOOTS,new Armor(ArmorSlot.BOOTS));
        this.selectedGemstone = Objects.requireNonNullElse(selectedGemstone, ItemID.AMETHYST_GEMSTONE);
        this.boosters = Objects.requireNonNullElseGet(boosters, ArrayList::new);
        save();
    }

    public LunixPlayer(UUID pUUID, String name) {
        this(pUUID,name,BigInteger.ZERO,0,0,0,new LunixPickaxe(pUUID),false,null,ItemID.AMETHYST_GEMSTONE,0,null);
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
        PMine mine = LunixPrison.getPlugin().getPmineManager().getPMine(pUUID);
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

    public void takeTokens(BigInteger tokens) {
        this.tokens = this.tokens.subtract(tokens);
    }
    public void takeGems(long gems) {
        this.gems -= gems;
    }
    public void takeLunixPoints(long lunixPoints) {
        this.lunixPoints -= lunixPoints;
    }

    public void takeCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case GEMS -> takeGems(amount.longValue());
            case LUNIX_POINTS -> takeLunixPoints(amount.longValue());
            case TOKENS -> takeTokens(amount);
        }
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
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", tokens);
        currencyMap.put("gems", gems);
        currencyMap.put("lunixpoints", lunixPoints);
        data.createSection("currencies", currencyMap);
        Map<String, Object> playerData = new LinkedHashMap<>();
        playerData.put("name",name);
        playerData.put("rank",rank);
        playerData.put("selectedGemstone",selectedGemstone.name());
        playerData.put("gemstoneCount",gemstoneCount);
        data.createSection("playerData",playerData);
        Map<String, Object> armorData = new LinkedHashMap<>();
        armorData.put("isArmorEquiped",isArmorEquiped);
        data.createSection("armor",armorData);
        if (pickaxe != null) {
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Object> enchantMap = new LinkedHashMap<>();
            List<String> disabledEnchantsList = new ArrayList<>();
            map.put("blocksBroken",pickaxe.getBlocksBroken());
            for (EnchantType type : pickaxe.getEnchants().keySet()) {
                enchantMap.put(type.name(),pickaxe.getEnchants().get(type));
            }
            for (EnchantType type : pickaxe.getDisabledEnchants()) {
                disabledEnchantsList.add(type.name());
            }
            data.createSection("pickaxeData", map);
            data.createSection("pickaxeData.enchants",enchantMap);
            data.set("pickaxeData.disabledEnchants",disabledEnchantsList);
        }
        for (Armor armor : armor.values()) {
            Map<String, Object> pieceData = new LinkedHashMap<>();
            pieceData.put("tier",armor.getTier());
            if (armor.hasCustomColor()) {
                pieceData.put("customColor",armor.getCustomColor().asRGB());
            }
            data.createSection("armor."+armor.getType().getName(),pieceData);
            Map<String,Object> abilityData = new LinkedHashMap<>();
            for(AbilityType type : armor.getAbilties().keySet()) {
                abilityData.put(type.name(),armor.getAbilties().get(type));
            }
            data.createSection("armor."+armor.getType().getName()+".abilities",abilityData);
        }
        try {
            data.save(file);
        } catch (IOException e) {
            LunixPrison.getPlugin().getLogger().severe("Failed to save " + pUUID + "'s player data.");
            e.printStackTrace();
        }
    }
}
