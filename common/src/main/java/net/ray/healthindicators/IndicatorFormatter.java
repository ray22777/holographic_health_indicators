package net.ray.healthindicators;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.ray.healthindicators.config.ConfigGetter;
import net.ray.healthindicators.config.IndicatorConfig;

public class IndicatorFormatter {
    public static Component text(LivingEntity living){
        Component result = null;
        float health = living.getHealth();
        float maxHealth = living.getMaxHealth();
        Component entityName = living.getDisplayName();
        String format = ConfigGetter.iconfig.indicatorFormat;
        String colors;
        float percentage = living.getHealth()/living.getMaxHealth();
        if(percentage < 0.25){
            colors = IndicatorConfig.Colors.quarter;
        }
        else if(percentage < 0.5){
            colors = IndicatorConfig.Colors.half;
        }
        else if(percentage < 0.75){
            colors = IndicatorConfig.Colors.threefourths;
        }
        else{
            colors = IndicatorConfig.Colors.full;
        }
        if (format.contains("{name}")) {
            if(living.hasCustomName()){
                int namePos = format.indexOf("{name}");
                String before = format.substring(0, namePos);
                String after = format.substring(namePos + 6);
                before = before.replace("{maxHP}", String.format("%." + ConfigGetter.iconfig.decimal + "f", maxHealth))
                        .replace("{HP}", colors.concat(String.format("%." + ConfigGetter.iconfig.decimal + "f", health)));

                after = after.replace("{maxHP}", String.format("%." + ConfigGetter.iconfig.decimal + "f", maxHealth))
                        .replace("{HP}", colors.concat(String.format("%." + ConfigGetter.iconfig.decimal + "f", health)));
                result = ComponentUtilsParser.parseColorCodes(before).copy().append(entityName).append(ComponentUtilsParser.parseColorCodes(after).copy());
            }
            else{
                String str = format
                        .replace("{maxHP}", String.format("%." + ConfigGetter.iconfig.decimal + "f", maxHealth))
                        .replace("{name}", entityName.getString())
                        .replace("{HP}", colors.concat(String.format("%." + ConfigGetter.iconfig.decimal + "f", health)));
                result = ComponentUtilsParser.parseColorCodes(str);
            }

        }
        else{
            String str = format
                    .replace("{maxHP}", String.format("%." + ConfigGetter.iconfig.decimal + "f", maxHealth))
                    .replace("{HP}", colors.concat(String.format("%." + ConfigGetter.iconfig.decimal + "f", health)));
            result = ComponentUtilsParser.parseColorCodes(str);
        }
        return result;
    }
}
