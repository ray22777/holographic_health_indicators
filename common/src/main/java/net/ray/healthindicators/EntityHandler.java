package net.ray.healthindicators;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.ray.HologramAPI.Hologram;
import net.ray.HologramAPI.HologramAPI;
import net.ray.healthindicators.config.ConfigGetter;
import net.ray.healthindicators.config.IndicatorConfig;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EntityHandler {
    public static final ConcurrentHashMap<Entity, EntityData> entityData = new ConcurrentHashMap<>();
    public static Entity lastLookedAtEntity = null;

    public static class EntityData {
        public Hologram hologram;
        public double previousHealth;
        public Component previousName;
        public long lastHurtTime;

        public EntityData(Hologram holo, float health, Component name) {
            this.hologram = holo;
            this.previousHealth = health;
            this.previousName = name;
            this.lastHurtTime = 0;
        }
    }

    public static void update() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        Player player = mc.player;
        Level level = player.level();
        if (!ConfigGetter.iconfig.enableIndicator) {
            entityData.forEach((entity, data) -> {
                if (data.hologram != null) {
                    data.hologram.remove();
                }
            });
            entityData.clear();
            lastLookedAtEntity = null;
            return;
        }
        Entity currentTarget = getTargetedEntity(mc);
        handleDisplayConditions(player, level, currentTarget, mc);
        cleanupOldHolograms(player, level);
    }

    private static Entity getTargetedEntity(Minecraft mc) {
        HitResult hit = mc.hitResult;
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hit).getEntity();
        }
        return null;
    }

    private static void handleDisplayConditions(Player player, Level level, Entity currentTarget, Minecraft mc) {
        boolean alwaysOn = IndicatorConfig.DisplayConditions.alwaysOn;
        boolean onAim = IndicatorConfig.DisplayConditions.onAim;
        boolean onDamage = IndicatorConfig.DisplayConditions.onDamage;
        boolean onlyDamaged = IndicatorConfig.DisplayConditions.onlyDamaged;
        int renderDistance = ConfigGetter.iconfig.renderDistance;
        AABB area = new AABB(
                player.getX() - renderDistance, player.getY() - renderDistance, player.getZ() - renderDistance,
                player.getX() + renderDistance, player.getY() + renderDistance, player.getZ() + renderDistance
        );

        List<Entity> nearbyEntities = level.getEntities(player, area);
        if (onAim && currentTarget != lastLookedAtEntity) {
            if (lastLookedAtEntity != null) {
                EntityData edata = entityData.get(lastLookedAtEntity);
                if (edata != null && edata.hologram != null) {
                    edata.hologram.visible(false);
                }
            }
            lastLookedAtEntity = currentTarget;
        }
        for (Entity entity : nearbyEntities) {
            if (entity == player || !(entity instanceof LivingEntity living) || !living.isAlive()) {
                continue;
            }

            double distance = player.distanceTo(entity);
            if (distance > renderDistance) {
                cleanupEntity(entity);
                continue;
            }

            boolean shouldRender = shouldRenderEntity(living, currentTarget, alwaysOn, onAim, onDamage, onlyDamaged, mc);

            if (shouldRender) {
                createOrUpdateHologram(player, living);
            } else {
                cleanupEntity(entity);
            }
        }
    }

    private static boolean shouldRenderEntity(LivingEntity entity, Entity currentTarget,
                                              boolean alwaysOn, boolean onAim, boolean onDamage,
                                              boolean onlyDamaged, Minecraft mc) {
        if (onAim && currentTarget == entity) {
            return true;
        }

        if (alwaysOn) {
            if (onlyDamaged) {
                return entity.getHealth() < entity.getMaxHealth();
            }
            return true;
        }

        if (onDamage) {
            if (entity.hurtTime > 0) {
                return true;
            }

            EntityData edata = entityData.get(entity);
            if (edata != null) {
                long currentTime = mc.level.getGameTime();
                long timeSinceHurt = currentTime - edata.lastHurtTime;
                return timeSinceHurt < IndicatorConfig.DisplayConditions.damageTicks;
            }
            if (entity.getHealth() < entity.getMaxHealth()) {
                return true;
            }
        }

        return false;
    }

    private static void createOrUpdateHologram(Player player, LivingEntity entity) {
        if (!entity.isAlive()) {
            cleanupEntity(entity);
            return;
        }

        Component text = IndicatorFormatter.text(entity);
        EntityData edata = entityData.get(entity);

        if (edata == null || edata.hologram == null) {
            createHologram(entity, text);
        } else {
            updateHologram(entity, edata, text);
        }
    }

    private static void createHologram(LivingEntity entity, Component text) {
        Hologram holo = HologramAPI.create(text, 0, 0, 0)
                .renderDistance(ConfigGetter.iconfig.renderDistance)
                .scale(ConfigGetter.iconfig.indicatorScale)
                .shadow(ConfigGetter.iconfig.shadow)
                .renderOnTop(ConfigGetter.iconfig.renderInfront)
                .trackEntity(entity.getId(), new Vec3(0, entity.getBbHeight() + ConfigGetter.iconfig.offset, 0));

        EntityData data = new EntityData(holo, entity.getHealth(), entity.getDisplayName());

        if (entity.hurtTime > 0) {
            data.lastHurtTime = Minecraft.getInstance().level.getGameTime();
        }

        entityData.put(entity, data);

        holo.onUpdate(h -> updateHologramText(h, entity));
    }
    private static void updateHologram(LivingEntity entity, EntityData edata, Component text) {
        float currentHealth = entity.getHealth();
        Component currentName = entity.getDisplayName();

        boolean healthChanged = Math.abs(edata.previousHealth - currentHealth) > 0.01;
        boolean nameChanged = !edata.previousName.equals(currentName);
        boolean isCurrentlyHurt = entity.hurtTime > 0;

        if (healthChanged || nameChanged) {
            edata.hologram.component(text);
            edata.previousHealth = currentHealth;
            edata.previousName = currentName;

            if (healthChanged) {
                edata.lastHurtTime = Minecraft.getInstance().level.getGameTime();
            }
        } else if (isCurrentlyHurt) {
            edata.lastHurtTime = Minecraft.getInstance().level.getGameTime();
        }

        edata.hologram.visible(true);
    }

    private static void updateHologramText(Hologram holo, LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            cleanupEntity(entity);
            return;
        }

        EntityData edata = entityData.get(entity);
        if (edata == null || edata.hologram != holo) {
            return;
        }

        Component newText = IndicatorFormatter.text(entity);
        if (!holo.component.equals(newText)) {
            holo.component(newText);
        }
    }

    private static void cleanupEntity(Entity entity) {
        EntityData edata = entityData.get(entity);
        if (edata != null && edata.hologram != null) {
            edata.hologram.visible(false);
        }
    }

    private static void cleanupOldHolograms(Player player, Level level) {
        entityData.entrySet().removeIf(entry -> {
            Entity entity = entry.getKey();
            EntityData edata = entry.getValue();

            if (entity == null || !entity.isAlive() || entity.isRemoved()) {
                if (edata.hologram != null) {
                    edata.hologram.remove();
                }
                return true;
            }

            double distance = player.distanceToSqr(entity);
            if (distance > ConfigGetter.iconfig.renderDistance * ConfigGetter.iconfig.renderDistance) {
                if (edata.hologram != null) {
                    edata.hologram.remove();
                }
                return true;
            }

            return false;
        });
    }
}