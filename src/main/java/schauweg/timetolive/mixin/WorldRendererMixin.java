package schauweg.timetolive.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import schauweg.timetolive.CountdownRenderer;
import schauweg.timetolive.config.TTLConfigManger;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private Frustum capturedFrustum;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projection, CallbackInfo ci) {
        if (!TTLConfigManger.getConfig().isOverlayActive()) return;
        CountdownRenderer.render(matrices, tickDelta, camera, projection, this.capturedFrustum);
    }

}
