package net.ray.healthindicators.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.ray.healthindicators.EntityHandler;

@EventBusSubscriber(modid = HolographicHealthIndicators.MODID, value = Dist.CLIENT)
public class EntityTicker {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        EntityHandler.update();
    }
}