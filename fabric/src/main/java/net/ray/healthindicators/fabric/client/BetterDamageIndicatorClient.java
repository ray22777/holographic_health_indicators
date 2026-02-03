package net.ray.healthindicators.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.ray.healthindicators.EntityHandler;
import net.ray.healthindicators.IndicatorRenderer;

public final class BetterDamageIndicatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        // Update damage timers
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (client.player != null) {
//                DamageTracker.tick();
//            }
//        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            DamageIndicatorCommand.register(dispatcher);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            EntityHandler.update();
        });
        WorldRenderEvents.AFTER_TRANSLUCENT.register((context) -> {
            IndicatorRenderer.renderEntityIndicators(context.tickDelta());
        });

    }
}