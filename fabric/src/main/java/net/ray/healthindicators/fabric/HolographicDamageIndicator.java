package net.ray.healthindicators.fabric;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.ray.healthindicators.config.ConfigGetter;
import net.ray.healthindicators.config.IndicatorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HolographicDamageIndicator implements ModInitializer {
    public static final String MOD_ID = "holographic_health_indicators";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(IndicatorConfig.class, GsonConfigSerializer::new);
        ConfigGetter.iconfig = AutoConfig.getConfigHolder(IndicatorConfig.class).getConfig();
    }

}