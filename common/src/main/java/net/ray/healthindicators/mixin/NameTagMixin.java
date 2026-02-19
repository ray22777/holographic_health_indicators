package net.ray.healthindicators.mixin;


import net.minecraft.client.renderer.entity.EntityRenderer;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NameTagMixin{
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void onRenderNameTag(CallbackInfo ci) {
            ci.cancel();
    }
}