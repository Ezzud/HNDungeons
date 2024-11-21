package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.horizonnetwork.HNDungeons.utils.FormatUtil;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import gg.horizonnetwork.HNDungeons.utils.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonMob {

    private final EntityType entityType;
    @Getter
    private final String name;
    @Getter
    private final ConfigurationSection config;
    @Getter
    @Setter
    private DungeonInstance instance;
    @Getter
    private Entity entity = null;
    @Getter
    private Location location = null;
    @Getter
    private Boolean isInvincible = false;
    @Getter
    private Boolean hasAI = true;
    @Getter
    @Setter
    private String prefix = "";
    @Getter
    @Setter
    private String brightColor = "&r";
    @Getter
    @Setter
    private double rewardMultiplier = 1.0;
    @Getter
    @Setter
    private double healthMultiplier = 1.0;
    @Getter
    @Setter
    private Team team;
    private String rarity;
    @Getter
    @Setter
    private double health;
    @Getter
    private double maxHealth;

    public DungeonMob(EntityType entityType, String name, DungeonInstance instance, ConfigurationSection section) {
        this.entityType = entityType;
        this.name = name;
        this.instance = instance;
        this.config = section;
        this.selectRarity();
    }

    public void spawn(Location loc) {
        World w = loc.getWorld();
        if(w == null) return;
        this.entity = w.spawnEntity(loc, entityType);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(ChatUtil.formatDungeonMobsName(this, prefix + name));
        this.entity.setSilent(true);
        this.entity.setMetadata("isFromInstance", new FixedMetadataValue(HNDungeons.getInstance(), "1"));
        this.brightEntity();

        this.location = loc;
    }

    public void selectRarity() {
        ConfigurationSection rarities = config.getConfigurationSection("rarities");
        if(rarities == null) {
            Logger.error("Unable to find rarities of mob " + name + " using default values...");
            return;
        }
        Set<String> rareKeys = rarities.getKeys(false);
        int randomNumber = RandomUtil.getRandomNumberInRange(1, 1000);

        for(String key : rareKeys) {
            double rarityValue = rarities.getDouble(key + ".chances") * 10;
            if(randomNumber <= rarityValue) {
                this.healthMultiplier = rarities.getDouble(key + ".health-multiplier");
                this.rewardMultiplier = rarities.getDouble(key + ".reward-multiplier");
                this.brightColor = rarities.getString(key + ".color");
                this.prefix = rarities.getString(key + ".prefix");
                this.rarity = key;
                Logger.info("&bMob " + name + " &bspawned as rarity " + this.prefix + " &bin instance " + this.instance.getId().toString());
                break;
            }
        }
    }

    public void updateName() {
        LivingEntity e = (LivingEntity) entity;
        if(isInvincible) {
            e.setCustomName(ChatUtil.formatDungeonMobsName(this, prefix + name));
        } else {
            e.setCustomName(ChatUtil.formatDungeonMobsName(this, prefix + name + this.getHealthDisplay()));
        }
    }

    public String getHealthDisplay() {
        return " &c" + FormatUtil.formatDbl(this.getHealth()) + "❤";
    }

    public void brightEntity() {
        if(!Objects.equals(this.brightColor, "&r")) {
            LivingEntity e = (LivingEntity) entity;
            ScoreboardManager m = HNDungeons.getInstance().getServer().getScoreboardManager();
            assert m != null;
            Scoreboard board = m.getMainScoreboard();
            Team t;
            if(board.getTeam(this.rarity) != null) {
                t = board.getTeam(this.rarity);
            } else
                t = board.registerNewTeam(this.rarity);
            this.team = t;
            assert t != null;
            t.setColor(Objects.requireNonNull(ChatColor.getByChar(this.brightColor.charAt(1))));
            t.addEntry(e.getUniqueId().toString());
            e.setGlowing(true);
        }
    }

    public void despawn() {
        if(this.entity == null) return;
        this.instance.removeEntity(this);
        this.entity.remove();
        this.entity = null;
    }

    public void disableAI() {
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        en.setAI(false);
        this.hasAI = false;
    }

    public void setMaxHealth(double health) {
        this.maxHealth = health;
        heal();
    }

    public void heal() {
        if (this.entity == null) return;
        this.health = this.maxHealth;
        this.updateName();
    }

    public boolean isInvincible() {
        List<MetadataValue> values = this.entity.getMetadata("isInstanceInvincible");
        return(!values.isEmpty() && values.get(0).value().equals("1"));
    }

    public void makeInvincible() {
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        en.setVisualFire(false);
        en.setMetadata("isInstanceInvincible", new FixedMetadataValue(HNDungeons.getInstance(), "1"));
        en.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        en.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        en.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 50, false, false));
        this.isInvincible = true;
    }
}
