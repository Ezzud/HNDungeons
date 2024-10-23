package gg.horizonnetwork.HNDungeons.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DungeonMob {

    private final EntityType entityType;
    private final String name;
    private Entity entity = null;

    public DungeonMob(EntityType entityType, String name) {
        this.entityType = entityType;
        this.name = name;
    }


    public void spawn(Location loc) {
        World w = loc.getWorld();
        if(w == null) return;
        this.entity = w.spawnEntity(loc, entityType);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(name);
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
    }

    public void makeInvincible() {
        if (this.entity == null) return;
        LivingEntity en  = (LivingEntity) this.entity;
        en.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        en.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 50, false, false));
    }
}
