package net.ray.healthindicators.neoforge;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.event.TickEvent;
import net.ray.HologramAPI.HologramAPI;
import net.ray.healthindicators.EntityHandler;
import net.ray.healthindicators.config.ConfigGetter;
import net.ray.healthindicators.config.IndicatorConfig;

@Mod(BetterDamageIndicator.MODID)
public class BetterDamageIndicator {
    public static final String MODID = "holographic_health_indicators";

    public BetterDamageIndicator() {
        AutoConfig.register(IndicatorConfig.class, GsonConfigSerializer::new);
        ConfigGetter.iconfig = AutoConfig.getConfigHolder(IndicatorConfig.class).getConfig();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) ->
                                AutoConfig.getConfigScreen(IndicatorConfig.class, parent).get()
                )
        );
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        EntityHandler.update();
    }
}