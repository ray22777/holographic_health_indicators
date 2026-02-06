package net.ray.healthindicators.neoforge;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.ray.healthindicators.EntityHandler;
import net.ray.healthindicators.config.ConfigGetter;
import net.ray.healthindicators.config.IndicatorConfig;

@Mod(HolographicHealthIndicators.MODID)
public class HolographicHealthIndicators {
    public static final String MODID = "holographic_health_indicators";

    public HolographicHealthIndicators() {
        AutoConfig.register(IndicatorConfig.class, GsonConfigSerializer::new);
        ConfigGetter.iconfig = AutoConfig.getConfigHolder(IndicatorConfig.class).getConfig();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (minecraft, parent) -> AutoConfig.getConfigScreen(IndicatorConfig.class, parent).get()
        );
    }
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        EntityHandler.update();
    }
}