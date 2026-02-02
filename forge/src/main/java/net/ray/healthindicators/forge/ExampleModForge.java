package net.ray.healthindicators.forge;

import net.minecraftforge.fml.common.Mod;

import net.ray.healthindicators.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
