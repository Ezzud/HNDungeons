package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class DungeonMob {

    private final EntityType entityType;
    @Getter
    private final String name;
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

    public DungeonMob(EntityType entityType, String name, DungeonInstance instance) {
        this.entityType = entityType;
        this.name = name;
        this.instance = instance;
    }


    public void spawn(Location loc) {
        World w = loc.getWorld();
        if(w == null) return;
        this.entity = w.spawnEntity(loc, entityType);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(ChatUtil.formatDungeonMobsName(this, name));
        this.location = loc;
    }

    public void despawn() {
        if(this.entity == null) return;
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
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        Objects.requireNonNull(en.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
    }

    public void heal() {
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        en.setHealth(Objects.requireNonNull(en.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
    }

    public double getMaxHealth() {
        if (this.entity == null) return -1;
        LivingEntity en  = (LivingEntity) this.entity;
        return Objects.requireNonNull(en.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
    }

    public void makeInvincible() {
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        en.setVisualFire(false);
        en.setMetadata("isFromInstance", new FixedMetadataValue(HNDungeons.getInstance(), "1"));
        en.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        en.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        en.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 50, false, false));
        this.isInvincible = true;
    }
}
