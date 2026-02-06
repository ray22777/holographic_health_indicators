package net.ray.healthindicators.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.ray.healthindicators.EntityHandler;

public final class HolographicIndicatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        // Update damage timers
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (client.player != null) {
//                DamageTracker.tick();
//            }
//        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            HolographicIndicatorCommand.register(dispatcher);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            EntityHandler.update();
        });
//        WorldRenderEvents.AFTER_TRANSLUCENT.register((context) -> {
//            IndicatorRenderer.updateHologramText(context.tickDelta());
//        });

    }
}