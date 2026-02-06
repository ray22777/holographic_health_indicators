package net.ray.healthindicators.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = "better-damage-indicator")
public class IndicatorConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip() //Enable health indicators
    public boolean enableIndicator = true;

    @ConfigEntry.Gui.Tooltip() //Changes how far damage indicators will be shown
    public int renderDistance = 30;

    @ConfigEntry.Gui.CollapsibleObject
    public DisplayConditions displayConditions = new DisplayConditions(); //Conditions to show indicator

    public static class DisplayConditions {
        @ConfigEntry.Gui.Tooltip //when you damage the entity
        public static boolean onDamage = true;
        @ConfigEntry.Gui.Tooltip
        public static boolean onAim = true; //when you look at the entity
        @ConfigEntry.Gui.Tooltip
        public static boolean alwaysOn = false; //always shows when inside render distance
        @ConfigEntry.Gui.Tooltip
        public static int damageTicks = 100; //how long after damage to still show.
        @ConfigEntry.Gui.Tooltip
        public static boolean onlyDamaged = true; //Show only if the entity is not a max health.(Damaged)
    }

    @ConfigEntry.Gui.Tooltip(count = 3)  //Formatting for health indicator text,
    // supporting the use of minecraft color codes (e.g. §a).
    // Placeholders: {maxHP} {HP} {name}.
    public String indicatorFormat = "&b{name}&r: {HP}&7/&r{maxHP}";

    @ConfigEntry.Gui.CollapsibleObject
    public Colors healthColor = new Colors(); //Set colors for {HP} below thresholds

    public static class Colors {
        public static String full = "&a";
        public static String threefourths = "&a";
        public static String half = "&e";
        public static String quarter = "&c";
    }

    @ConfigEntry.Gui.Tooltip //How many trailing decimals
    public int decimal = 1;

    @ConfigEntry.Gui.Tooltip() //How big the damage indicator will be.
    public float indicatorScale = 1;

    @ConfigEntry.Gui.Tooltip() //How much to offset indicator in blocks.
    public float offset = 0.5f;

    @ConfigEntry.Gui.Tooltip(count = 1) //Enables shadow for texts.
    public boolean shadow = true;

    @ConfigEntry.Gui.Tooltip(count = 3) //Renders the health indicator over everything,
                                        // including blocks and entities.
                                        //(BUGGY in <1.21.10, DO NOT ENABLE)
    public boolean renderInfront = false;

    @ConfigEntry.Gui.Tooltip() //Disable nametag to avoid interference.
    public boolean disableNametag = true;

//    @ConfigEntry.Category("damage")
//    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
//    @ConfigEntry.Gui.Tooltip(count = 2) //Damage source filter
//    public DamageTracker.DAMAGE_SOURCE damageSource = DamageTracker.DAMAGE_SOURCE.ALL;

}