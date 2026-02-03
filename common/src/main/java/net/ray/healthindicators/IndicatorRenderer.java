package net.ray.healthindicators;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.ray.HologramAPI.Hologram;
import net.ray.healthindicators.config.ConfigGetter;

import static net.ray.healthindicators.EntityHandler.*;


public class IndicatorRenderer {
    public static void renderEntityIndicators(float tickDelta) {
        if (Minecraft.getInstance() == null ||
                Minecraft.getInstance().player == null ||
                Minecraft.getInstance().player.level() == null) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        for (Entity entity : entitiesToRender) {
            if (entity == null) continue;
            Hologram holo = entityData.get(entity).hologram;
            if (!entity.isAlive()){
                entitiesToRender.remove(entity);
                entityData.remove(entity);
                holo.remove();
                continue;
            }
            double distance = player.distanceTo(entity);
            if(distance > ConfigGetter.iconfig.renderDistance){
                entitiesToRender.remove(entity);
                entityData.remove(entity);
                holo.remove();
                continue;
            }

            holo.x = entity.getPosition(tickDelta).x;
            holo.y = entity.getPosition(tickDelta).y + entity.getBbHeight() + ConfigGetter.iconfig.offset;
            holo.z = entity.getPosition(tickDelta).z;
//            holo.x = entity.getX();
//            holo.z = entity.getZ();
//            holo.y = entity.getY();
        }
    }
//    public static int countHolograms() {
//        try {
//            // Get the private HOLOGRAMS map
//            Class<?> managerClass = Class.forName("net.ray.HologramAPI.HologramRenderer$HologramManager");
//            Field hologramsField = managerClass.getDeclaredField("HOLOGRAMS");
//            hologramsField.setAccessible(true); // Make it accessible
//
//            Map<?, ?> holograms = (Map<?, ?>) hologramsField.get(null);
//            return holograms.size();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }

}
